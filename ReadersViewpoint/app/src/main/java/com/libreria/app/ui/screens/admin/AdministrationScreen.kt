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

/**
 * Componente de tarjeta reutilizable para una opción en el panel de administración.
 *
 * @param modifier El [Modifier] a aplicar a la tarjeta.
 * @param icon El [ImageVector] que representa el icono de la opción.
 * @param title El título de la opción.
 * @param onClick La acción a ejecutar cuando se hace clic en la tarjeta.
 */
@Composable
fun AdminOptionCard(
    modifier: Modifier,
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    ElevatedCard(
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

/**
 * Pantalla principal del Panel de Administración.
 *
 * Muestra una cuadrícula de opciones navegables para la gestión del inventario,
 * personal, movimientos y ventas.
 *
 * @param onGoInventory Callback para navegar a la pantalla de gestión de inventario.
 * @param onGoEmployees Callback para navegar a la lista de empleados.
 * @param onGoMovements Callback para navegar al historial de movimientos de inventario.
 * @param onCreateAccount Callback para navegar a la pantalla de creación de nuevas cuentas de usuario.
 * @param onGoSalesHistory Callback para navegar al historial de ventas.
 * @param onGoBack Callback para cerrar la sesión de administrador o volver al login.
 */
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
    // Definición de las opciones del panel con sus iconos y acciones
    val options = listOf(
        Triple(Icons.Filled.Inventory, "Inventario", onGoInventory),
        Triple(Icons.Filled.Group, "Empleados", onGoEmployees),
        Triple(Icons.Filled.PersonAdd, "Crear Usuario", onCreateAccount),
        Triple(Icons.Filled.History, "Movimientos", onGoMovements),
        Triple(Icons.Filled.ReceiptLong, "Historial Ventas", onGoSalesHistory),
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF5F5EF)
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
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(Modifier.height(24.dp))

                Text(
                    "Gestión y Operaciones",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF655D4D)
                    ),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Spacer(Modifier.height(16.dp))

                // Distribución de las opciones en una cuadrícula
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        AdminOptionCard(
                            modifier = Modifier.weight(1f),
                            icon = options[0].first,
                            title = options[0].second,
                            onClick = options[0].third
                        )
                        AdminOptionCard(
                            modifier = Modifier.weight(1f),
                            icon = options[1].first,
                            title = options[1].second,
                            onClick = options[1].third
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        AdminOptionCard(
                            modifier = Modifier.weight(1f),
                            icon = options[2].first,
                            title = options[2].second,
                            onClick = options[2].third
                        )
                        AdminOptionCard(
                            modifier = Modifier.weight(1f),
                            icon = options[3].first,
                            title = options[3].second,
                            onClick = options[3].third
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
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