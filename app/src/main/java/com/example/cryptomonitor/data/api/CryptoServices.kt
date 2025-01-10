package com.example.cryptomonitor.data.api

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface BinanceService {
    @GET("api/v3/ticker/price")
    suspend fun getPrice(
        @Header("X-MBX-APIKEY") apiKey: String,
        @Query("symbol") symbol: String
    ): BinanceResponse
}

interface BitsoService {
    @GET("v3/ticker/")
    suspend fun getPrice(
        @Header("Authorization") apiKey: String,
        @Query("book") book: String
    ): BitsoResponse
}
