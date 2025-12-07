
package com.libreria.app.ui.screens.guest

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.libreria.app.vm.CatalogViewModel
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.graphics.asAndroidBitmap
import android.graphics.Bitmap
import com.libreria.app.utils.generateQrCodeBitmap
import com.libreria.app.utils.rememberCaptureComposable
import com.libreria.app.utils.saveImageToGallery


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiptScreen(
    vm: CatalogViewModel,
    ticketId: String,
    origin: String,
    onGoHome: () -> Unit,
    onNavigateBackToHistory: () -> Unit
) {
    val ticket by vm.getTicketDetails(ticketId).collectAsState(initial = null)
    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale.getDefault()) }
    val context = LocalContext.current
    val localView = LocalView.current

    var qrCodeBitmap: ImageBitmap? by remember(ticketId) { mutableStateOf(null) }
    var ticketCardSize by remember { mutableStateOf(IntSize.Zero) }
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(ticket) {
        val currentTicket = ticket
        if (ticketId.isNotEmpty() && currentTicket != null) {

            // 1. Construye la lista de artículos en una cadena (ej: 2xLibroA;1xLibroB)
            val itemsString = currentTicket.items.joinToString(separator = ";") { item ->
                "${item.quantity}x${item.title.replace(" ", "_")}" // Reemplazar espacios para evitar errores de análisis
            }

            // 2. Combina todos los datos en una cadena delimitada (ej: ID|TOTAL|ARTÍCULOS)
            val dataToEncode = "$ticketId|${currentTicket.total}|$itemsString"

            // 3. Genera el QR con la cadena completa
            qrCodeBitmap = generateQrCodeBitmap(dataToEncode, 256)
        }
    }

    val captureCard = rememberCaptureComposable { imageBitmap ->
        capturedBitmap = imageBitmap.asAndroidBitmap()
    }

    LaunchedEffect(capturedBitmap) {
        capturedBitmap?.let { bitmap ->
            saveImageToGallery(context, bitmap, "Ticket_${ticketId}.png")
            capturedBitmap = null
        }
    }

    val isFromHistory = origin == "history"
    val buttonText = if (isFromHistory) "Volver al Historial" else "Volver al Catálogo"
    val navigationAction = if (isFromHistory) onNavigateBackToHistory else onGoHome

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF5F5EF)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Ticket de Compra") },
                    navigationIcon = {
                        IconButton(onClick = navigationAction) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
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
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                        .onGloballyPositioned { coordinates ->
                            ticketCardSize = coordinates.size
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xC8FFFFFF),
                        contentColor = Color.Black
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "LIBRERÍA 'READERS VIEWPOINT'",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text("Gracias por su compra.", style = MaterialTheme.typography.bodyMedium)
                        HorizontalDivider(Modifier.padding(vertical = 12.dp))

                        if (ticket == null || qrCodeBitmap == null) {
                            CircularProgressIndicator()
                            Text("Cargando detalles del ticket...")
                        } else {
                            val currentTicket = ticket!!

                            currentTicket.items.forEach { item ->
                                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                                    Text(
                                        "${item.quantity} x ${item.title}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        currencyFormat.format(item.price),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }

                            HorizontalDivider(Modifier.padding(vertical = 12.dp))

                            Text("TOTAL", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Text(
                                currencyFormat.format(currentTicket.total),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold
                            )

                            Spacer(Modifier.height(24.dp))

                            qrCodeBitmap?.let { bitmap ->
                                Image(
                                    bitmap = bitmap,
                                    contentDescription = "Código QR del Ticket $ticketId",
                                    modifier = Modifier.size(150.dp)
                                )
                            }

                            Spacer(Modifier.height(16.dp))

                            Text(
                                "ID de Transacción: $ticketId",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                "Fecha: ${currentTicket.date.substringBefore('T')}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                Spacer(Modifier.height(32.dp))

                Button(
                    onClick = {
                        captureCard(localView, ticketCardSize)
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF463C33),
                        contentColor = Color.White
                    )
                ) {
                    Text("Descargar Ticket como Imagen")
                }

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = navigationAction,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF655D4D),
                        contentColor = Color.White
                    )
                ) {
                    Text(buttonText)
                }
            }
        }
    }
}