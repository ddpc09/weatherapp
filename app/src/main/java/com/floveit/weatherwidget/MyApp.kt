package com.floveit.weatherwidget

import android.app.Application
import com.floveit.weatherwidget.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger() // Optional: logs Koin resolution to Logcat
            androidContext(this@MyApp)
            modules(appModule)
        }
    }
}