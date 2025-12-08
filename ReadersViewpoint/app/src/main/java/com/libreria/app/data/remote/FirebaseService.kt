package com.libreria.app.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.libreria.app.data.model.Libro
import com.libreria.app.data.model.Movimiento
import com.libreria.app.data.model.UserProfile
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Servicio que encapsula todas las interacciones directas con las APIs de Firebase (Auth y Firestore).
 *
 * Maneja la lógica de bajo nivel para la persistencia, autenticación y obtención de datos remotos.
 *
 * @param auth Instancia de [FirebaseAuth].
 * @param db Instancia de [FirebaseFirestore].
 */
class FirebaseService(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val librosCol = db.collection("libros")
    private val movCol = db.collection("movimientos")
    private val usersCol = db.collection("users")

    /** Obtiene el usuario de Firebase autenticado actualmente. */
    fun currentUser(): FirebaseUser? = auth.currentUser

    /**
     * Inicia sesión de un usuario con email y contraseña.
     * @return El resultado de la tarea de inicio de sesión.
     */
    suspend fun signIn(email: String, password: String) = auth.signInWithEmailAndPassword(email, password).await()

    /**
     * Registra un nuevo usuario con email y contraseña en Firebase Authentication.
     * @return El resultado de la tarea de registro.
     */
    suspend fun signUp(email: String, password: String) = auth.createUserWithEmailAndPassword(email, password).await()

    /** Cierra la sesión del usuario actual. */
    fun signOut() = auth.signOut()


    /**
     * Crea o actualiza el documento de perfil de usuario en Firestore.
     * @param profile El [UserProfile] a guardar.
     */
    suspend fun setUserProfile(profile: UserProfile) {
        usersCol.document(profile.uid).set(profile).await()
    }

    /**
     * Obtiene el perfil de usuario de Firestore por su UID.
     * @param uid El UID del usuario.
     * @return El objeto [UserProfile] o null.
     */
    suspend fun getUserProfile(uid: String): UserProfile? {
        val doc = usersCol.document(uid).get().await()
        return if (doc.exists()) doc.toObject(UserProfile::class.java) else null
    }

    /**
     * Obtiene una lista de todos los libros de la colección "libros".
     * @return Lista de [Libro]s.
     */
    suspend fun fetchLibros(): List<Libro> {
        val snap = librosCol.get().await()
        return snap.documents.mapNotNull { d -> d.toObject(Libro::class.java) }
    }

    /**
     * Obtiene un libro específico por su ID.
     * @param id El ID del libro.
     * @return El objeto [Libro] o null.
     */
    suspend fun getLibroById(id: String): Libro? {
        val doc = librosCol.document(id).get().await()
        return if (doc.exists()) doc.toObject(Libro::class.java) else null
    }

    /**
     * Crea o actualiza un libro en la colección "libros".
     * @param libro El objeto [Libro] a guardar.
     */
    suspend fun upsertLibro(libro: Libro) {
        librosCol.document(libro.id).set(libro).await()
    }

    /**
     * Elimina un libro de la colección "libros" por su ID.
     * @param id El ID del libro a eliminar.
     */
    suspend fun deleteLibro(id: String) {
        librosCol.document(id).delete().await()
    }

    /**
     * Añade un nuevo documento de movimiento a la colección "movimientos".
     * @param m El objeto [Movimiento] a registrar.
     */
    suspend fun addMovimiento(m: Movimiento) {
        movCol.document(m.id).set(m).await()
    }

    /**
     * Obtiene una lista de movimientos de inventario, ordenados por fecha descendente.
     * @return Lista de [Movimiento]s.
     */
    suspend fun fetchMovimientos(): List<Movimiento> {
        val snap = movCol.orderBy("dateIso", Query.Direction.DESCENDING).get().await()
        return snap.documents.mapNotNull { it.toObject(Movimiento::class.java) }
    }

    /**
     * Crea un usuario en Firebase Auth y su perfil en Firestore.
     * @param email Email para la cuenta.
     * @param password Contraseña para la cuenta.
     * @param displayName Nombre de visualización.
     * @param role Rol del usuario (e.g., "admin", "employee").
     * @return El UID del usuario creado.
     * @throws Exception Si no se obtiene un UID.
     */
    suspend fun createAccount(email: String, password: String, displayName: String, role: String): String {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val uid = result.user?.uid ?: throw Exception("No uid")
        val profile = UserProfile(uid = uid, email = email, displayName = displayName, role = role)
        usersCol.document(uid).set(profile).await()
        return uid
    }

    /**
     * Elimina el documento de perfil de usuario de Firestore.
     * @param userId El UID del usuario cuyo perfil se va a eliminar.
     */
    suspend fun deleteUserAccount(userId: String) {
        db.collection("users").document(userId).delete().await()
    }

    /**
     * Obtiene un [Flow] de usuarios en tiempo real que tienen alguno de los roles especificados.
     *
     * @param roles Lista de roles a buscar (e.g., ["admin", "employee"]).
     * @return [Flow] de [List] de [UserProfile].
     */
    fun fetchUsersWithRoles(roles: List<String>): Flow<List<UserProfile>> = callbackFlow {

        val listener = usersCol
            .whereIn("role", roles)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val users = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(UserProfile::class.java)
                } ?: emptyList()

                trySend(users)
            }

        awaitClose {
            listener.remove()
        }
    }
}