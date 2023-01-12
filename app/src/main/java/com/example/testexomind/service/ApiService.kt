package com.example.testexomind.service

import com.example.testexomind.BuildConfig
import com.example.testexomind.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {
    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("q") city: String?,
        @Query("appid") str2: String?=BuildConfig.API_KEY,
        @Query("units") str3: String?="metric",
        @Query("lang") str4: String?="fr",
    ): WeatherResponse
}