package com.example.cleanarchitechture

import android.app.Application
import com.example.cleanarchitechture.di.networkModule
import com.example.cleanarchitechture.di.viewModelModule
import org.koin.android.ext.android.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this

        val modules = listOf(networkModule, viewModelModule)
        startKoin(this, modules)
    }

    companion object {
        lateinit var instance: App
            private set
    }
}