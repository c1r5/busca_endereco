package com.example.qualmeuendereco.datasources

import com.example.qualmeuendereco.models.ViaCepResponseModel
import com.example.qualmeuendereco.retrofit.ViacepRetrofitService
import com.example.qualmeuendereco.retrofit.gson
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class ViaCepRemoteDataSource(private val service: ViacepRetrofitService) {
    fun consultaCep(cep: String): Flow<ViaCepResponseModel> = callbackFlow {

        service.getEndereco(cep).enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(p0: Call<ResponseBody>, p1: Response<ResponseBody>) {
                if (p1.isSuccessful) {
                    when (p1.code()) {
                        200 -> {
                            val content = p1.body()?.string() ?: ""
                            if (content.contains("\"erro\": \"true\"")) {
                                close(ViaCepServiceNotFoundException())
                            } else {
                                val endereco =
                                    gson().fromJson(content, ViaCepResponseModel::class.java)
                                launch { send(endereco) }
                            }
                        }

                        400 -> close(ViaCepServiceInvalidException())
                        404 -> close(ViaCepServiceNotFoundException())
                        else -> close(ViaCepServiceException())
                    }
                } else {
                    close(ViaCepServiceException())
                }
            }

            override fun onFailure(p0: Call<ResponseBody>, p1: Throwable) {
                close(ViaCepServiceException())
            }
        })

        awaitClose { }
    }

    class ViaCepServiceNotFoundException : ViaCepServiceException("CEP não encontrado")
    class ViaCepServiceInvalidException : ViaCepServiceException("CEP inválido")
    open class ViaCepServiceException(message: String = "Erro ao consultar CEP") :
        Exception(message)
}