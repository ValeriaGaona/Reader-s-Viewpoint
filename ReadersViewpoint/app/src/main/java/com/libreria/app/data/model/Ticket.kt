package com.libreria.app.data.model

data class Ticket(
    val id: String = "",
    val date: String = "",
    val total: Double = 0.0,
    val items: List<TicketItem> = emptyList()
)