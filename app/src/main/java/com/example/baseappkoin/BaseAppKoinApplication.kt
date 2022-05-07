package com.example.baseappkoin

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.bumptech.glide.Glide
import com.example.baseappkoin.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class BaseAppKoinApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        context = this
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@BaseAppKoinApplication)
            modules(
                listOf(
                    sharedPreferencesModule,
                    roomDatabaseModule,
                    networkModule,
                    viewModelModule,
                    repositoryModule
                )
            )
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()

        Glide.get(this).clearMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        Glide.get(this).trimMemory(level)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }
}