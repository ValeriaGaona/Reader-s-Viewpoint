package com.libreria.app.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.libreria.app.data.model.Movimiento
import com.libreria.app.vm.InventoryViewModel

@Composable
fun AdminMovementsScreen(vmInventory: InventoryViewModel) {
    val movements by vmInventory.movements.collectAsState()

    Column(Modifier.fillMaxSize().padding(12.dp)) {
        Text("Movimientos (Administrador)", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))

        LazyColumn {
            items(movements) { mov ->
                Card(Modifier.fillMaxWidth().padding(6.dp)) {
                    Column(Modifier.padding(10.dp)) {
                        Text(
                            text = "[${mov.dateIso}] ${mov.action} - ${mov.bookTitle}",
                            style = MaterialTheme.typography.titleMedium
                        )

                        // CORRECCIÓN 1: Se completa la interpolación de cadena con mov.quantity y se cierra el paréntesis
                        Text(
                            text = "Empleado: ${mov.employeeName} (ID: ${mov.employeeId}) | Cantidad: ${mov.quantity}",
                            style = MaterialTheme.typography.bodySmall
                        ) // <-- Cierra el Text
                    } // <-- Cierra el Column dentro del Card
                } // <-- Cierra el Card
            }
        } // <-- Cierra el LazyColumn
    } // <-- Cierra el Column principal
} // <-- Cierra el Composable AdminMovementsScreen