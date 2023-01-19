package com.auf.letswatch.services.helper

import com.auf.letswatch.constants.BASE_API_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitHelper {
    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}