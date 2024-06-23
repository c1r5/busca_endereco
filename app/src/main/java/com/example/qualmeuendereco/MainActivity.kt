package com.example.qualmeuendereco

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.qualmeuendereco.databinding.ActivityMainBinding
import com.example.qualmeuendereco.viewmodels.MainViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val activityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val mainViewModel by viewModels<MainViewModel>()

    private lateinit var cepField: TextInputEditText
    private lateinit var searchButton: Button
    private lateinit var referenceField: TextInputEditText
    private lateinit var ufField: TextInputEditText
    private lateinit var cityField: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        searchButton = activityMainBinding.searchButton
        cepField = activityMainBinding.cepField
        referenceField = activityMainBinding.referenceField
        ufField = activityMainBinding.ufField
        cityField = activityMainBinding.cityField

        searchButton.setOnClickListener {
            val cep = cepField.text.toString()
            if (cep.isNotEmpty()) {
                mainViewModel.consultaCep(cep)
            } else {
                Toast.makeText(this, "Digite um CEP válido", Toast.LENGTH_SHORT).show()
            }
        }

        MainScope().launch {
            mainViewModel.cep.collect {
                it?.let { endereco ->
                    referenceField.setText(endereco.bairro)
                    ufField.setText(endereco.uf)
                    cityField.setText(endereco.localidade)
                }
            }
        }

        MainScope().launch {
            mainViewModel.error.collect {
                it?.let {
                    Toast.makeText(
                        this@MainActivity,
                        it.message ?: "Erro ao consultar CEP",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}