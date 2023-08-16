package com.example.androidcrown.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.http.GET

class MainRetrofitClient {
    companion object {
        val CLIENT = Retrofit.Builder()
            .baseUrl("https://gist.githubusercontent.com/lubetel/")
            .build()
            .create(Api::class.java)
    }
    interface Api {
        @GET("b0399e72219b149222ab4dbd5105f7c0/raw/025beb00e63d8383504a0b8d97e69310b99e4eb1/gistfile1.txt")
        suspend fun get(): ResponseBody
    }
    @Serializable
    data class Pusk(
        val pusk: Boolean,
        val link: String?
    )
}