package com.example.qualmeuendereco.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qualmeuendereco.BaseApp
import com.example.qualmeuendereco.datasources.ViaCepRemoteDataSource
import com.example.qualmeuendereco.models.Endereco
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _cep = MutableStateFlow<Endereco?>(null)
    private val _error = MutableStateFlow<Throwable?>(null)

    val cep = _cep.asStateFlow()
    val error = _error.asStateFlow()

    fun consultaCep(cep: String) {
        viewModelScope.launch {
            try {
                ViaCepRemoteDataSource(BaseApp.apiService).consultaCep(cep).collect {
                    _cep.value = it
                }
            } catch (e: Exception) {
                _error.value = e
            }
        }
    }


}