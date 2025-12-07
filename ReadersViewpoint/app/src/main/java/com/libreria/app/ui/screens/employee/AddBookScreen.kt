package com.libreria.app.ui.screens.employee

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.libreria.app.vm.InventoryViewModel

@Composable
fun AddBookScreen(vm: InventoryViewModel, creatorId: String, creatorName: String, onDone: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var synopsis by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    val initialQuantity = 0
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF5F5EF)
    ) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Spacer(Modifier.height(22.dp))
        Text("Agregar Libro", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(19.dp))
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = author,
            onValueChange = { author = it },
            label = { Text("Autor") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Categoría") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = synopsis,
            onValueChange = { synopsis = it },
            label = { Text("Sinopsis") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Precio") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        Button(onClick = {
            vm.addNewBook(
                title = title,
                author = author,
                category = category,
                synopsis = synopsis,
                price = price.toDouble(),
                creatorId = creatorId,
                creatorName = creatorName,
                quantity = initialQuantity,

            )
            onDone()
        }, modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF655D4D),
                contentColor = Color.White
            )
        ) {
            Text("Crear libro (stock $initialQuantity)")
        }
    }
}
}