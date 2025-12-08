package com.libreria.app.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import java.io.File
import java.io.OutputStream

/**
 * Guarda un [Bitmap] dado en la galería de imágenes del dispositivo Android.
 *
 * Utiliza [MediaStore] para almacenar la imagen de forma compatible con diferentes
 * versiones de Android (incluyendo el almacenamiento scoped a partir de Android Q).
 * Muestra un [Toast] al usuario con el resultado de la operación.
 *
 * @param context El contexto de la aplicación, necesario para acceder a [ContentResolver] y [Toast].
 * @param bitmap El mapa de bits ([Bitmap]) que se desea guardar.
 * @param fileName El nombre del archivo con el que se guardará la imagen. Por defecto, incluye un timestamp.
 */
fun saveImageToGallery(context: Context, bitmap: Bitmap, fileName: String = "ticket_${System.currentTimeMillis()}.png") {
    val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
    } else {
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }

    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
        put(MediaStore.Images.Media.MIME_TYPE, "image/png")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Define el subdirectorio dentro de PICTURES (ej: /Pictures/ReadersViewpoint)
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "ReadersViewpoint")
            // Marca el archivo como pendiente de escritura
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }
    }

    val uri = context.contentResolver.insert(collection, contentValues)

    uri?.let {
        try {
            val outputStream: OutputStream? = context.contentResolver.openOutputStream(it)
            outputStream?.use { stream ->
                // Comprime el bitmap en formato PNG y lo escribe en el stream
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Finaliza el archivo pendiente para hacerlo visible
                contentValues.clear()
                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                context.contentResolver.update(it, contentValues, null, null)
            }
            Toast.makeText(context, "Ticket guardado en la galería.", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error al guardar la imagen.", Toast.LENGTH_LONG).show()
        }
    }
}