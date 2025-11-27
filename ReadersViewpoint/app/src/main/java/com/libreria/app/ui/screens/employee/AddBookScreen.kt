package com.libreria.app.ui.screens.employee

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.libreria.app.vm.InventoryViewModel
@Composable
fun AddBookScreen(vm: InventoryViewModel, creatorId: String, creatorName: String, onDone: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var synopsis by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Agregar Libro", style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Título") })
        OutlinedTextField(value = author, onValueChange = { author = it }, label = { Text("Autor") })
        OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Categoría") })
        OutlinedTextField(value = synopsis, onValueChange = { synopsis = it }, label = { Text("Sinopsis") })
        OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Precio") })
        Spacer(Modifier.height(12.dp))
        Button(onClick = {
            vm.addNewBook(title = title, author = author, category = category, synopsis = synopsis, price = price.toDou onDone()
        }) { Text("Crear libro (stock 0)") }
    }
}
