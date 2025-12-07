//package com.libreria.app.ui.screens.employee
//
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.ArrowBack
//import androidx.compose.material.icons.filled.Delete
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import com.libreria.app.vm.AdminViewModel
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun EmployeeDetailsScreen(
//    employeeId: String,
//    adminVm: AdminViewModel,
//    onBack: () -> Unit,
//    onDelete: (String) -> Unit
//) {
//    val employeeDetails by adminVm.getUserDetails(employeeId).collectAsState(initial = null)
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text(employeeDetails?.displayName ?: "Cargando detalles...") },
//                navigationIcon = {
//                    IconButton(onClick = onBack) {
//                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
//                    }
//                },
//                actions = {
//                    if (employeeDetails != null) {
//                        IconButton(
//                            onClick = {
//                                onDelete(employeeId)
//                            }
//                        ) {
//                            Icon(
//                                imageVector = Icons.Filled.Delete,
//                                contentDescription = "Eliminar empleado",
//                                tint = Color.Red
//                            )
//                        }
//                    }
//                }
//            )
//        }
//    )
//    { padding ->
//        Column(Modifier.padding(padding).padding(16.dp)) {
//            if (employeeDetails == null) {
//                CircularProgressIndicator()
//            } else {
//                Text("Nombre: ${employeeDetails!!.displayName}", style = MaterialTheme.typography.titleMedium)
//                Text("Email: ${employeeDetails!!.email}", style = MaterialTheme.typography.bodyLarge)
//                Text("Rol: ${employeeDetails!!.role.uppercase()}", style = MaterialTheme.typography.bodyLarge)
//
//            }
//        }
//    }
//}

package com.libreria.app.ui.screens.employee

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
// ⬅️ Importa TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
                        IconButton(
                            onClick = {
                                onDelete(employeeId)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Eliminar empleado",
                                tint = Color.Red.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            )
        }
    )
    { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            if (employeeDetails == null) {
                CircularProgressIndicator()
            } else {
                Text("Nombre: ${employeeDetails!!.displayName}", style = MaterialTheme.typography.titleMedium)
                Text("Email: ${employeeDetails!!.email}", style = MaterialTheme.typography.bodyLarge)
                Text("Rol: ${employeeDetails!!.role.uppercase()}", style = MaterialTheme.typography.bodyLarge)

            }
        }
    }
}