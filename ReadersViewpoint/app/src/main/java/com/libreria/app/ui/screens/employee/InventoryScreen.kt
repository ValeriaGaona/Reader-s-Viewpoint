package com.libreria.app.ui.screens.employee

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.libreria.app.data.model.Libro
import com.libreria.app.vm.InventoryViewModel
@Composable
fun InventoryScreen(vm: InventoryViewModel, onAddBook: () -> Unit, currentEmployeeId: String, currentEmployeeName: Stri val books by vm.books.collectAsState()
val movements by vm.movements.collectAsState()
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
                Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(modifier = Modifier.clickable {
                        // Open dialog to add/remove stock
                    }) {
                        Text(book.title)
                        Text("ID: ${'$'}{book.id} - Stock: ${'$'}{book.quantity}")
                    }
                    Column {
                        Button(onClick = { /* quick +1 */ vm.changeStock(book.id, 1, currentEmployeeId, currentEmpl Spacer(Modifier.height(4.dp))
                                Button(onClick = { /* quick -1 */ vm.changeStock(book.id, -1, currentEmployeeId, currentEmp }
                        }
                    }
                }
            }
        }
    }
