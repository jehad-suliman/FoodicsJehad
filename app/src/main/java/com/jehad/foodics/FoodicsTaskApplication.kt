package com.jehad.foodics

import android.app.Application
import com.jehad.foodics.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class FoodicsTaskApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@FoodicsTaskApplication)
            modules(appModule)
        }
    }
}