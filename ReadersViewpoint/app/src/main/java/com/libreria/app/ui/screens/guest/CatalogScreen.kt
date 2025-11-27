package com.libreria.app.ui.screens.guest

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.libreria.app.vm.CatalogViewModel

@Composable
fun CatalogScreen(
    vm: CatalogViewModel,
    onViewDetails: (String) -> Unit,
    onGoInventory: () -> Unit,
    onGoEmployees: () -> Unit, // 👈 NUEVO HANDLER PARA EMPLEADOS
    onGoMovements: () -> Unit, // 👈 NUEVO HANDLER PARA MOVIMIENTOS
    onCreateAccount: () -> Unit,
    isGuest: Boolean
) {
    val books by vm.books.collectAsState()
    val cart by vm.cart.collectAsState()

    Column(Modifier.fillMaxSize().padding(12.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Catálogo", style = MaterialTheme.typography.headlineMedium)

            if (!isGuest) {
                Column { // Contenedor vertical para las dos filas
                    Row { // Primera fila
                        Button(onClick = onGoInventory) { Text("Inventario") }
                        Spacer(Modifier.width(8.dp))
                        Button(onClick = onGoEmployees) { Text("Empleados") }
                    }
                    Spacer(Modifier.height(8.dp)) // Espacio entre filas
                    Row { // Segunda fila
                        Button(onClick = onCreateAccount) { Text("Crear usuario") }
                        Spacer(Modifier.width(8.dp))
                        Button(onClick = onGoMovements) { Text("Movimientos") }
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        LazyColumn(Modifier.fillMaxSize()) {
            items(books) { book ->
                Card(Modifier.fillMaxWidth().padding(6.dp)) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(Modifier.clickable { onViewDetails(book.id) }) {
                            Text(book.title, style = MaterialTheme.typography.titleMedium)
                            Text(book.author, style = MaterialTheme.typography.bodyMedium)
                            Text("Categoría: ${book.category} - Stock: ${book.quantity}", style = MaterialTheme.typography.bodySmall)
                        }

                        Column {
                            Button(onClick = { vm.addToCart(book.id) }) { Text("Agregar (${cart[book.id] ?: 0})") }
                        }
                    }
                }
            }
        }
    }
}