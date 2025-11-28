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
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.compose.ui.graphics.Color // Para definir colores específicos
import androidx.compose.foundation.Image // Para mostrar la imagen
import androidx.compose.ui.res.painterResource // Para cargar la imagen desde res/drawable
import androidx.compose.ui.layout.ContentScale // Para escalar la imagen
import com.libreria.app.R // Asegúrate de que esta es la ruta a tus recursos drawable
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.StateFlow


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
        Image(
            painter = painterResource(id = R.drawable.imgcat),
            contentDescription = "_",
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp), // Ajusta la altura según necesites
            contentScale = ContentScale.Crop // Recorta la imagen para llenar el espacio
        )
        Spacer(Modifier.height(12.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {


            if (!isGuest) {
                Column { // Contenedor vertical para las dos filas
                    Row { // Primera fila

                        Button(
                            onClick = { onGoInventory() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF655D4D), // Rojo
                                contentColor = Color.White
                            )
                        ) {
                            Text("Inventario")
                        }
                        Spacer(Modifier.width(8.dp))
                        Button(
                            onClick = { onGoEmployees() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF655D4D), // Rojo
                                contentColor = Color.White
                            )
                        ) {
                            Text("Empleados")
                        }
                    }
                    Spacer(Modifier.height(8.dp)) // Espacio entre filas
                    Row { // Segunda fila
                        Button(
                            onClick = onCreateAccount,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF655D4D), // Verde
                                contentColor = Color.White
                            )
                        )  {
                            Text("Crear usuario")
                        }
                        Spacer(Modifier.width(8.dp))
                        Button(
                            onClick = onGoMovements,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF655D4D), // Azul
                                contentColor = Color.White
                            )
                        ) {
                            Text("Movimientos")
                        }
                    }
                }
            }
        }


        Spacer(Modifier.height(8.dp))

        LazyColumn(Modifier.fillMaxSize()) {
            items(books) { book ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    // 👇 COLORES DE LA TARJETA DEFINIDOS AQUÍ
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFCC80), // Color de fondo de la Card (Naranja claro)
                        contentColor = Color.Black          // Color del texto dentro de la Card
                    )
                ) {
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
