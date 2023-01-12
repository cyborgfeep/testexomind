package com.example.testexomind.service

import com.example.testexomind.BuildConfig
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    companion object {
        private const val BASE_URL = BuildConfig.API_URL
        private var retrofit: Retrofit? = null
        val client: Retrofit?
            get() {
                if (retrofit == null) {
                    retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(
                        GsonConverterFactory.create(
                            GsonBuilder().setLenient().create()
                        )
                    ).build()
                }
                return retrofit
            }
    }
}
