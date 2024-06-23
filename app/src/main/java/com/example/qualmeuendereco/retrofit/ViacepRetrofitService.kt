package com.example.qualmeuendereco.retrofit

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

fun interface ViacepRetrofitService {
    @GET("ws/{cep}/json/")
    fun getEndereco(@Path("cep") cep: String): Call<ResponseBody>
}