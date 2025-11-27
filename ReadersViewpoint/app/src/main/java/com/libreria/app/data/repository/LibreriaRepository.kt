package com.libreria.app.data.repository

import com.libreria.app.data.model.Libro
import com.libreria.app.data.model.Movimiento
import com.libreria.app.data.model.UserProfile
import com.libreria.app.data.remote.FirebaseService
class LibreriaRepository(private val firebase: FirebaseService) {
    suspend fun getAllBooks(): List<Libro> = firebase.fetchLibros()
    suspend fun getBookById(id: String): Libro? = firebase.getLibroById(id)
    suspend fun upsertBook(libro: Libro) = firebase.upsertLibro(libro)
    suspend fun deleteBook(id: String) = firebase.deleteLibro(id)
    suspend fun addMovement(m: Movimiento) = firebase.addMovimiento(m)
    suspend fun getMovements(): List<Movimiento> = firebase.fetchMovimientos()
    suspend fun getUserProfile(uid: String): UserProfile? = firebase.getUserProfile(uid)
    suspend fun createAccount(email: String, password: String, name: String, role: String) =
        firebase.createAccount(email, password, name, role)
}