package com.example.qualmeuendereco

import android.app.Application
import com.example.qualmeuendereco.retrofit.service

class BaseApp : Application() {
    companion object {
        lateinit var instance: BaseApp

        val apiService by lazy {
            service()
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}