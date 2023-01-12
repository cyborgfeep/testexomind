package com.example.testexomind

import android.app.Application
import android.content.Context
import timber.log.Timber

class App : Application(){
    companion object {

        lateinit  var appContext: Context

    }
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

}