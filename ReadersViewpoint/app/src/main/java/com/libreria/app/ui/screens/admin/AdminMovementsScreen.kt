
package com.libreria.app.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
// ⬅️ Importa Iconos necesarios
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.libreria.app.data.model.Movimiento
import com.libreria.app.vm.InventoryViewModel
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.libreria.app.R
import androidx.compose.ui.layout.ContentScale

@OptIn(ExperimentalMaterial3Api::class) // ⬅️ AÑADIDO
@Composable
fun AdminMovementsScreen(
    vmInventory: InventoryViewModel,
    onClose: () -> Unit
) {
    val movements by vmInventory.movements.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF5F5EF)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Historial de Movimientos") },
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
                    .padding(horizontal = 12.dp)
            ) {
                Spacer(Modifier.height(40.dp))

                Spacer(Modifier.height(8.dp))

                LazyColumn {
                    items(movements) { mov ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xC87A6F5F),
                                contentColor = Color.Black
                            )
                        ) {
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
    }
}