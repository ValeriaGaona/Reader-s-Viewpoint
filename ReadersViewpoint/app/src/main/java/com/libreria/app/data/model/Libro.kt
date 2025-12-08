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

