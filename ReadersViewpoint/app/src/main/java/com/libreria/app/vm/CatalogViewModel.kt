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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import com.libreria.app.data.model.Ticket
import com.libreria.app.data.model.TicketItem
import kotlinx.coroutines.flow.map

class CatalogViewModel(private val repo: LibreriaRepository) : ViewModel() {

    private val _books = MutableStateFlow<List<Libro>>(emptyList())
    val books: StateFlow<List<Libro>> = _books

    private val _cart = MutableStateFlow(mutableMapOf<String, Int>())
    val cart: StateFlow<Map<String, Int>> = _cart

    private val _tickets = MutableStateFlow<List<Ticket>>(emptyList())
    val tickets: StateFlow<List<Ticket>> = _tickets

    init {
        refreshBooks()
        viewModelScope.launch {
            try {
                _tickets.value = repo.fetchTickets()
            } catch (e: Exception) {
                println("Error cargando tickets: ${e.message}")
            }
        }
    }

    val cartDetails: StateFlow<Map<Libro, Int>> = combine(_cart, _books) { cartMap, booksList ->
        val bookMap = booksList.associateBy { it.id }
        cartMap
            .filter { it.value > 0 }
            .mapNotNull { (bookId, quantity) ->
                bookMap[bookId]?.let { book ->
                    book to quantity
                }
            }
            .toMap()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyMap()
    )


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
        val currentQty = map[bookId] ?: 0

        if (currentQty > 1) {
            map[bookId] = currentQty - 1
        } else {
            map.remove(bookId)
        }
        _cart.value = map
    }

    suspend fun processCheckout(employeeId: String? = "Caja", employeeName: String? = "Cliente") : String {
        return withContext(Dispatchers.IO) {
            val ticketId = System.currentTimeMillis().toString()
            val cartCopy = _cart.value.toMap()
            val currentBooks = _books.value.associateBy { it.id }.toMutableMap()
            val ticketItems = mutableListOf<TicketItem>()
            var total = 0.0

            cartCopy.forEach { (bookId, qty) ->
                val book = currentBooks[bookId] ?: return@forEach

                val itemPrice = book.price * qty
                total += itemPrice
                ticketItems.add(TicketItem(book.title, qty, itemPrice))

                val updated = book.copy(quantity = (book.quantity - qty).coerceAtLeast(0))
                repo.upsertBook(updated)

                val mov = Movimiento(
                    id = System.currentTimeMillis().toString() + bookId, // ID único
                    employeeId = employeeId ?: "Caja",
                    employeeName = employeeName ?: "Cliente",
                    action = "Vendió",
                    bookId = book.id,
                    bookTitle = book.title,
                    quantity = qty,
                    dateIso = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
                )
                repo.addMovement(mov)
            }

            val newTicket = Ticket(
                id = ticketId,
                date = DateTimeFormatter.ISO_INSTANT.format(Instant.now()),
                total = total,
                items = ticketItems
            )

            repo.addTicket(newTicket)

            _tickets.value = listOf(newTicket) + _tickets.value

            _cart.value = mutableMapOf()
            refreshBooks()

            return@withContext ticketId
        }
    }

    fun getTicketDetails(ticketId: String): Flow<Ticket?> = flow {
        val cachedTicket = _tickets.value.find { it.id == ticketId }

        if (cachedTicket != null) {
            emit(cachedTicket)
        } else {

            emit(null)
        }
    }
    fun getBookById(id: String): Flow<Libro?> {
        return books.map { list -> list.find { it.id == id } }
    }
}