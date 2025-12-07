package com.libreria.app.ui.screens.guest

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.libreria.app.data.model.Libro
import com.libreria.app.vm.CatalogViewModel
@Composable
fun BookDetailsScreen(book: Libro, vm: CatalogViewModel, onBack: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF5F5EF)
    ) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text(book.title, style = MaterialTheme.typography.headlineLarge)
        Text("Autor: ${'$'}{book.author}")
        Text("Precio: $${'$'}{book.price}")
        Spacer(Modifier.height(8.dp))
        Text("Sinopsis:")
        Text(book.synopsis)
        Spacer(Modifier.height(12.dp))
        Row {
            Button(onClick = { vm.addToCart(book.id) }) { Text("Agregar al carrito") }
            Spacer(Modifier.width(8.dp))
            Button(onClick = onBack) { Text("Volver") }
        }
    }
}
}
