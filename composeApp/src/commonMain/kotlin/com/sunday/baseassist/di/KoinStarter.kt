package com.sunday.baseassist.di

import com.sunday.data.di.coreDataModule
import com.sunday.data.di.platformModule
import di.conversationListModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

fun initKoin(appDeclaration: KoinApplication.() -> Unit = {}) {
    startKoin {
        appDeclaration()
        modules(platformModule, coreDataModule, conversationListModule)
    }
}