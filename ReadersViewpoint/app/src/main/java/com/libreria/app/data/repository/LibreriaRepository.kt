package com.libreria.app.data.repository

import com.libreria.app.data.model.Libro
import com.libreria.app.data.model.Movimiento
import com.libreria.app.data.model.UserProfile
import com.libreria.app.data.model.Ticket // Asegúrate de que esta ruta sea correcta
import com.libreria.app.data.remote.FirebaseService

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose

// ✅ IMPORTACIONES CLAVE AÑADIDAS PARA SOLUCIONAR EL ERROR:
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// ✅ IMPORTACIÓN CLAVE PARA TAREAS DE FIREBASE:
import kotlinx.coroutines.tasks.await


class LibreriaRepository(
    private val firebase: FirebaseService,
    private val db: FirebaseFirestore
) {

    // Funciones existentes
    suspend fun getAllBooks(): List<Libro> = firebase.fetchLibros()
    suspend fun getBookById(id: String): Libro? = firebase.getLibroById(id)
    suspend fun upsertBook(libro: Libro) = firebase.upsertLibro(libro)
    suspend fun deleteBook(id: String) = firebase.deleteLibro(id)
    suspend fun addMovement(m: Movimiento) = firebase.addMovimiento(m)
    suspend fun getMovements(): List<Movimiento> = firebase.fetchMovimientos()
    suspend fun getUserProfile(uid: String): UserProfile? = firebase.getUserProfile(uid)


    suspend fun addTicket(ticket: Ticket) {
        db.collection("tickets").document(ticket.id).set(ticket).await()
    }


    suspend fun fetchTickets(): List<Ticket> = withContext(Dispatchers.IO) {
        db.collection("tickets")
            .orderBy("date", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .await()
            .toObjects(Ticket::class.java)
    }

    suspend fun getTicketById(ticketId: String): Ticket? = withContext(Dispatchers.IO) {
        db.collection("tickets")
            .document(ticketId)
            .get()
            .await()
            .toObject(Ticket::class.java)
    }


    suspend fun createAccount(email: String, password: String, name: String, role: String) =
        firebase.createAccount(email, password, name, role)

    fun getUsersWithRoles(roles: List<String>): Flow<List<UserProfile>> {
        return firebase.fetchUsersWithRoles(roles)
    }

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