package com.example.qualmeuendereco

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.qualmeuendereco.databinding.ActivityMainBinding
import com.example.qualmeuendereco.helpers.clearField
import com.example.qualmeuendereco.models.BrazilStateDataModel
import com.example.qualmeuendereco.viewmodels.MainViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private val activityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val mainViewModel by viewModels<MainViewModel>()

    private lateinit var cepField: TextInputEditText
    private lateinit var searchButton: Button
    private lateinit var referenceField: TextInputEditText
    private lateinit var ufField: AutoCompleteTextView
    private lateinit var cityField: TextInputEditText
    private lateinit var adapter: ArrayAdapter<String>

    private var newSearch = MutableStateFlow(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        searchButton = activityMainBinding.searchButton
        cepField = activityMainBinding.cepField
        referenceField = activityMainBinding.referenceField
        ufField = activityMainBinding.ufField
        cityField = activityMainBinding.cityField

        val ufList = mainViewModel.getStates(baseContext)

        mainViewModel.setSearchButtonState(MainViewModel.SearchButtonState.IDLE)

        adapter = ArrayAdapter<String>(
            this,
            R.layout.dropdown_menu_item,
            ufList.map { it.nome }
        )

        ufField.setAdapter(adapter)

        searchButton.setOnClickListener {
            val cep = cepField.text.toString()
            if (cep.isNotEmpty()) {
                mainViewModel.consultaCep(cep)
                mainViewModel.setSearchButtonState(MainViewModel.SearchButtonState.LOADING)
                newSearch.value = true
            } else {
                Toast.makeText(this, "Digite um CEP válido", Toast.LENGTH_SHORT).show()
            }
        }
        // Load data into UI
        MainScope().launch {
            mainViewModel.cep.collect {
                it?.let { endereco ->
                    referenceField.setText(endereco.bairro)
                    selectDropdownState(ufList.find { state -> state.sigla == endereco.uf })
                    cityField.setText(endereco.localidade)
                    mainViewModel.setSearchButtonState(MainViewModel.SearchButtonState.LOADED)
                }
            }
        }
        // Handle errors
        MainScope().launch {
            mainViewModel.error.collect {
                it?.let {
                    Toast.makeText(
                        this@MainActivity,
                        it.message ?: "Erro ao consultar CEP",
                        Toast.LENGTH_SHORT
                    ).show()
                    mainViewModel.setSearchButtonState(MainViewModel.SearchButtonState.IDLE)
                }
            }
        }
        // Handler SearchButtonState
        MainScope().launch {
            mainViewModel.searchButtonState.collect {
                when (it) {
                    MainViewModel.SearchButtonState.LOADING -> {
                        searchButton.isEnabled = false
                        searchButton.text = getString(R.string.searching)
                    }

                    MainViewModel.SearchButtonState.LOADED -> {
                        searchButton.isEnabled = true
                        searchButton.text = getString(R.string.new_search)
                    }

                    MainViewModel.SearchButtonState.IDLE -> {
                        searchButton.isEnabled = true
                        searchButton.text = getString(R.string.search)
                        resetFields()
                    }
                }
            }
        }
        // Handle newSearch
        MainScope().launch {
            newSearch.collect {
                if (it) {
                    resetFields()
                    newSearch.value = false
                }
            }
        }
    }

    private fun selectDropdownState(stateDataModel: BrazilStateDataModel?) {
        stateDataModel?.let { state ->
            ufField.setText(state.nome, false)
        }
    }

    private fun resetFields() {
        cepField.clearField()
        referenceField.clearField()
        ufField.clearField()
        cityField.clearField()
    }
}