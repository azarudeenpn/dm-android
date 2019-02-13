package com.dm.client

data class VictimTicket(
    val name: String,
    val phone: String,
    val lat: Float,
    val lon: Float,
    val distance: Float,
    val timeDuration: Int
)