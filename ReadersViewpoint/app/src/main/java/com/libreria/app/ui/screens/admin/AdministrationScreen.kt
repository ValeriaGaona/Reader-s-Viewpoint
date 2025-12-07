//
//package com.libreria.app.ui.screens.admin
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.*
//// ⬅️ Importa Iconos y Componentes para TopAppBar
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.ArrowBack
//import androidx.compose.material3.* import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.libreria.app.R
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AdministrationScreen(
//    onGoInventory: () -> Unit,
//    onGoEmployees: () -> Unit,
//    onGoMovements: () -> Unit,
//    onCreateAccount: () -> Unit,
//    onGoSalesHistory: () -> Unit,
//    onGoBack: () -> Unit
//) {
//    Surface(
//        modifier = Modifier.fillMaxSize(),
//        color = Color(0xFFF5F5EF)
//    ) {
//        Scaffold(
//            topBar = {
//                TopAppBar(
//                    title = { Text("Administración") },
//                    navigationIcon = {
//                        IconButton(onClick = onGoBack) {
//                            Icon(
//                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                                contentDescription = "Volver"
//                            )
//                        }
//                    },
//                    colors = TopAppBarDefaults.topAppBarColors(
//                        containerColor = Color(0xFF655D4D),
//                        titleContentColor = Color.White,
//                        navigationIconContentColor = Color.White
//                    )
//                )
//            },
//            containerColor = Color(0xFFF5F5EF)
//        ) { paddingValues ->
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(paddingValues)
//                    .padding(horizontal = 24.dp)
//            ) {
//                Spacer(Modifier.height(32.dp))
//
//                Text(
//                    "",
//                    style = MaterialTheme.typography.headlineMedium,
//                    modifier = Modifier.align(Alignment.CenterHorizontally)
//                )
//                Spacer(Modifier.height(24.dp))
//
//                Column(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Button(
//                        onClick = onGoInventory,
//                        modifier = Modifier.fillMaxWidth().height(50.dp),
//                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF655D4D), contentColor = Color.White)
//                    ) {
//                        Text("Inventario", style = TextStyle(fontSize = 20.sp))
//                    }
//                    Spacer(Modifier.height(15.dp))
//
//                    Button(
//                        onClick = onGoEmployees,
//                        modifier = Modifier.fillMaxWidth().height(50.dp),
//                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF655D4D), contentColor = Color.White)
//                    ) {
//                        Text("Empleados", style = TextStyle(fontSize = 20.sp))
//                    }
//                    Spacer(Modifier.height(15.dp))
//
//                    Button(
//                        onClick = onCreateAccount,
//                        modifier = Modifier.fillMaxWidth().height(50.dp),
//                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF655D4D), contentColor = Color.White)
//                    ) {
//                        Text("Crear usuario", style = TextStyle(fontSize = 20.sp))
//                    }
//                    Spacer(Modifier.height(15.dp))
//
//                    Button(
//                        onClick = onGoMovements,
//                        modifier = Modifier.fillMaxWidth().height(50.dp),
//                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF655D4D), contentColor = Color.White)
//                    ) {
//                        Text("Movimientos", style = TextStyle(fontSize = 20.sp))
//                    }
//                    Spacer(Modifier.height(15.dp))
//
//                    Button(
//                        onClick = onGoSalesHistory,
//                        modifier = Modifier.fillMaxWidth().height(50.dp),
//                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF655D4D), contentColor = Color.White)
//                    ) {
//                        Text("Historial de Ventas", style = TextStyle(fontSize = 20.sp))
//                    }
//                }
//            }
//        }
//    }
//}

package com.libreria.app.ui.screens.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ----------------------------------------------------------------------
// COMPONENTE AUXILIAR PARA LAS OPCIONES DEL DASHBOARD
// ----------------------------------------------------------------------

@Composable
fun AdminOptionCard(
    modifier: Modifier, // ✅ CORREGIDO: Recibe el Modifier que incluye .weight(1f)
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    ElevatedCard(
        // ✅ CORREGIDO: Aplica el Modifier recibido
        modifier = modifier
            .height(140.dp)
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.White,
            contentColor = Color(0xFF655D4D)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(40.dp),
                tint = Color(0xFF655D4D)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Color.Black.copy(alpha = 0.8f)
            )
        }
    }
}

// ----------------------------------------------------------------------
// PANTALLA PRINCIPAL DE ADMINISTRACIÓN
// ----------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdministrationScreen(
    onGoInventory: () -> Unit,
    onGoEmployees: () -> Unit,
    onGoMovements: () -> Unit,
    onCreateAccount: () -> Unit,
    onGoSalesHistory: () -> Unit,
    onGoBack: () -> Unit
) {
    // Definición de las opciones y sus iconos
    val options = listOf(
        Triple(Icons.Filled.Inventory, "Inventario", onGoInventory),
        Triple(Icons.Filled.Group, "Empleados", onGoEmployees),
        Triple(Icons.Filled.PersonAdd, "Crear Usuario", onCreateAccount),
        Triple(Icons.Filled.History, "Movimientos", onGoMovements),
        Triple(Icons.Filled.ReceiptLong, "Historial Ventas", onGoSalesHistory),
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF5F5EF) // Fondo Elegante: Beige muy claro
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Panel de Administración") },
                    navigationIcon = {
                        IconButton(onClick = onGoBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Cerrar Sesión"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF655D4D), // Color Principal: Marrón Oscuro
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
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(Modifier.height(24.dp))

                // Título descriptivo
                Text(
                    "Gestión y Operaciones",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF655D4D)
                    ),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Spacer(Modifier.height(16.dp))

                // 🚀 DISEÑO DE GRILLA (2x2) USANDO ROWS
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Fila 1: Inventario y Empleados
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // ✅ CORREGIDO: Pasa el weight al modifier
                        AdminOptionCard(
                            modifier = Modifier.weight(1f),
                            icon = options[0].first,
                            title = options[0].second,
                            onClick = options[0].third
                        )
                        // ✅ CORREGIDO: Pasa el weight al modifier
                        AdminOptionCard(
                            modifier = Modifier.weight(1f),
                            icon = options[1].first,
                            title = options[1].second,
                            onClick = options[1].third
                        )
                    }

                    // Fila 2: Crear Usuario y Movimientos
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // ✅ CORREGIDO: Pasa el weight al modifier
                        AdminOptionCard(
                            modifier = Modifier.weight(1f),
                            icon = options[2].first,
                            title = options[2].second,
                            onClick = options[2].third
                        )
                        // ✅ CORREGIDO: Pasa el weight al modifier
                        AdminOptionCard(
                            modifier = Modifier.weight(1f),
                            icon = options[3].first,
                            title = options[3].second,
                            onClick = options[3].third
                        )
                    }

                    // Fila 3: Historial de Ventas (Ocupa todo el ancho)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        // ✅ CORREGIDO: Pasa el weight al modifier
                        AdminOptionCard(
                            modifier = Modifier.weight(1f),
                            icon = options[4].first,
                            title = options[4].second,
                            onClick = options[4].third
                        )
                    }
                }
            }
        }
    }
}