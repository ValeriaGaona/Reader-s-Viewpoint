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

@Composable
fun rememberCaptureComposable(
    onBitmapReady: (ImageBitmap) -> Unit
): (View, IntSize) -> Unit {

    val view = LocalView.current

    return remember {
        { composableView: View, size: IntSize ->
            if (size.width > 0 && size.height > 0) {
                val bitmap = Bitmap.createBitmap(size.width, size.height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)

                composableView.draw(canvas)

                onBitmapReady(bitmap.asImageBitmap())
            }
        }
    }
}