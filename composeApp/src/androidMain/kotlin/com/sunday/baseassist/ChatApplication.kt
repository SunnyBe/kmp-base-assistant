package com.sunday.baseassist

import android.app.Application
import com.sunday.baseassist.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class ChatApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin{
            androidLogger(Level.INFO) // Android Logger
            androidContext(this@ChatApplication) // Binding app context
        }
    }
}