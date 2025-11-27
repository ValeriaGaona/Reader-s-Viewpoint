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

//package com.libreria.app.data.repository
//
//import android.content.Context // 🚨 Necesario
//import com.libreria.app.data.local.LibreriaDatabase // Asume que tienes esto
//import com.libreria.app.data.remote.FirebaseService
//
//class LibreriaRepository(
//    context: Context, // 👈 AHORA ACEPTA EL CONTEXTO
//    private val firebase: FirebaseService
//) {
//    // Inicialización de ROOM usando el Contexto
//    private val db = LibreriaDatabase.getDatabase(context) // Asume que tienes un patrón Singleton para Room
//    private val libroDao = db.libroDao()
//    private val movimientoDao = db.movimientoDao()
//
//    // ... (El resto de tus funciones de repositorio)
//
//    // Ejemplo de cómo obtendrías los libros (Room)
//    suspend fun getAllBooks() = libroDao.getAll()
//
//    // Ejemplo de Firebase (Firestore)
//    suspend fun getBookById(id: String) = firebase.getBookById(id)
//
//}