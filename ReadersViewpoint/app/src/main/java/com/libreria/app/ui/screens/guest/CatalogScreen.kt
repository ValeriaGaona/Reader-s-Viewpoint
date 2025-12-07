
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
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import com.libreria.app.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material.icons.Icons
// ⬅️ Importa Iconos necesarios
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.text.TextStyle

@OptIn(ExperimentalMaterial3Api::class) // ⬅️ AÑADIDO
@Composable
fun CatalogScreen(
    vm: CatalogViewModel,
    onViewDetails: (String) -> Unit,
    onGoShoppingCart: () -> Unit,
    onGoBack: () -> Unit
) {
    val books by vm.books.collectAsState()
    val cart by vm.cart.collectAsState()

    val isCartEmpty = cart.isEmpty()

    val imageResId = R.drawable.imgcat

    val categories = remember(books) {
        books.map { it.category }.distinct().sorted()
    }

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Todas") }
    var expanded by remember { mutableStateOf(false) }

    val filteredBooks = remember(books, searchQuery, selectedCategory) {
        books.filter { book ->
            val matchesSearch = searchQuery.isBlank() ||
                    book.title.contains(searchQuery, ignoreCase = true) ||
                    book.author.contains(searchQuery, ignoreCase = true)

            val matchesCategory = selectedCategory == "Todas" || book.category == selectedCategory

            matchesSearch && matchesCategory
        }
    }


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF5F5EF)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(" Libros") },
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

                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = "Imagen de catálogo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Buscar libro por título o autor") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
                        OutlinedButton(
                            onClick = { expanded = true }
                        ) {
                            Text("Categoría: $selectedCategory")
                            Icon(Icons.Filled.ArrowDropDown, contentDescription = "Seleccionar categoría")
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Todas") },
                                onClick = {
                                    selectedCategory = "Todas"
                                    expanded = false
                                }
                            )
                            categories.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category) },
                                    onClick = {
                                        selectedCategory = category
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Button(
                        onClick = onGoShoppingCart,
                        enabled = !isCartEmpty,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF655D4D),
                            contentColor = Color.White,
                        ),
                    ) {
                        Text("Ver Carrito (${cart.size})")
                    }
                }

                Spacer(Modifier.height(8.dp))

                LazyColumn(Modifier.weight(1f)) {
                    items(filteredBooks) { book ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp),
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
                                Column(Modifier.clickable { onViewDetails(book.id) }) {
                                    Text(book.title, style = MaterialTheme.typography.titleMedium)
                                    Text(book.author, style = MaterialTheme.typography.bodyMedium)
                                    Text(
                                        "Categoría: ${book.category} - Stock: ${book.quantity}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    val currentQuantity = cart[book.id] ?: 0
                                    val canAddToCart = currentQuantity < book.quantity

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Button(
                                            onClick = { vm.removeFromCart(book.id) },
                                            enabled = currentQuantity > 0,
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

                                        Text(
                                            text = "$currentQuantity",
                                            style = MaterialTheme.typography.titleMedium,
                                            modifier = Modifier.width(20.dp),
                                            textAlign = TextAlign.Center
                                        )

                                        Spacer(Modifier.width(8.dp))

                                        Button(
                                            onClick = { vm.addToCart(book.id) },
                                            enabled = canAddToCart,
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
                }
            }
        }
    }
}