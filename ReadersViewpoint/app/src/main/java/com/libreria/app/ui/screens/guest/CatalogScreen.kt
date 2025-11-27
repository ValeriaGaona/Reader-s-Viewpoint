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
fun CatalogScreen(vm: CatalogViewModel, onViewDetails: (String) -> Unit, onGoInventory: () -> Unit, onGoAdmin: () -> Un val books by vm.books.collectAsState()
val cart by vm.cart.collectAsState()
Column(Modifier.fillMaxSize().padding(12.dp)) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text("Catálogo", style = MaterialTheme.typography.headlineMedium)
        if (!isGuest) {
            Row {
                Button(onClick = onGoInventory) { Text("Inventario") }
                Spacer(Modifier.width(8.dp))
                Button(onClick = onGoAdmin) { Text("Admin") }
            }
        }
    }
    Spacer(Modifier.height(8.dp))
    LazyColumn(Modifier.fillMaxSize()) {
        items(books) { book ->
            Card(Modifier.fillMaxWidth().padding(6.dp)) {
                Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(Modifier.clickable { onViewDetails(book.id) } ) {
                        Text(book.title)
                        Text(book.author)
                        Text("Categoria: ${'$'}{book.category} - Stock: ${'$'}{book.quantity}")
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
