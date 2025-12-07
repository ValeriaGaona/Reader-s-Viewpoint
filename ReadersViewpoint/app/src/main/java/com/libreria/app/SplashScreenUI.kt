package com.libreria.app

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.res.painterResource // Asegúrate de tener tu logo en res/drawable
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun SplashScreenUI() {
    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF5F5EF)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_rv), // 🖼️ CAMBIA 'tu_logo'
                contentDescription = "Logo de la Aplicación",
                modifier = Modifier.size(200.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(32.dp))
            CircularProgressIndicator(
                color = Color(0xFF655D4D) // Color de tu tema
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Conectando a la librería...", color = Color.Gray)
        }
    }
}