package com.nougust3.tasks

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.nougust3.tasks.di.app
import org.koin.android.ext.android.startKoin

@SuppressLint("Registered")
class Tasks : Application() {

    override fun onCreate() {
        super.onCreate()

        appContext = applicationContext

        startKoin(this, app)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var appContext: Context

        fun getContext(): Context {
            return appContext
        }
    }

}