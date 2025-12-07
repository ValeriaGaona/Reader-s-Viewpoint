package com.libreria.app.ui.screens.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
// ⬅️ Importa el icono de retroceso
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.libreria.app.R
import com.libreria.app.data.model.UserProfile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeListScreen(
    employees: List<UserProfile>,
    onViewDetails: (String) -> Unit,
    onClose: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF5F5EF)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Gestión de Personal") },
                    navigationIcon = {
                        IconButton(onClick = onClose) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
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

                Spacer(Modifier.height(16.dp))

                if (employees.isEmpty()) {
                    Text(
                        "No se encontró ningún usuario con rol 'admin' o 'employee'.",
                        modifier = Modifier.padding(vertical = 24.dp)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(employees) { e ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(6.dp)
                                    .clickable { onViewDetails(e.uid) },
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xC87A6F5F),
                                    contentColor = Color.Black
                                )
                            ){
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(Modifier.weight(1f)) {
                                        Text(
                                            e.displayName.ifBlank { e.email },
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Text("ID: ${e.uid}", style = MaterialTheme.typography.bodySmall)
                                    }
                                    Text(
                                        e.role.replaceFirstChar { it.uppercase() },
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun EmployeeListScreenPreview() {
    val sampleEmployees = listOf(
        UserProfile(uid = "1", displayName = "Alice Smith", email = "alice@example.com", role = "admin"),
        UserProfile(uid = "2", displayName = "", email = "bob_employee@example.com", role = "employee")
    )
    EmployeeListScreen(
        employees = sampleEmployees,
        onViewDetails = {},
        onClose = {}
    )
}