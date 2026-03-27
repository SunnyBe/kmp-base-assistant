package com.sunday.baseassist.di

import com.sunday.baseassist.core.data.JvmConnectivityService
import com.sunday.baseassist.core.data.JvmDatabaseDriverFactory
import com.sunday.baseassist.core.data.JvmDateFormatter
import com.sunday.baseassist.core.data.source.local.sqldelight.DatabaseDriverFactory
import com.sunday.baseassist.core.domain.service.connectivity.ConnectivityService
import com.sunday.baseassist.core.domain.service.formatter.DateFormatter
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<DatabaseDriverFactory> { JvmDatabaseDriverFactory() }
    single<ConnectivityService> { JvmConnectivityService() }
    single<DateFormatter> { JvmDateFormatter() }
}