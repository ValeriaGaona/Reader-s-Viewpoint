package com.libreria.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

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