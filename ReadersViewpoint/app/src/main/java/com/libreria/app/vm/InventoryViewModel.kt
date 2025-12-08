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

/**
 * ViewModel responsable de la lógica de negocio para la gestión del inventario.
 *
 * Se encarga de la carga de la lista de libros, la manipulación del stock y el
 * registro de movimientos de inventario.
 *
 * @param repo El repositorio [LibreriaRepository] utilizado para acceder a los datos.
 */
class InventoryViewModel(private val repo: LibreriaRepository) : ViewModel() {
    /**
     * Flujo mutable interno que almacena la lista completa de libros ([Libro]).
     */
    private val _books = MutableStateFlow<List<Libro>>(emptyList())
    /**
     * Flujo de estado público que expone la lista de libros al UI.
     */
    val books: StateFlow<List<Libro>> = _books
    /**
     * Flujo mutable interno que almacena el historial de movimientos de inventario ([Movimiento]).
     */
    private val _movements = MutableStateFlow<List<Movimiento>>(emptyList())
    /**
     * Flujo de estado público que expone el historial de movimientos.
     */
    val movements: StateFlow<List<Movimiento>> = _movements

    init { refresh() }

    /**
     * Carga y actualiza las listas de todos los libros y todos los movimientos desde el repositorio.
     *
     * Se ejecuta dentro de un [viewModelScope] para ser asíncrono y ligado al ciclo de vida.
     */
    fun refresh() {
        viewModelScope.launch {
            _books.value = repo.getAllBooks()
            _movements.value = repo.getMovements()
        }
    }

    /**
     * Añade un nuevo libro al inventario y registra el movimiento de stock inicial.
     *
     * @param title Título del nuevo libro.
     * @param author Autor del nuevo libro.
     * @param category Categoría del nuevo libro.
     * @param synopsis Sinopsis del nuevo libro.
     * @param price Precio de venta unitario.
     * @param creatorId ID del empleado que realiza la acción.
     * @param creatorName Nombre del empleado que realiza la acción.
     * @param quantity Cantidad inicial de stock (por defecto 0).
     */
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
    /**
     * Modifica la cantidad de stock de un libro existente y registra el movimiento.
     *
     * @param bookId ID del libro cuyo stock será modificado.
     * @param delta Cantidad a añadir (positivo) o retirar (negativo).
     * @param employeeId ID del empleado que realiza la acción.
     * @param employeeName Nombre del empleado que realiza la acción.
     * @param absolute Si es `true`, `delta` se trata como la cantidad final absoluta (no un cambio).
     */
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
            // Asegura que la cantidad nunca sea menor que cero.
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