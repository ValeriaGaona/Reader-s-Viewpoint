package com.libreria.app.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.libreria.app.data.model.Libro
import com.libreria.app.data.model.Movimiento
import com.libreria.app.data.repository.LibreriaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.format.DateTimeFormatter

class InventoryViewModel(private val repo: LibreriaRepository) : ViewModel() {
    private val _books = MutableStateFlow<List<Libro>>(emptyList())
    val books: StateFlow<List<Libro>> = _books
    private val _movements = MutableStateFlow<List<Movimiento>>(emptyList())
    val movements: StateFlow<List<Movimiento>> = _movements

    init { refresh() }

    fun refresh() {
        viewModelScope.launch {
            _books.value = repo.getAllBooks()
            _movements.value = repo.getMovements()
        }
    }

    fun addNewBook(
        title: String, author: String, category: String, synopsis: String,
        price: Double, creatorId: String, creatorName: String, quantity: Int = 0
    ) {
        viewModelScope.launch {
            val id = System.currentTimeMillis().toString()
            val book = Libro(
                id = id,
                title = title,
                author = author,
                category = category,
                synopsis = synopsis,
                price = price,
                quantity = quantity
            )
            repo.upsertBook(book)

            val mov = Movimiento(
                id = System.currentTimeMillis().toString(),
                employeeId = creatorId,
                employeeName = creatorName,
                action = "Añadió Stock Inicial",
                bookId = book.id,
                bookTitle = book.title,
                quantity = quantity,
                dateIso = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
            )
            repo.addMovement(mov)
            refresh()
        }
    }
    fun changeStock(
        bookId: String,
        delta: Int,
        employeeId: String,
        employeeName: String,
        absolute: Boolean = false
    ) {
        viewModelScope.launch {
            val book = repo.getBookById(bookId) ?: return@launch
            val newQty = if (absolute) delta else (book.quantity + delta)
            val updated = book.copy(quantity = newQty.coerceAtLeast(0))

            repo.upsertBook(updated)

            val action = if (delta > 0) "Añadió Stock" else "Retiró Stock"
            val mov = Movimiento(
                id = System.currentTimeMillis().toString(),
                employeeId = employeeId,
                employeeName = employeeName,
                action = action,
                bookId = book.id,
                bookTitle = book.title,
                quantity = delta,
                dateIso = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
            )
            repo.addMovement(mov)
            refresh()
        }
    }
}