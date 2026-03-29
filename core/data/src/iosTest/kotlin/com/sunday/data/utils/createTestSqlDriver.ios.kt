package com.sunday.data.utils

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.sunday.data.local.ChatDatabase

internal actual fun createTestSqlDriver(): SqlDriver {
    return NativeSqliteDriver(ChatDatabase.Schema, "test_db.db")
}