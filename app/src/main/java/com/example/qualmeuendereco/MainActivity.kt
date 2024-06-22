package com.example.qualmeuendereco

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.qualmeuendereco.databinding.ActivityMainBinding
import com.example.qualmeuendereco.viewmodels.MainViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val activityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val mainViewModel by viewModels<MainViewModel>()

    private lateinit var cepField: EditText
    private lateinit var searchButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        searchButton = activityMainBinding.searchButton
        cepField = activityMainBinding.cepField

        searchButton.setOnClickListener {
            val cep = cepField.text.toString()
            if (cep.isNotEmpty()) {
                mainViewModel.consultaCep(cep)
            }
        }

        MainScope().launch {
            mainViewModel.cep.collect {
                it?.let { endereco ->
                    Log.d("Endereco", endereco.toString())
                } ?: Toast.makeText(this@MainActivity, "CEP não encontrado", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        MainScope().launch {
            mainViewModel.error.collect {
                Toast.makeText(this@MainActivity, "Erro ao consultar CEP", Toast.LENGTH_SHORT)
                    .show()

                it?.let { error ->
                    Log.e("Error", error.message.toString())
                }
            }
        }
    }
}