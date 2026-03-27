package com.sunday.data.di

import com.sunday.data.local.IosDatabaseDriverFactory
import com.sunday.data.service.IosConnectivityService
import com.sunday.data.service.IosDateFormatter
import com.sunday.data.source.local.sqldelight.DatabaseDriverFactory
import com.sunday.domain.service.connectivity.ConnectivityService
import com.sunday.domain.service.formatter.DateFormatter
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<DatabaseDriverFactory> { IosDatabaseDriverFactory() }
    single<ConnectivityService> { IosConnectivityService() }
    single<DateFormatter> { IosDateFormatter() }
}