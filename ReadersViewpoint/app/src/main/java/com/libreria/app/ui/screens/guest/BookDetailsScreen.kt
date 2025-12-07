package com.libreria.app.ui.screens.guest

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.libreria.app.data.model.Libro
import com.libreria.app.vm.CatalogViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailsScreen(book: Libro, vm: CatalogViewModel, onBack: () -> Unit) {
    // Inicializa el formateador de moneda
    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale.getDefault()) }

    // Obtiene el estado actual del libro en el carrito
    val cart by vm.cart.collectAsState()
    val currentQuantity = cart[book.id] ?: 0
    val canAddToCart = currentQuantity < book.quantity

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF5F5EF) // Fondo claro
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(book.title, maxLines = 1) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF655D4D), // Color de la barra superior
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
                    .padding(16.dp)
            ) {

                // --- SECCIÓN DE PRECIO Y AUTOR ---
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE5E0D8)) // Fondo del card más suave
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = book.author,
                            style = MaterialTheme.typography.titleMedium.copy(color = Color(0xFF463C33))
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = currencyFormat.format(book.price),
                            style = MaterialTheme.typography.headlineLarge.copy(
                                color = Color(0xFF655D4D),
                                fontWeight = FontWeight.ExtraBold
                            )
                        )
                    }
                }

                // --- SECCIÓN DE DATOS Y SINOPSIS ---
                Text("Detalles del Libro", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 8.dp))

                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Categoría: ${book.category}", style = MaterialTheme.typography.bodyLarge)
                        Text("Stock Disponible: ${book.quantity}", style = MaterialTheme.typography.bodyLarge)

                        Divider(Modifier.padding(vertical = 12.dp))

                        Text("Sinopsis:", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(4.dp))
                        // Scrollable para sinopsis largas
                        Text(
                            book.synopsis,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.fillMaxWidth()
                                .heightIn(max = 200.dp)
                                .weight(1f, fill = false) // Permite que ocupe espacio sin empujar el resto
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // --- SECCIÓN DE ACCIONES (CARRITO Y VOLVER) ---
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Botón para volver
                    OutlinedButton(
                        onClick = onBack,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF655D4D)
                        )
                    ) {
                        Text("Volver al Catálogo")
                    }

                    // Control de Cantidad para el Carrito
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Button(
                            onClick = { vm.removeFromCart(book.id) },
                            enabled = currentQuantity > 0,
                            modifier = Modifier.size(40.dp),
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF655D4D))
                        ) {
                            Text("-", style = MaterialTheme.typography.titleMedium)
                        }

                        Spacer(Modifier.width(12.dp))

                        Text(
                            text = "$currentQuantity",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.width(24.dp),
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.width(12.dp))

                        Button(
                            onClick = { vm.addToCart(book.id) },
                            enabled = canAddToCart,
                            modifier = Modifier.size(40.dp),
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF655D4D))
                        ) {
                            Text("+", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
            }
        }
    }
}