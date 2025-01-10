package com.example.cryptomonitor.data.api

data class BinanceResponse(
    val symbol: String,
    val price: String
)

data class BitsoResponse(
    val success: Boolean,
    val payload: BitsoPayload
)

data class BitsoPayload(
    val book: String,
    val last: String,
    val volume: String,
    val high: String,
    val low: String
)
