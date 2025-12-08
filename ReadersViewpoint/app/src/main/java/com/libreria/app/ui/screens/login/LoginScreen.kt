package com.libreria.app.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.libreria.app.R
import com.libreria.app.vm.AuthViewModel

/**
 * Pantalla de inicio de sesión de la aplicación.
 *
 * Permite a los usuarios ingresar sus credenciales (email y contraseña) para iniciar sesión
 * o acceder como invitado.
 *
 * @param authVm El [AuthViewModel] que maneja la lógica de autenticación.
 * @param onGuest Callback que se ejecuta cuando se selecciona entrar como invitado.
 * @param onSignedIn Callback que se ejecuta cuando el inicio de sesión es exitoso.
 */
@Composable
fun LoginScreen(authVm: AuthViewModel, onGuest: () -> Unit, onSignedIn: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF5F5EF) // Fondo de la interfaz
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Imagen de bienvenida/logo
            Image(
                painter = painterResource(id = R.drawable.imgini),
                contentDescription = "Logo de bienvenida",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(12.dp))

            // Campo de texto para el Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            // Campo de texto para la Contraseña
            OutlinedTextField(
                value = pass,
                onValueChange = { pass = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            // Botón de Iniciar Sesión
            Button(
                onClick = {
                    loading = true
                    // Llama a la función de inicio de sesión del ViewModel
                    authVm.signIn(email, pass) { ok, msg ->
                        loading = false
                        if (ok) onSignedIn() else error = msg
                    }
                },
                enabled = !loading,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF655D4D),
                    contentColor = Color.White
                )
            ) {
                Text("Iniciar sesión")
                // Muestra un indicador de carga si la autenticación está en progreso
                if (loading) {
                    Spacer(Modifier.width(8.dp))
                    CircularProgressIndicator(
                        Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // Botón de Entrar como Invitado
            OutlinedButton(
                onClick = {
                    authVm.signInAsGuest {
                        onGuest()
                    }
                },
                enabled = !loading,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF655D4D)
                )
            ) {
                Text("Entrar como invitado")
            }

            // Muestra el mensaje de error si existe
            error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        }
    }
}