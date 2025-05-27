// QuanLyBanLaptopRetrofitClient.kt
package com.example.lapstore.api

import GioHangAPIService
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Constants {
    const val BASE_URL = "http://chillcup.io.vn/ITLabRoomAPI/api/"
}

object GiangVienRetrofitClient {
    val giangVienAPIService: GioHangAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(GioHangAPIService::class.java)
    }
}
