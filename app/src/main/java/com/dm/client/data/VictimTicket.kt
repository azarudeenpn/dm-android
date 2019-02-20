package com.dm.client.data

data class VictimTicket(
    val name: String,
    val phone: String,
    val lat: Float,
    val lon: Float,
    val distance: Float,
    val timeDuration: Int
)