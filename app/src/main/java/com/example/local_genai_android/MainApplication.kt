package com.example.local_genai_android

import android.app.Application
import com.example.local_genai_android.shared.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidLogger()
            androidContext(this@MainApplication)
//            modules(appModule)
        }
    }
}