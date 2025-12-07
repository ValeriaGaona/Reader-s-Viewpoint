package com.libreria.app.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.libreria.app.data.model.Libro
import com.libreria.app.data.model.Movimiento
import com.libreria.app.data.model.UserProfile
import kotlinx.coroutines.channels.awaitClose // ⬅️ AÑADIDO
import kotlinx.coroutines.flow.Flow // ⬅️ AÑADIDO
import kotlinx.coroutines.flow.callbackFlow // ⬅️ AÑADIDO
import kotlinx.coroutines.tasks.await

class FirebaseService(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val librosCol = db.collection("libros")
    private val movCol = db.collection("movimientos")
    private val usersCol = db.collection("users") // stores roles and display names

    // Auth helpers
    fun currentUser(): FirebaseUser? = auth.currentUser
    suspend fun signIn(email: String, password: String) = auth.signInWithEmailAndPassword(email, password).await()
    suspend fun signUp(email: String, password: String) = auth.createUserWithEmailAndPassword(email, password).await()
    fun signOut() = auth.signOut()


    suspend fun setUserProfile(profile: UserProfile) {
        usersCol.document(profile.uid).set(profile).await()
    }
    suspend fun getUserProfile(uid: String): UserProfile? {
        val doc = usersCol.document(uid).get().await()
        return if (doc.exists()) doc.toObject(UserProfile::class.java) else null
    }

    suspend fun fetchLibros(): List<Libro> {
        val snap = librosCol.get().await()
        return snap.documents.mapNotNull { d -> d.toObject(Libro::class.java) }
    }
    suspend fun getLibroById(id: String): Libro? {
        val doc = librosCol.document(id).get().await()
        return if (doc.exists()) doc.toObject(Libro::class.java) else null
    }
    suspend fun upsertLibro(libro: Libro) {
        librosCol.document(libro.id).set(libro).await()
    }
    suspend fun deleteLibro(id: String) {
        librosCol.document(id).delete().await()
    }

    suspend fun addMovimiento(m: Movimiento) {
        movCol.document(m.id).set(m).await()
    }
    suspend fun fetchMovimientos(): List<Movimiento> {
        // CORRECCIÓN: Usar DESCENDING, que es la constante correcta.
        val snap = movCol.orderBy("dateIso", Query.Direction.DESCENDING).get().await()
        return snap.documents.mapNotNull { it.toObject(Movimiento::class.java) }
    }

    suspend fun createAccount(email: String, password: String, displayName: String, role: String): String {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val uid = result.user?.uid ?: throw Exception("No uid")
        val profile = UserProfile(uid = uid, email = email, displayName = displayName, role = role)
        usersCol.document(uid).set(profile).await()
        return uid
    }

    suspend fun deleteUserAccount(userId: String) {
        // 1. ELIMINAR DATOS DEL USUARIO DE FIRESTORE (Colección "users")
        db.collection("users").document(userId).delete().await()

        // 2. ELIMINAR LA CUENTA DE FIREBASE AUTH
        // Nota: Solo un administrador autenticado con credenciales de Firebase Admin SDK
        // puede eliminar otros usuarios directamente. Si estás haciendo esto desde el cliente,
        // debes usar Cloud Functions o un mecanismo de seguridad diferente.
        // Si el usuario a eliminar es el usuario actualmente autenticado, usarías auth.currentUser.delete().

        // Si estás seguro de que el usuario que ejecuta esta acción es un Administrador
        // y estás utilizando Cloud Functions o un backend seguro para la eliminación de Auth:
        // Por ahora, asumiremos que la eliminación de datos de Firestore es suficiente o que
        // tienes un mecanismo de backend seguro.

        // Si estás intentando eliminar al *usuario actualmente logueado* (no es el caso aquí):
        // auth.currentUser?.delete()?.await()
    }

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