package com.example.fotofun

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
public class FotoFun: Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: FotoFun? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        // initialize for any

        // Use ApplicationContext.
        // example: SharedPreferences etc...
        val context: Context = FotoFun.applicationContext()
    }
}