package com.example.qualmeuendereco.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qualmeuendereco.BaseApp
import com.example.qualmeuendereco.datasources.ViaCepRemoteDataSource
import com.example.qualmeuendereco.models.ViaCepResponseModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _cep = MutableStateFlow<ViaCepResponseModel?>(null)
    private val _error = MutableStateFlow<Throwable?>(null)

    val cep = _cep.asStateFlow()
    val error = _error.asStateFlow()

    fun consultaCep(cep: String) {
        viewModelScope.launch {
            try {
                val result = ViaCepRemoteDataSource(BaseApp.apiService).consultaCep(cep).first()
                _cep.value = result
            } catch (e: Throwable) {
                when (e) {
                    is ViaCepRemoteDataSource.ViaCepServiceException -> _error.value = e
                }
            }

        }
    }


}