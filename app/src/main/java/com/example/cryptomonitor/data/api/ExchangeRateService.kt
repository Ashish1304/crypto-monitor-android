package com.example.cryptomonitor.data.api

import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeRateService {
    @GET("latest")
    suspend fun getExchangeRates(
        @Query("base") base: String = "USD",
        @Query("symbols") symbols: String = "MXN"
    ): ExchangeRateResponse
}

data class ExchangeRateResponse(
    val rates: Map<String, Double>,
    val base: String,
    val date: String
)
