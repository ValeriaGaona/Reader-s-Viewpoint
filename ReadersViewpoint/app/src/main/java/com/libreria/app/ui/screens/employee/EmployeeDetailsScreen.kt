package com.libreria.app.ui.screens.employee

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.libreria.app.vm.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeDetailsScreen(
    employeeId: String,
    adminVm: AdminViewModel,
    onBack: () -> Unit,
    onDelete: (String) -> Unit
) {
    val employeeDetails by adminVm.getUserDetails(employeeId).collectAsState(initial = null)

    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(employeeDetails?.displayName ?: "Cargando detalles...") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF655D4D),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                actions = {
                    if (employeeDetails != null) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Eliminar empleado",
                                tint = Color.Red.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            )
        },
        containerColor = Color(0xFFF5F5EF)
    )
    { padding ->
        if (employeeDetails == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "Avatar",
                            modifier = Modifier.size(72.dp),
                            tint = Color(0xFF655D4D)
                        )
                        Spacer(Modifier.height(16.dp))

                        Text(
                            employeeDetails!!.displayName.ifBlank { "Nombre No Asignado" },
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF333333)
                        )
                        Spacer(Modifier.height(8.dp))

                        Text(
                            employeeDetails!!.role.uppercase(),
                            style = MaterialTheme.typography.titleLarge,
                            color = Color(0xFF388E3C),
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.height(16.dp))
                        Divider() // Separador
                        Spacer(Modifier.height(16.dp))

                        Text(
                            "Email:",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                        )
                        Text(
                            employeeDetails!!.email,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF555555)
                        )
                        Spacer(Modifier.height(16.dp))

                        Text(
                            "ID de Usuario:",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                        )
                        Text(
                            employeeId,
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.DarkGray)
                        )
                    }
                }
            }
        }
    }

    if (showDeleteDialog && employeeDetails != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirmar Eliminación") },
            text = {
                Text(
                    "¿Está seguro que desea eliminar la cuenta de ${employeeDetails!!.displayName} (${employeeDetails!!.email})? " +
                            "Esta acción es irreversible y eliminará todos sus datos."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete(employeeId)
                        showDeleteDialog = false
                        onBack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.8f))
                ) {
                    Text("Eliminar permanentemente")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}