package com.example.qualmeuendereco.datasources

import com.example.qualmeuendereco.models.Endereco
import com.example.qualmeuendereco.retrofit.ViacepRetrofitService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class ViaCepRemoteDataSource(private val service: ViacepRetrofitService) {
    fun consultaCep(cep: String): Flow<Endereco> = callbackFlow {

        service.getEndereco(cep).enqueue(object : retrofit2.Callback<Endereco> {
            override fun onResponse(p0: Call<Endereco>, p1: Response<Endereco>) {
                if (p1.code() == 200) {
                    launch { p1.body()?.let { send(it) } }
                } else {
                    close(Throwable("Erro ao consultar CEP"))
                }
            }

            override fun onFailure(p0: Call<Endereco>, p1: Throwable) {
                close(p1)
            }
        })

        awaitClose { }
    }
}