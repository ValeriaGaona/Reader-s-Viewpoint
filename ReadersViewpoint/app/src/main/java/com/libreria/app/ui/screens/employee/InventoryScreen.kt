package com.libreria.app.ui.screens.employee

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.libreria.app.R
import com.libreria.app.data.model.Libro
import com.libreria.app.vm.InventoryViewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.automirrored.filled.ArrowBack

/**
 * Clase de datos que encapsula la acción de stock a confirmar.
 *
 * @param bookId ID del libro afectado.
 * @param bookTitle Título del libro afectado.
 * @param quantity Cantidad a añadir (delta).
 */
data class StockAction(
    val bookId: String,
    val bookTitle: String,
    val quantity: Int
)

/**
 * Pantalla para la gestión y manipulación del inventario de libros.
 *
 * Permite buscar libros, ver su stock actual, añadir nuevas unidades y
 * navegar a la pantalla de agregar nuevos libros.
 *
 * @param vm El [InventoryViewModel] para la lógica de inventario.
 * @param onAddBook Callback para navegar a la pantalla de agregar libro.
 * @param currentEmployeeId ID del empleado actualmente autenticado (para registrar movimientos).
 * @param currentEmployeeName Nombre del empleado actualmente autenticado.
 * @param onGoBack Callback para regresar a la pantalla anterior (Dashboard).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    vm: InventoryViewModel,
    onAddBook: () -> Unit,
    currentEmployeeId: String,
    currentEmployeeName: String,
    onGoBack: () -> Unit
) {
    val books by vm.books.collectAsState()

    var searchId by remember { mutableStateOf("") }

    var showConfirmationDialog by remember { mutableStateOf(false) }
    var actionToConfirm by remember { mutableStateOf<StockAction?>(null) }

    // Filtra la lista de libros basada en el texto de búsqueda (ID o Título)
    val filteredBooks = remember(books, searchId) {
        if (searchId.isBlank()) {
            books
        } else {
            books.filter { libro ->
                libro.id.contains(searchId, ignoreCase = true) ||
                        libro.title.contains(searchId, ignoreCase = true)
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF5F5EF)
    ) {Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Inventario") },
                navigationIcon = {
                    IconButton(onClick = onGoBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF655D4D),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFF5F5EF)
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 12.dp)
        ) {

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(" ", style = MaterialTheme.typography.headlineMedium)

                // Botón para agregar un nuevo libro al sistema
                Button(
                    onClick = { onAddBook() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF655D4D),
                        contentColor = Color.White
                    )
                ) {
                    Text("Agregar libro")
                }
            }

            Spacer(Modifier.height(16.dp))

            // Campo de búsqueda
            OutlinedTextField(
                value = searchId,
                onValueChange = { searchId = it },
                label = { Text("Buscar libro por ID o Título") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // Lista de libros filtrados
            LazyColumn {
                items(filteredBooks, key = { it.id }) { book ->
                    var quantityInput by remember(book.id) { mutableStateOf("1") }
                    val quantityToAdd = quantityInput.toIntOrNull() ?: 0
                    val isQuantityValid = quantityToAdd > 0

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xC87A6F5F),
                            contentColor = Color.Black
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Información del libro
                            Column(modifier = Modifier.weight(1f).clickable { /* ... */ }) {
                                Text(book.title, style = MaterialTheme.typography.titleMedium)
                                Text(
                                    "ID: ${book.id} - Stock: ${book.quantity}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            Spacer(Modifier.width(16.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {

                                // Campo para ingresar la cantidad a añadir
                                OutlinedTextField(
                                    value = quantityInput,
                                    onValueChange = { newValue ->
                                        if (newValue.all { it.isDigit() } || newValue.isBlank()) {
                                            quantityInput = newValue
                                        }
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number
                                    ),
                                    label = { Text("Cant.") },
                                    singleLine = true,
                                    modifier = Modifier.width(80.dp)
                                )

                                Spacer(Modifier.width(8.dp))

                                // Botón de añadir stock
                                Button(
                                    onClick = {
                                        actionToConfirm = StockAction(
                                            bookId = book.id,
                                            bookTitle = book.title,
                                            quantity = quantityToAdd
                                        )
                                        showConfirmationDialog = true
                                    },
                                    enabled = isQuantityValid,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF655D4D),
                                        contentColor = Color.White
                                    ),
                                    modifier = Modifier.height(56.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Add,
                                        contentDescription = "Añadir $quantityToAdd al stock"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    }

    // Diálogo de confirmación
    if (showConfirmationDialog && actionToConfirm != null) {
        ConfirmationDialog(
            action = actionToConfirm!!,
            onDismiss = {
                showConfirmationDialog = false
                actionToConfirm = null
            },
            onConfirm = { action ->
                // Llama al ViewModel para cambiar el stock y registrar el movimiento
                vm.changeStock(
                    action.bookId,
                    action.quantity,
                    currentEmployeeId,
                    currentEmployeeName
                )
                showConfirmationDialog = false
                actionToConfirm = null
            }
        )
    }
}

/**
 * Diálogo de confirmación para añadir stock.
 *
 * @param action La acción de stock ([StockAction]) a confirmar.
 * @param onDismiss Callback para descartar el diálogo.
 * @param onConfirm Callback que se ejecuta al confirmar la acción.
 */
@Composable
fun ConfirmationDialog(
    action: StockAction,
    onDismiss: () -> Unit,
    onConfirm: (StockAction) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirmar Adición de Stock") },
        text = {
            Text(
                "¿Está seguro que desea añadir ${action.quantity} unidades al libro:\n" +
                        "\"${action.bookTitle}\" (ID: ${action.bookId})?"
            )
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(action) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF655D4D),
                    contentColor = Color.White
                )
            ) {
                Text("Confirmar (+${action.quantity})")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss
            ) {
                Text("Cancelar")
            }
        }
    )
}