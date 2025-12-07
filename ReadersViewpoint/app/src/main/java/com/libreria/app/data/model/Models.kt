package com.libreria.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


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

@Entity(tableName = "movimientos")
data class Movimiento(
    @PrimaryKey val id: String = System.currentTimeMillis().toString(),
    val employeeId: String = "",
    val employeeName: String = "",
    val action: String = "",
    val bookId: String = "",
    val bookTitle: String = "",
    val quantity: Int = 0,
    val dateIso: String = ""
)

data class UserProfile(
    val uid: String = "",
    val email: String = "",
    val displayName: String = "",
    val role: String = "employee"
)



data class TicketItem(
    val title: String = "",
    val quantity: Int = 0,
    val price: Double = 0.0
)

data class Ticket(
    val id: String = "",
    val date: String = "",
    val total: Double = 0.0,
    val items: List<TicketItem> = emptyList()
)