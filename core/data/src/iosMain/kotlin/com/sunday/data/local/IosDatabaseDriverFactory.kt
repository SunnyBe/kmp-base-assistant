package com.sunday.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.sunday.data.source.local.sqldelight.DatabaseDriverFactory

class IosDatabaseDriverFactory : DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        return NativeSqliteDriver(ChatDatabase.Schema, "chat.db")
    }
}