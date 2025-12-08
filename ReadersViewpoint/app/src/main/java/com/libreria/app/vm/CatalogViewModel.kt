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

/**
 * ViewModel responsable de la lógica del catálogo de libros, el carrito de compras,
 * y el procesamiento de ventas (checkout).
 *
 * @param repo El repositorio [LibreriaRepository] utilizado para acceder a los datos.
 */
class CatalogViewModel(private val repo: LibreriaRepository) : ViewModel() {

    /**
     * Flujo de estado interno que almacena la lista completa de libros del catálogo ([Libro]).
     */
    private val _books = MutableStateFlow<List<Libro>>(emptyList())
    /**
     * Flujo de estado público que expone la lista de libros al UI.
     */
    val books: StateFlow<List<Libro>> = _books

    /**
     * Flujo de estado interno que almacena el carrito de compras como un mapa de BookID a Cantidad.
     */
    private val _cart = MutableStateFlow(mutableMapOf<String, Int>())
    /**
     * Flujo de estado público que expone el contenido raw del carrito de compras.
     */
    val cart: StateFlow<Map<String, Int>> = _cart

    /**
     * Flujo de estado interno que almacena la lista de tickets de ventas ([Ticket]).
     */
    private val _tickets = MutableStateFlow<List<Ticket>>(emptyList())
    /**
     * Flujo de estado público que expone la lista de tickets de ventas.
     */
    val tickets: StateFlow<List<Ticket>> = _tickets

    init {
        refreshBooks()
        viewModelScope.launch {
            try {
                // Intenta cargar los tickets al inicio
                _tickets.value = repo.fetchTickets()
            } catch (e: Exception) {
                println("Error cargando tickets: ${e.message}")
            }
        }
    }

    /**
     * Combina el carrito raw ([_cart]) con los datos de los libros ([_books])
     * para proporcionar una vista detallada del carrito (Libro y Cantidad).
     *
     * Es un [StateFlow] que se mantiene activo mientras se suscribe (5 segundos).
     */
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

    /**
     * Recarga la lista de libros desde el repositorio.
     */
    fun refreshBooks() {
        viewModelScope.launch {
            _books.value = repo.getAllBooks()
        }
    }

    /**
     * Incrementa la cantidad de un libro específico en el carrito.
     *
     * @param bookId El ID del libro a añadir o incrementar.
     */
    fun addToCart(bookId: String) {
        val map = _cart.value.toMutableMap()
        map[bookId] = (map[bookId] ?: 0) + 1
        _cart.value = map
    }

    /**
     * Decrementa la cantidad de un libro específico en el carrito.
     * Si la cantidad es 1, el libro se elimina del carrito.
     *
     * @param bookId El ID del libro a decrementar o eliminar.
     */
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

    /**
     * Procesa la transacción de compra (checkout).
     *
     * Esta función realiza las siguientes tareas críticas de negocio en orden:
     * 1. Genera un ID de ticket único.
     * 2. Itera sobre el carrito para:
     * a. Calcular el total.
     * b. Crear [TicketItem]s.
     * c. Actualizar el stock del libro en el repositorio.
     * d. Registrar un [Movimiento] de "Vendió" para cada artículo.
     * 3. Crea y añade el [Ticket] final al repositorio.
     * 4. Limpia el carrito.
     * 5. Recarga la lista de libros.
     *
     * @param employeeId ID del empleado o "Caja" si es una venta anónima.
     * @param employeeName Nombre del empleado o "Cliente" si es una venta anónima.
     * @return El ID del ticket de venta generado.
     */
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

            // Añade el nuevo ticket al caché local
            _tickets.value = listOf(newTicket) + _tickets.value

            // Limpia el carrito y actualiza el inventario
            _cart.value = mutableMapOf()
            refreshBooks()

            return@withContext ticketId
        }
    }

    /**
     * Obtiene los detalles de un ticket de venta específico.
     *
     * Primero busca en la caché de [_tickets]. Si no se encuentra, la implementación
     * actual emite `null` (aunque podría extenderse para buscar en el repositorio remoto).
     *
     * @param ticketId El ID del ticket a buscar.
     * @return Un [Flow] que emite el [Ticket] encontrado o `null`.
     */
    fun getTicketDetails(ticketId: String): Flow<Ticket?> = flow {
        val cachedTicket = _tickets.value.find { it.id == ticketId }

        if (cachedTicket != null) {
            emit(cachedTicket)
        } else {
            // En una aplicación real, se haría una llamada a repo.getTicketDetails(ticketId) aquí
            emit(null)
        }
    }
    /**
     * Obtiene un libro específico por su ID.
     *
     * @param id El ID del libro a buscar.
     * @return Un [Flow] que emite el [Libro] si se encuentra, o `null`.
     */
    fun getBookById(id: String): Flow<Libro?> {
        return books.map { list -> list.find { it.id == id } }
    }
}