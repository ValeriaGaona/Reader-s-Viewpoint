package com.libreria.app.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.libreria.app.data.model.Movimiento
import com.libreria.app.vm.InventoryViewModel
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.libreria.app.R
import androidx.compose.ui.layout.ContentScale

@Composable
fun AdminMovementsScreen(
    vmInventory: InventoryViewModel,
    onClose: () -> Unit // ✅ HANDLER AÑADIDO
) {
    val movements by vmInventory.movements.collectAsState()

    Image(
        painter = painterResource(id = R.drawable.imgmov),
        contentDescription = "_",
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp), // Ajusta la altura según necesites
        contentScale = ContentScale.Crop // Recorta la imagen para llenar el espacio
    )
    Spacer(Modifier.height(12.dp))

    Column(Modifier.fillMaxSize().padding(12.dp)) {

        // --- Encabezado con botón de cierre ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Movimientos (Administrador)", style = MaterialTheme.typography.headlineMedium)

            // Botón de cierre
            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cerrar movimientos"
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // --- Lista de Movimientos ---
        LazyColumn {
            items(movements) { mov ->
                Card(Modifier.fillMaxWidth().padding(6.dp)) {
                    Column(Modifier.padding(10.dp)) {
                        Text(
                            text = "[${mov.dateIso}] ${mov.action} - ${mov.bookTitle}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Empleado: ${mov.employeeName} (ID: ${mov.employeeId}) | Cantidad: ${mov.quantity}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}