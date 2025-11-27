//package com.libreria.app.data.model
//
//import androidx.room.Entity
//import androidx.room.PrimaryKey
//@Entity(tableName = "libros")
//data class Libro(
//    @PrimaryKey val id: String,
//    val title: String,
//    val author: String,
//    val category: String = "Sin categoría",
//    val synopsis: String = "",
//    var quantity: Int = 0,
//    val price: Double = 0.0
//)
//data class Movimiento(
//    val id: String = "",
//    val employeeId: String = "",
//    val employeeName: String = "",
//    val action: String = "",
//    val bookId: String = "",
//    val bookTitle: String = "",
//    val quantity: Int = 0,
//    val dateIso: String = "" // ISO timestamp
//)
//data class UserProfile(
//    val uid: String = "",
//    val email: String = "",
//    val displayName: String = "",
//    val role: String = "employee" // or "admin"
//)

package com.libreria.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representa un libro en el inventario.
 * Es una entidad tanto para Room (base de datos local) como para Firebase.
 */
@Entity(tableName = "libros")
data class Libro(
    @PrimaryKey val id: String = "",
    val title: String = "",
    val author: String = "",
    val category: String = "Sin categoría",
    val synopsis: String = "",
    var quantity: Int = 0,
    val price: Double = 0.0
)

/**
 * Representa un movimiento de inventario (añadir/retirar stock).
 * Se guarda en Room y Firebase ('movimientos').
 */
@Entity(tableName = "movimientos")
data class Movimiento(
    @PrimaryKey val id: String = System.currentTimeMillis().toString(),
    val employeeId: String = "",
    val employeeName: String = "",
    val action: String = "", // e.g., "Añadió Stock", "Retiró Stock"
    val bookId: String = "",
    val bookTitle: String = "",
    val quantity: Int = 0, // Cantidad del cambio (delta)
    val dateIso: String = "" // ISO timestamp para ordenamiento
)

/**
 * Representa el perfil de un usuario (empleado o administrador).
 * Se guarda en Firebase Firestore ('users').
 */
data class UserProfile(
    val uid: String = "",
    val email: String = "",
    val displayName: String = "",
    val role: String = "employee" // or "admin"
)