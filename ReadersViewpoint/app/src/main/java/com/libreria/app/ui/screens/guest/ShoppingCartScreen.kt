package com.libreria.app.ui.screens.guest

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.libreria.app.vm.CatalogViewModel
import com.libreria.app.data.model.Libro
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.launch
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingCartScreen(
    vm: CatalogViewModel,
    onClose: () -> Unit,
    onCheckoutSuccess: (String) -> Unit,
    onGoBack: () -> Unit,
    onGoCheckout: (ticketId: String) -> Unit

) {
    val scope = rememberCoroutineScope()

    val cart by vm.cart.collectAsState()
    val cartDetails by vm.cartDetails.collectAsState()

    val total = cartDetails.entries.sumOf { (book, quantity) ->
        book.price * quantity
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF5F5EF)
    ) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Carrito de Compras") },
                navigationIcon = {
                    IconButton(onClick = onGoBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF655D4D),
                    titleContentColor = Color.Black,
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

            Text("Tu Pedido:", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))

            if (cart.isEmpty()) {
                Text("El carrito está vacío.", style = MaterialTheme.typography.bodyLarge)
            } else {
                LazyColumn(Modifier.weight(1f)) {
                    items(cartDetails.entries.toList()) { (book, quantity) ->
                        CartItem(book = book, quantity = quantity, vm = vm)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total a pagar:", style = MaterialTheme.typography.headlineSmall)
                Text(
                    "$$$total",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    scope.launch {
                        val ticketId = vm.processCheckout()
                        onCheckoutSuccess(ticketId)
                    }
                },
                enabled = cart.isNotEmpty(),
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF655D4D),
                    contentColor = Color.White
                )
            ) {
                Text("Finalizar Compra y Generar Ticket")
            }
        }
    }
}
}

@Composable
fun CartItem(book: Libro, quantity: Int, vm: CatalogViewModel) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF5F5EF) // Color personalizado
    ) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xC87A6F5F),
            contentColor = Color.Black
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(book.title, style = MaterialTheme.typography.titleMedium)
                Text("Precio unitario: $$${book.price}", style = MaterialTheme.typography.bodySmall)
                Text(
                    "Cantidad: $quantity",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Row {
                Button(
                    onClick = { vm.removeFromCart(book.id) },
                    enabled = quantity > 0,
                    modifier = Modifier.size(36.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF655D4D),
                        contentColor = Color.White
                    )
                ) {
                    Text("-")
                }
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = { vm.addToCart(book.id) },
                    modifier = Modifier.size(36.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF655D4D),
                        contentColor = Color.White
                    )
                ) {
                    Text("+")
                }
            }
        }
    }
}
}