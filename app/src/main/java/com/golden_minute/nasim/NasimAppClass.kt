package com.golden_minute.nasim

import android.app.Application
import com.golden_minute.nasim.DI.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class NasimAppClass : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@NasimAppClass)
            modules(appModule)
        }
    }

}