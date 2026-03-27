package com.sunday.data.di

import com.sunday.data.service.connectivity.AndroidConnectivityService
import com.sunday.data.service.formatter.AndroidDateFormatter
import com.sunday.data.local.AndroidDatabaseDriverFactory
import com.sunday.data.source.local.sqldelight.DatabaseDriverFactory
import com.sunday.domain.service.connectivity.ConnectivityService
import com.sunday.domain.service.formatter.DateFormatter
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<DatabaseDriverFactory> { AndroidDatabaseDriverFactory(get()) }
    single<ConnectivityService> { AndroidConnectivityService(get()) }
    single<DateFormatter> { AndroidDateFormatter() }
}