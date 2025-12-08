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

/**
 * Actividad inicial de la aplicación, responsable de mostrar la pantalla de bienvenida (Splash Screen).
 *
 * Esta actividad realiza una espera inicial corta y luego comprueba la disponibilidad de red
 * en un bucle antes de navegar a la [MainActivity].
 */
class SplashScreenActivity : ComponentActivity() {

    /**
     * Se llama cuando la actividad es creada.
     *
     * 1. Establece la interfaz de usuario con [SplashScreenUI].
     * 2. Inicia una corrutina en [lifecycleScope] para esperar 1.5 segundos
     * y luego comenzar el bucle de verificación de red [checkNetworkLoop].
     *
     * @param savedInstanceState Si la actividad se reinicia, este Bundle contiene los datos
     * proporcionados más recientemente en [onSaveInstanceState].
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SplashScreenUI()
        }
        lifecycleScope.launch {
            delay(1500)
            checkNetworkLoop()
        }
    }

    /**
     * Inicia un bucle de verificación de red asíncrono.
     *
     * Comprueba continuamente el estado de la red cada 3 segundos. Una vez que la red
     * esté disponible ([isNetworkAvailable] devuelve true), el bucle termina y
     * se llama a [navigateToMain].
     */
    private fun checkNetworkLoop() {
        lifecycleScope.launch {
            while (!isNetworkAvailable(this@SplashScreenActivity)) {
                delay(3000)
            }

            navigateToMain()
        }
    }

    /**
     * Verifica si existe una conexión de red activa.
     *
     * Utiliza el [ConnectivityManager] para obtener el estado de la red actual.
     *
     * @param context El contexto de la aplicación, necesario para acceder a [ConnectivityManager].
     * @return `true` si hay alguna red activa y conectada, `false` en caso contrario.
     */
    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork
        return activeNetwork != null
    }

    /**
     * Navega a la [MainActivity] e inmediatamente finaliza esta [SplashScreenActivity].
     *
     * Esto asegura que el usuario no pueda volver a la pantalla de bienvenida
     * usando el botón de retroceso.
     */
    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}