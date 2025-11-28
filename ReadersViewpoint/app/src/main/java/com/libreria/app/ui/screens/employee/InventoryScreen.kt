package com.libreria.app.ui.screens.employee

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.libreria.app.R
import com.libreria.app.data.model.Libro
import com.libreria.app.vm.InventoryViewModel

@Composable
fun InventoryScreen(
    vm: InventoryViewModel,
    onAddBook: () -> Unit,
    currentEmployeeId: String,
    currentEmployeeName: String // <-- Parámetro completo y cierre de paréntesis
) { // <-- Abre el cuerpo de la función aquí

    // Variables movidas dentro del cuerpo de la función
    val books by vm.books.collectAsState()
    val movements by vm.movements.collectAsState()

    Image(
        painter = painterResource(id = R.drawable.imginv),
        contentDescription = "_",
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp), // Ajusta la altura según necesites
        contentScale = ContentScale.Crop // Recorta la imagen para llenar el espacio
    )
    Spacer(Modifier.height(12.dp))
    Column(Modifier.fillMaxSize().padding(12.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Inventario", style = MaterialTheme.typography.headlineMedium)
            Button(onClick = onAddBook) { Text("Agregar libro") }
        }

        Spacer(Modifier.height(8.dp))

        // Search by ID
        var searchId by remember { mutableStateOf("") }
        Row {
            OutlinedTextField(value = searchId, onValueChange = { searchId = it }, label = { Text("Buscar por ID") })
            Spacer(Modifier.width(8.dp))
            Button(onClick = {
                if (searchId.isNotBlank()) {
                    // open change stock UI
                }
            }) { Text("Buscar") }
        }

        Spacer(Modifier.height(8.dp))

        LazyColumn {
            items(books) { book ->
                Card(Modifier.fillMaxWidth().padding(6.dp)) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.clickable {
                            // Open dialog to add/remove stock
                        }) {
                            Text(book.title)
                            Text("ID: ${book.id} - Stock: ${book.quantity}")
                        }
                        Column {
                            // CORRECCIÓN 3.1: Llama a changeStock y cierra el paréntesis
                            Button(onClick = {
                                vm.changeStock(book.id, 1, currentEmployeeId, currentEmployeeName)
                            }) {
                                Text("Stock +1") // Texto ajustado para claridad
                            }

                            Spacer(Modifier.height(4.dp))

                            // CORRECCIÓN 3.2: Llama a changeStock y cierra el paréntesis
                            Button(onClick = {
                                vm.changeStock(book.id, -1, currentEmployeeId, currentEmployeeName)
                            }) {
                                Text("Stock -1") // Texto ajustado para claridad
                            }
                        }
                    }
                }
            }
        }
    } // Cierra el Column
} // <-- Cierra el Composable InventoryScreen