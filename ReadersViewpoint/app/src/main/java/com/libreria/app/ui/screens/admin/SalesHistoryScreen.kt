package com.libreria.app.ui.screens.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.libreria.app.vm.CatalogViewModel

import com.libreria.app.data.model.Ticket
import com.libreria.app.data.model.TicketItem

import java.text.NumberFormat
import java.util.Locale
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesHistoryScreen(
    vm: CatalogViewModel,
    onClose: () -> Unit,
    onViewTicketDetails: (String) -> Unit
) {
    val tickets by vm.tickets.collectAsState()

    val format = NumberFormat.getCurrencyInstance(Locale.getDefault())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial de Ventas") },
                navigationIcon = {
                    IconButton(onClick = onClose) {
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
    ) { padding ->
        if (tickets.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No hay ventas registradas aún.",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        } else {

            LazyColumn(
                modifier = Modifier.padding(padding).padding(horizontal = 8.dp)
            ) {
                items(tickets) { ticket ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp)
                            .clickable { onViewTicketDetails(ticket.id) },

                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xC87A6F5F),
                            contentColor = Color.Black
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {

                            Text(
                                "Ticket #${ticket.id}",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "Fecha: ${ticket.date.substringBefore('T')}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                "Artículos: ${ticket.items.size}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                "Total: ${format.format(ticket.total)}",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color(0xFF463C33)
                            )
                        }
                    }
                }
            }
        }
    }
}