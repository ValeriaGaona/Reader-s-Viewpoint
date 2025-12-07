
package com.libreria.app.ui.screens.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
// ⬅️ Importa Iconos y Componentes para TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.* import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.libreria.app.R

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
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF5F5EF)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Administración") },
                    navigationIcon = {
                        IconButton(onClick = onGoBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver"
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
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(Modifier.height(32.dp))

                Text(
                    "",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(Modifier.height(24.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = onGoInventory,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF655D4D), contentColor = Color.White)
                    ) {
                        Text("Inventario", style = TextStyle(fontSize = 20.sp))
                    }
                    Spacer(Modifier.height(15.dp))

                    Button(
                        onClick = onGoEmployees,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF655D4D), contentColor = Color.White)
                    ) {
                        Text("Empleados", style = TextStyle(fontSize = 20.sp))
                    }
                    Spacer(Modifier.height(15.dp))

                    Button(
                        onClick = onCreateAccount,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF655D4D), contentColor = Color.White)
                    ) {
                        Text("Crear usuario", style = TextStyle(fontSize = 20.sp))
                    }
                    Spacer(Modifier.height(15.dp))

                    Button(
                        onClick = onGoMovements,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF655D4D), contentColor = Color.White)
                    ) {
                        Text("Movimientos", style = TextStyle(fontSize = 20.sp))
                    }
                    Spacer(Modifier.height(15.dp))

                    Button(
                        onClick = onGoSalesHistory,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF655D4D), contentColor = Color.White)
                    ) {
                        Text("Historial de Ventas", style = TextStyle(fontSize = 20.sp))
                    }
                }
            }
        }
    }
}