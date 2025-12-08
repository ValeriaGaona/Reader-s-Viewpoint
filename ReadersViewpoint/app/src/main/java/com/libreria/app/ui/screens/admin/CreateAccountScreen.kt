package com.libreria.app.ui.screens.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.libreria.app.R
import com.libreria.app.vm.AdminViewModel

/**
 * Pantalla para que un administrador cree una nueva cuenta de usuario (empleado o administrador).
 *
 * Permite ingresar email, contraseña, nombre y seleccionar el rol del nuevo usuario.
 *
 * @param adminVm El [AdminViewModel] responsable de la lógica de creación de cuentas de usuario.
 * @param onDone Callback que se invoca después de intentar crear la cuenta (puede cerrar la pantalla).
 * @param onClose Callback para cerrar la pantalla sin crear la cuenta y regresar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAccountScreen(adminVm: AdminViewModel, onDone: () -> Unit, onClose: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("employee") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF5F5EF)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Crear Nueva Cuenta") },
                    navigationIcon = {
                        IconButton(onClick = onClose) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Cerrar")
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
                    .padding(horizontal = 16.dp)
            ) {

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(16.dp))

                // Selector de Rol
                Row {

                    Button(
                        onClick = { role = "employee" },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (role == "employee") {
                                Color(0xFF655D4D)
                            } else {
                                Color(0xFF837F75)
                            }
                        )
                    ) { Text("Empleado") }

                    Spacer(Modifier.width(8.dp))

                    Button(
                        onClick = { role = "admin" },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (role == "admin") {
                                Color(0xFF655D4D)
                            } else {
                                Color(0xFF837F75)
                            }
                        )
                    ) {
                        Text("Administrador")
                    }
                }
                Spacer(Modifier.height(24.dp))
                // Botón de Creación
                Button(
                    onClick = {
                        // Llama a la función de creación de cuenta
                        adminVm.createAccount(email, password, name, role) { ok, msg ->
                        }
                        onDone() // Llama a la acción de finalización
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = email.isNotBlank() && password.isNotBlank() && name.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF655D4D),
                        contentColor = Color.White
                    )
                ) {
                    Text("Crear cuenta")
                }
            }
        }
    }
}