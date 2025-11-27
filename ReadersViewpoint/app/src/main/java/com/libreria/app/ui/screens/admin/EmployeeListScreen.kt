package com.libreria.app.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.libreria.app.data.model.UserProfile
@Composable
fun EmployeeListScreen(employees: List<UserProfile>) {
    Column(Modifier.fillMaxSize().padding(12.dp)) {
        Text("Empleados", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        LazyColumn {
            items(employees) { e ->
                Card(Modifier.fillMaxWidth().padding(6.dp)) {
                    Row(Modifier.padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text(e.displayName.ifBlank { e.email })
                            Text("ID: ${e.uid}")
                        }
                        Text(e.role)
                    }
                }
            }
        }
    }
}
