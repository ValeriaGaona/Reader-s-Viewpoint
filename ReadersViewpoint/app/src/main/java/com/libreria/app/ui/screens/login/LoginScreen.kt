package com.libreria.app.ui.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.libreria.app.vm.AuthViewModel

@Composable
fun LoginScreen(authVm: AuthViewModel, onGuest: () -> Unit, onSignedIn: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        // 1. CORRECCIÓN DE COLUMN: Cerrar paréntesis y completar Alignment
        horizontalAlignment = Alignment.CenterHorizontally
    ) { // <-- Abre el bloque de contenido de Column

        Text("Librería", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(12.dp))

        // 2. CORRECCIÓN DE OutlinedTextField 1: Completar Modifier y cerrar paréntesis
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth() // <-- Asumiendo que faltaba .fillMaxWidth()
        ) // <-- Cierre correcto

        // 3. CORRECCIÓN DE OutlinedTextField 2: Completar visualTransformation y cerrar paréntesis
        OutlinedTextField(
            value = pass,
            onValueChange = { pass = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(), // <-- Corrección de visualTransforma
            modifier = Modifier.fillMaxWidth() // <-- Agregando modifier para consistencia
        ) // <-- Cierre correcto

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = {
                loading = true
                authVm.signIn(email, pass) { ok, msg ->
                    loading = false
                    if (ok) onSignedIn() else error = msg
                }
            },
            enabled = !loading, // Mejorar UX deshabilitando el botón durante la carga
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Iniciar sesión")
            if (loading) {
                Spacer(Modifier.width(8.dp))
                CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
            }
        }

        Spacer(Modifier.height(8.dp))

        OutlinedButton(
            onClick = {
                authVm.signInAsGuest {
                    onGuest()
                }
            },
            enabled = !loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Entrar como invitado")
        }

        error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
    } // <-- Cierre del bloque Column
} // <-- Cierre de la función LoginScreen