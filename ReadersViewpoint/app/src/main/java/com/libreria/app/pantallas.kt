package com.libreria.app

// Proyecto Kotlin/Android Jetpack Compose
// Archivo principal: LibreriaApp.kt
// Incluye pantallas: Login, Catalogo, Inventario, Jefe, Carrito
// Manejo de estado, Cards, Buttons, Rows, Columns.


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

// ---------------- MODELOS -------------------
data class Libro(
    val id: Int,
    val titulo: String,
    val autor: String,
    val categoria: String,
    val precio: Double,
    var stock: Int
)

data class Movimiento(
    val fecha: String,
    val empleadoId: String,
    val libroId: Int,
    val tipo: String,
    val cantidad: Int
)

// ---------------- ACTIVIDAD PRINCIPAL -------------------
class LibreriaApp : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                App()
            }
        }
    }
}

// ---------------- ESTADO GLOBAL -------------------
@Composable
fun App() {
    var screen by remember { mutableStateOf("login") }
    var usuario by remember { mutableStateOf("") }

    val libros = remember {
        mutableStateListOf(
            Libro(1, "El Quijote", "Cervantes", "Novela", 120.0, 5),
            Libro(2, "1984", "George Orwell", "Distopía", 200.0, 3)
        )
    }

    val carrito = remember { mutableStateListOf<Libro>() }
    val movimientos = remember { mutableStateListOf<Movimiento>() }

    when (screen) {
        "login" -> LoginScreen(
            onLogin = {
                usuario = it
                screen = "catalogo"
            }
        )
        "catalogo" -> CatalogoScreen(libros, carrito, onGoInventario = { screen = "inventario" }, onGoJefe = { screen = "jefe" })
        "inventario" -> InventarioScreen(libros, movimientos)
        "jefe" -> JefeScreen(movimientos)
    }
}

// ---------------- LOGIN -------------------
@Composable
fun LoginScreen(onLogin: (String) -> Unit) {
    var user by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Iniciar Sesión", fontSize = 28.sp)
        Spacer(Modifier.height(20.dp))

        OutlinedTextField(value = user, onValueChange = { user = it }, label = { Text("Usuario") })
        OutlinedTextField(
            value = pass,
            onValueChange = { pass = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(Modifier.height(20.dp))
        Button(onClick = { onLogin(user) }) {
            Text("Entrar")
        }
    }
}

// ---------------- CATALOGO -------------------
@Composable
fun CatalogoScreen(libros: List<Libro>, carrito: MutableList<Libro>, onGoInventario: () -> Unit, onGoJefe: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(15.dp)) {
        Text("Catálogo", fontSize = 26.sp)

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = onGoInventario) { Text("Inventario") }
            Button(onClick = onGoJefe) { Text("Jefe") }
        }

        LazyColumn {
            items(libros) { libro ->
                Card(Modifier.fillMaxWidth().padding(8.dp)) {
                    Column(Modifier.padding(12.dp)) {
                        Text(libro.titulo, fontSize = 22.sp)
                        Text(libro.autor)
                        Text("Categoría: ${libro.categoria}")
                        Text("$${libro.precio}")
                        Text("Stock: ${libro.stock}")
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = { carrito.add(libro) }) {
                            val count = carrito.count { it.id == libro.id }
                            Text("Agregar (${count})")
                        }
                    }
                }
            }
        }
    }
}

// ---------------- INVENTARIO -------------------
@Composable
fun InventarioScreen(libros: MutableList<Libro>, movimientos: MutableList<Movimiento>) {
    var nuevoTitulo by remember { mutableStateOf("") }
    var nuevoAutor by remember { mutableStateOf("") }
    var nuevaCategoria by remember { mutableStateOf("") }
    var nuevoPrecio by remember { mutableStateOf("") }
    var empleadoId by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(15.dp)) {
        Text("Inventario", fontSize = 26.sp)

        Spacer(Modifier.height(10.dp))
        Text("Agregar Nuevo Libro", fontSize = 18.sp)

        OutlinedTextField(nuevoTitulo, { nuevoTitulo = it }, label = { Text("Título") })
        OutlinedTextField(nuevoAutor, { nuevoAutor = it }, label = { Text("Autor") })
        OutlinedTextField(nuevaCategoria, { nuevaCategoria = it }, label = { Text("Categoría") })
        OutlinedTextField(nuevoPrecio, { nuevoPrecio = it }, label = { Text("Precio") })

        Button(onClick = {
            if (nuevoTitulo.isNotBlank()) {
                val nuevoId = (libros.maxOfOrNull { it.id } ?: 0) + 1
                libros.add(Libro(nuevoId, nuevoTitulo, nuevoAutor, nuevaCategoria, nuevoPrecio.toDouble(), 0))
            }
        }) { Text("Guardar Libro") }

        Spacer(Modifier.height(20.dp))

        LazyColumn {
            items(libros) { libro ->
                Card(Modifier.fillMaxWidth().padding(8.dp)) {
                    Column(Modifier.padding(12.dp)) {
                        Text(libro.titulo, fontSize = 20.sp)
                        Text("Stock: ${libro.stock}")

                        OutlinedTextField(
                            value = empleadoId,
                            onValueChange = { empleadoId = it },
                            label = { Text("ID Empleado") }
                        )

                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                            Button(onClick = {
                                if (empleadoId.isNotBlank()) {
                                    libro.stock++
                                    movimientos.add(Movimiento("2025-01-01", empleadoId, libro.id, "Agregar", 1))
                                }
                            }) { Text("+1") }
                            Button(onClick = {
                                if (empleadoId.isNotBlank() && libro.stock > 0) {
                                    libro.stock--
                                    movimientos.add(Movimiento("2025-01-01", empleadoId, libro.id, "Restar", 1))
                                }
                            }) { Text("-1") }
                        }
                    }
                }
            }
        }
    }
}

// ---------------- JEFE -------------------
@Composable
fun JefeScreen(movimientos: List<Movimiento>) {
    Column(Modifier.fillMaxSize().padding(15.dp)) {
        Text("Panel del Jefe", fontSize = 26.sp)

        LazyColumn {
            items(movimientos) { mov ->
                Card(Modifier.fillMaxWidth().padding(8.dp)) {
                    Column(Modifier.padding(12.dp)) {
                        Text("Libro ID: ${mov.libroId}")
                        Text("Empleado: ${mov.empleadoId}")
                        Text("Tipo: ${mov.tipo}")
                        Text("Cantidad: ${mov.cantidad}")
                        Text("Fecha: ${mov.fecha}")
                    }
                }
            }
        }
    }
}



// ---------------- PREVIEWS -------------------

// Preview de Login
@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    MaterialTheme {
        LoginScreen(onLogin = {})
    }
}

// Preview de Catálogo
@Preview(showBackground = true)
@Composable
fun PreviewCatalogoScreen() {
    val libros = listOf(
        Libro(1, "El Quijote", "Cervantes", "Novela", 120.0, 5),
        Libro(2, "1984", "George Orwell", "Distopía", 200.0, 3)
    )
    val carrito = mutableListOf<Libro>()
    MaterialTheme {
        CatalogoScreen(libros, carrito, onGoInventario = {}, onGoJefe = {})
    }
}

// Preview de Inventario
@Preview(showBackground = true)
@Composable
fun PreviewInventarioScreen() {
    val libros = mutableListOf(
        Libro(1, "El Quijote", "Cervantes", "Novela", 120.0, 5),
        Libro(2, "1984", "George Orwell", "Distopía", 200.0, 3)
    )
    val movimientos = mutableListOf<Movimiento>()
    MaterialTheme {
        InventarioScreen(libros, movimientos)
    }
}

// Preview de Jefe
@Preview(showBackground = true)
@Composable
fun PreviewJefeScreen() {
    val movimientos = listOf(
        Movimiento("2025-01-01", "EMP001", 1, "Agregar", 1),
        Movimiento("2025-01-02", "EMP002", 2, "Restar", 1)
    )
    MaterialTheme {
        JefeScreen(movimientos)
    }
}