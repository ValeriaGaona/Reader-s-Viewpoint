package com.libreria.app.data.repository

import com.libreria.app.data.model.Libro
import com.libreria.app.data.model.Movimiento
import com.libreria.app.data.model.UserProfile
import com.libreria.app.data.model.Ticket
import com.libreria.app.data.remote.FirebaseService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.tasks.await


/**
 * Repository central para el acceso a datos de la aplicación.
 *
 * Actúa como intermediario entre la capa de ViewModel y los servicios de datos remotos ([FirebaseService]),
 * proporcionando una API limpia para la gestión de libros, usuarios, tickets y movimientos.
 *
 * @param firebase Servicio que maneja la autenticación y las colecciones principales de Firestore.
 * @param db Instancia directa de [FirebaseFirestore] para operaciones específicas.
 */
class LibreriaRepository(
    private val firebase: FirebaseService,
    private val db: FirebaseFirestore
) {

    /** Obtiene una lista de todos los libros disponibles. */
    suspend fun getAllBooks(): List<Libro> = firebase.fetchLibros()

    /**
     * Obtiene un libro específico por su ID.
     * @param id El ID del libro.
     * @return El objeto [Libro] o null si no se encuentra.
     */
    suspend fun getBookById(id: String): Libro? = firebase.getLibroById(id)

    /**
     * Crea o actualiza un registro de libro.
     * @param libro El objeto [Libro] a guardar.
     */
    suspend fun upsertBook(libro: Libro) = firebase.upsertLibro(libro)

    /**
     * Elimina un libro por su ID.
     * @param id El ID del libro a eliminar.
     */
    suspend fun deleteBook(id: String) = firebase.deleteLibro(id)

    /**
     * Registra un nuevo movimiento de inventario.
     * @param m El objeto [Movimiento] a registrar.
     */
    suspend fun addMovement(m: Movimiento) = firebase.addMovimiento(m)

    /**
     * Obtiene una lista de todos los movimientos de inventario registrados.
     * @return Lista de [Movimiento]s.
     */
    suspend fun getMovements(): List<Movimiento> = firebase.fetchMovimientos()

    /**
     * Obtiene el perfil de un usuario por su ID de usuario (UID).
     * @param uid El UID del usuario.
     * @return El objeto [UserProfile] o null.
     */
    suspend fun getUserProfile(uid: String): UserProfile? = firebase.getUserProfile(uid)


    /**
     * Añade un nuevo ticket de venta a la base de datos de Firestore.
     * @param ticket El objeto [Ticket] a guardar.
     */
    suspend fun addTicket(ticket: Ticket) {
        db.collection("tickets").document(ticket.id).set(ticket).await()
    }

    /**
     * Obtiene una lista de todos los tickets de venta, ordenados por fecha descendente.
     * @return Lista de [Ticket]s.
     */
    suspend fun fetchTickets(): List<Ticket> = withContext(Dispatchers.IO) {
        db.collection("tickets")
            .orderBy("date", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .await()
            .toObjects(Ticket::class.java)
    }

    /**
     * Obtiene un ticket de venta específico por su ID.
     * @param ticketId El ID del ticket.
     * @return El objeto [Ticket] o null si no se encuentra.
     */
    suspend fun getTicketById(ticketId: String): Ticket? = withContext(Dispatchers.IO) {
        db.collection("tickets")
            .document(ticketId)
            .get()
            .await()
            .toObject(Ticket::class.java)
    }

    /**
     * Elimina el documento de perfil de usuario de Firestore.
     * @param userId El UID del usuario a eliminar.
     */
    suspend fun deleteUser(userId: String) {
        firebase.deleteUserAccount(userId)
    }

    /**
     * Crea una nueva cuenta de usuario en Firebase Auth y un perfil de usuario en Firestore.
     * @param email Email para la cuenta.
     * @param password Contraseña para la cuenta.
     * @param name Nombre de visualización.
     * @param role Rol del usuario (e.g., "admin", "employee").
     * @return El UID del usuario creado.
     */
    suspend fun createAccount(email: String, password: String, name: String, role: String) =
        firebase.createAccount(email, password, name, role)

    /**
     * Obtiene un [Flow] que emite una lista de usuarios cuyos roles están incluidos en la lista proporcionada.
     * Ideal para obtener empleados y administradores en tiempo real.
     *
     * @param roles Lista de roles a filtrar (e.g., ["admin", "employee"]).
     * @return [Flow] de [List] de [UserProfile].
     */
    fun getUsersWithRoles(roles: List<String>): Flow<List<UserProfile>> {
        return firebase.fetchUsersWithRoles(roles)
    }

    /**
     * Obtiene un [Flow] que emite los detalles del perfil de un usuario específico.
     * Esta es una escucha en tiempo real del documento de Firestore.
     *
     * @param uid El UID del usuario.
     * @return [Flow] que emite el objeto [UserProfile] o null.
     */
    fun getUserDetails(uid: String): Flow<UserProfile?> = callbackFlow {
        val userDocRef = db.collection("users").document(uid)

        val subscription = userDocRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                channel.trySend(null)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val user = snapshot.toObject(UserProfile::class.java)
                channel.trySend(user)
            } else {
                channel.trySend(null)
            }
        }

        awaitClose {
            subscription.remove()
        }
    }
}