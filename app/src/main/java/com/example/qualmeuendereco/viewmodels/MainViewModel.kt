package com.example.qualmeuendereco.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qualmeuendereco.BaseApp
import com.example.qualmeuendereco.R
import com.example.qualmeuendereco.datasources.ViaCepRemoteDataSource
import com.example.qualmeuendereco.models.BrazilStateDataModel
import com.example.qualmeuendereco.models.ViaCepResponseModel
import com.example.qualmeuendereco.retrofit.gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _searchButtonState = MutableStateFlow(SearchButtonState.IDLE)
    private val _cep = MutableStateFlow<ViaCepResponseModel?>(null)
    private val _error = MutableStateFlow<Throwable?>(null)

    val searchButtonState = _searchButtonState.asStateFlow()
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

    fun getStates(context: Context): List<BrazilStateDataModel> {

        val content = context.assets.open(context.getString(R.string.state_list_asset_filename))
            .bufferedReader(
                Charsets.UTF_8
            ).use {
                it.readText()
            }

        val states = gson().fromJson(content, Array<BrazilStateDataModel>::class.java)

        return states.toList()
    }

    fun setSearchButtonState(state: SearchButtonState) {
        viewModelScope.launch { _searchButtonState.emit(state) }
    }

    enum class SearchButtonState {
        LOADING,
        LOADED,
        IDLE
    }
}