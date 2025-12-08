package com.libreria.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

/**
 * Componente Composable que representa la pantalla de bienvenida (Splash Screen).
 *
 * Muestra el logo de la aplicación, un indicador de progreso de carga,
 * y un mensaje de estado mientras se realizan las tareas de inicialización,
 * como la conexión a Firebase o la carga de datos iniciales.
 *
 * Estructura de la UI:
 * - Un [Surface] que ocupa toda la pantalla con un color de fondo claro.
 * - Un [Column] centrado vertical y horizontalmente.
 * - [Image] con el logo de la aplicación (R.drawable.ic_rv).
 * - [CircularProgressIndicator] para indicar actividad de carga.
 * - [Text] mostrando el estado actual ("Conectando a la librería...").
 */
@Composable
fun SplashScreenUI() {
    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFB9B9AA)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_rv),
                contentDescription = "Logo de la Aplicación",
                modifier = Modifier.size(200.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(32.dp))
            CircularProgressIndicator(
                color = Color(0xFF655D4D)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Conectando a la librería...", color = Color.Gray)
        }
    }
}