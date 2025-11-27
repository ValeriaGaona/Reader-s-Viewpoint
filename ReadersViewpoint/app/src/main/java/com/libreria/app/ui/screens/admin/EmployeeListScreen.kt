package com.libreria.app.ui.screens.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.libreria.app.data.model.UserProfile
// ❌ La importación de InventoryViewModel fue removida, ya que la clase no se usa en la vista.


@Composable
fun EmployeeListScreen(
    // ❌ vm: InventoryViewModel, <-- ELIMINADO para corregir el error de compilación
    employees: List<UserProfile>,
    onViewDetails: (String) -> Unit, // ✅ HANDLER
    onClose: () -> Unit // ✅ HANDLER
) {

    Column(Modifier.fillMaxSize().padding(12.dp)) {

        // --- Encabezado con botón de cierre ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Empleados", style = MaterialTheme.typography.headlineMedium)

            // Botón de cierre
            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cerrar lista de empleados"
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // --- Lista de Empleados ---
        LazyColumn {
            items(employees) { e ->
                // ✅ Hacemos el Card cliqueable para ver detalles
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                        .clickable { onViewDetails(e.uid) } // Llama a onViewDetails con el ID del empleado
                ) {
                    Row(Modifier.padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text(e.displayName.ifBlank { e.email }, style = MaterialTheme.typography.titleMedium)
                            Text("ID: ${e.uid}", style = MaterialTheme.typography.bodySmall)
                        }
                        Text(e.role)
                    }
                }
            }
        }
    }
}
// Agregando el Preview para que el archivo compile completamente
@Preview(showBackground = true)
@Composable
fun EmployeeListScreenPreview() {
    val sampleEmployees = listOf(
        UserProfile(uid = "1", displayName = "Alice", email = "alice@example.com", role = "Admin"),
        UserProfile(uid = "2", displayName = "Bob", email = "bob@example.com", role = "User")
    )
    EmployeeListScreen(
        employees = sampleEmployees,
        onViewDetails = {},
        onClose = {}
    )
}