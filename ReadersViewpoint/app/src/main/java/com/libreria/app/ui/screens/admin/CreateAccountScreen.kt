package com.libreria.app.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.libreria.app.vm.AdminViewModel
@Composable
fun CreateAccountScreen(adminVm: AdminViewModel, onDone: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("employee") }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Crear cuenta (Administrador)", style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(email, { email = it }, label = { Text("Email") })
        OutlinedTextField(password, { password = it }, label = { Text("Contraseña") })
        OutlinedTextField(name, { name = it }, label = { Text("Nombre") })
        Spacer(Modifier.height(8.dp))
        Row {
            Button(onClick = { role = "employee" }) { Text("Empleado") }
            Spacer(Modifier.width(8.dp))
            Button(onClick = { role = "admin" }) { Text("Administrador") }
        }
        Spacer(Modifier.height(8.dp))
        Button(onClick = {
            adminVm.createAccount(email, password, name, role) { ok, msg ->
                // handle result
            }
            onDone()
        }) { Text("Crear cuenta") }
    }
}