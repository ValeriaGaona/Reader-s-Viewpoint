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
class CatalogViewModel(private val repo: LibreriaRepository) : ViewModel() {
    private val _books = MutableStateFlow<List<Libro>>(emptyList())
    val books: StateFlow<List<Libro>> = _books
    private val _cart = MutableStateFlow(mutableMapOf<String, Int>()) // bookId -> qty
    val cart: StateFlow<Map<String, Int>> = _cart
    init { refreshBooks() }
    fun refreshBooks() {
        viewModelScope.launch {
            _books.value = repo.getAllBooks()
        }
    }
    fun addToCart(bookId: String) {
        val map = _cart.value.toMutableMap()
        map[bookId] = (map[bookId] ?: 0) + 1
        _cart.value = map
    }
    fun removeFromCart(bookId: String) {
        val map = _cart.value.toMutableMap()
        map.remove(bookId)
        _cart.value = map
    }
    fun checkout(employeeId: String?, employeeName: String?) {
        // Build ticket and create movimientos, reduce stock
        viewModelScope.launch {
            val currentBooks = _books.value.associateBy { it.id }.toMutableMap()
            val cartCopy = _cart.value.toMap()
            var total = 0.0
            cartCopy.forEach { (bookId, qty) ->
                val book = currentBooks[bookId] ?: return@forEach
                total += book.price * qty
                // update stock
                val updated = book.copy(quantity = (book.quantity - qty).coerceAtLeast(0))
                repo.upsertBook(updated)
                // add movimiento of sale
                val mov = Movimiento(
                    id = System.currentTimeMillis().toString(),
                    employeeId = employeeId ?: "Caja",
                    employeeName = employeeName ?: "Caja",
                    action = "Vendió",
                    bookId = book.id,
                    bookTitle = book.title,
                    quantity = qty,
                    dateIso = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
                )
                repo.addMovement(mov)
            }
            // clear cart
            _cart.value = mutableMapOf()
            // refresh books
            refreshBooks()
        }
    }
}
