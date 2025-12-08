package com.libreria.app.ui.screens.employee

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.libreria.app.vm.InventoryViewModel

/**
 * Pantalla para añadir un nuevo libro al inventario de la librería.
 *
 * Permite introducir todos los datos del nuevo libro, incluyendo el título, autor,
 * categoría, sinopsis y precio. El stock inicial se establece en 0.
 *
 * @param vm El [InventoryViewModel] para la lógica de añadir el libro.
 * @param creatorId ID del empleado que está añadiendo el libro (para el registro de movimiento).
 * @param creatorName Nombre del empleado que está añadiendo el libro.
 * @param onDone Callback que se ejecuta cuando el libro se añade con éxito (o al presionar el botón)
 * y debe volver a la pantalla anterior.
 */
@Composable
fun AddBookScreen(vm: InventoryViewModel, creatorId: String, creatorName: String, onDone: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var synopsis by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    val initialQuantity = 0 // Stock inicial por defecto
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF5F5EF)
    ) {
        Column(Modifier.fillMaxSize().padding(16.dp)) {
            Spacer(Modifier.height(22.dp))
            Text("Agregar Libro", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(19.dp))

            // Campo de texto para el Título
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth()
            )
            // Campo de texto para el Autor
            OutlinedTextField(
                value = author,
                onValueChange = { author = it },
                label = { Text("Autor") },
                modifier = Modifier.fillMaxWidth()
            )
            // Campo de texto para la Categoría
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Categoría") },
                modifier = Modifier.fillMaxWidth()
            )
            // Campo de texto para la Sinopsis
            OutlinedTextField(
                value = synopsis,
                onValueChange = { synopsis = it },
                label = { Text("Sinopsis") },
                modifier = Modifier.fillMaxWidth()
            )
            // Campo de texto para el Precio
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Precio") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            // Botón para crear el libro
            Button(onClick = {
                // Llama al ViewModel para añadir el libro y registrar el movimiento de stock inicial
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