package com.libreria.app.utils

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.common.BitMatrix

/**
 * Genera un código QR (Quick Response) como un [ImageBitmap].
 *
 * Utiliza la librería ZXing para codificar el contenido en un [BitMatrix] y luego
 * convierte esa matriz en un [Bitmap] de Android, que a su vez se convierte en
 * el tipo de Compose [ImageBitmap].
 *
 * @param content La cadena de texto que se codificará en el código QR (e.g., el ID del ticket).
 * @param size El tamaño deseado (ancho y alto) del código QR en píxeles.
 * @return Un [ImageBitmap] que contiene el código QR generado.
 */
fun generateQrCodeBitmap(content: String, size: Int): ImageBitmap {
    val hints = mapOf(EncodeHintType.CHARACTER_SET to "UTF-8")
    val bitMatrix: BitMatrix = QRCodeWriter().encode(
        content,
        BarcodeFormat.QR_CODE,
        size,
        size,
        hints
    )
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)

    for (x in 0 until size) {
        for (y in 0 until size) {
            // Si la celda es 'true' (negra), usa 0xFF000000, si es 'false' (blanca), usa 0xFFFFFFFF
            bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) 0xFF000000.toInt() else 0xFFFFFFFF.toInt())
        }
    }

    return bitmap.asImageBitmap()
}