package com.example.cryptomonitor.data.api

import com.example.cryptomonitor.data.models.CryptoPrice
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface BinanceService {
    @GET("api/v3/ticker/price")
    suspend fun getPrice(
        @Header("X-MBX-APIKEY") apiKey: String,
        @Path("symbol") symbol: String
    ): CryptoPrice
}

interface BitsoService {
    @GET("api/v3/ticker/{symbol}")
    suspend fun getPrice(
        @Header("Authorization") apiKey: String,
        @Path("symbol") symbol: String
    ): CryptoPrice
}
