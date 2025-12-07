package com.libreria.app

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Aquí puedes usar un Layout XML simple o Compose para el logo
        setContent {
            // 🎨 Muestra tu logo aquí.
            SplashScreenUI()
        }

        // Lógica de carga y redirección
        lifecycleScope.launch {
            // Espera un mínimo de 1.5 segundos para que el usuario vea el logo
            delay(1500)

            checkNetworkLoop()
        }
    }

    private fun checkNetworkLoop() {
        lifecycleScope.launch {
            while (!isNetworkAvailable(this@SplashScreenActivity)) {
                // Si no hay internet, espera y vuelve a intentar.
                delay(3000) // Espera 3 segundos
            }

            // Si la red está disponible, navega a la actividad principal (login).
            navigateToMain()
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Verifica si hay alguna conexión activa (Wi-Fi o datos móviles)
        val activeNetwork = connectivityManager.activeNetwork
        return activeNetwork != null
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Cierra la SplashScreen para que el usuario no pueda volver a ella.
    }
}