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
    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Align Text("Librería", style = MaterialTheme.typography.headlineLarge)
    Spacer(Modifier.height(12.dp))
    OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier OutlinedTextField(value = pass, onValueChange = { pass = it }, label = { Text("Contraseña") }, visualTransforma Spacer(Modifier.height(12.dp))
            Button(onClick = {
        loading = true
        authVm.signIn(email, pass) { ok, msg ->
            loading = false
            if (ok) onSignedIn() else error = msg
        }
    }, modifier = Modifier.fillMaxWidth()) { Text("Iniciar sesión") }
    Spacer(Modifier.height(8.dp))
    OutlinedButton(onClick = { authVm.signInAsGuest { onGuest() } }, modifier = Modifier.fillMaxWidth()) {
        Text("Entrar como invitado")
    }
    error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
}
}
