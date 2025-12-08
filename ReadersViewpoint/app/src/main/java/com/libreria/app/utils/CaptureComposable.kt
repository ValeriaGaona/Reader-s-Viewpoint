package com.libreria.app.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.IntSize

/**
 * Hook Composable para crear un capturador de vistas Composable que persiste a través de recomposiciones.
 *
 * Devuelve una lambda que puede ser invocada para dibujar un [View] (Composable) en un [Bitmap]
 * y devolverlo como [ImageBitmap] a través del callback [onBitmapReady].
 *
 * @param onBitmapReady Callback que recibe el [ImageBitmap] una vez que la captura se ha completado.
 * @return Una función lambda (capturador) que toma la vista Composable a capturar y su tamaño.
 */
@Composable
fun rememberCaptureComposable(
    onBitmapReady: (ImageBitmap) -> Unit
): (View, IntSize) -> Unit {

    val view = LocalView.current

    return remember {
        /**
         * Función capturadora que toma la vista Composable y sus dimensiones.
         *
         * @param composableView La vista [View] del Composable a capturar (generalmente un Box o Column).
         * @param size El tamaño ([IntSize]) de la vista capturada.
         */
        { composableView: View, size: IntSize ->
            if (size.width > 0 && size.height > 0) {
                // 1. Crea un Bitmap vacío con el tamaño de la vista
                val bitmap = Bitmap.createBitmap(size.width, size.height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)

                // 2. Dibuja la jerarquía de la vista en el Canvas del Bitmap
                composableView.draw(canvas)

                // 3. Convierte y devuelve el Bitmap como ImageBitmap de Compose
                onBitmapReady(bitmap.asImageBitmap())
            }
        }
    }
}