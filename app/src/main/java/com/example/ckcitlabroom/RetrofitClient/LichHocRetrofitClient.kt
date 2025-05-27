// QuanLyBanLaptopRetrofitClient.kt
package com.example.lapstore.api

import LichHocAPIService
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object LichHocRetrofitClient {
    val lichhocAPIService: LichHocAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(LichHocAPIService::class.java)
    }
}
