package com.example.testexomind.model

import com.google.gson.annotations.SerializedName
//Je recupere seulement les infos dont j'ai besoin
data class WeatherResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("main")
    val main: Main,
    @SerializedName("name")
    val name: String,
    @SerializedName("weather")
    val weather: List<Weather>,
)