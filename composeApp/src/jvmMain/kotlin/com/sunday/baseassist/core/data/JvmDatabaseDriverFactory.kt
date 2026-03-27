package com.sunday.baseassist.core.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.sunday.baseassis.ChatDatabase
import com.sunday.baseassist.core.data.source.local.sqldelight.DatabaseDriverFactory
import java.io.File

class JvmDatabaseDriverFactory: DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        val databasePath = File(System.getProperty("java.io.tmpdir"), "chat.db")
        return JdbcSqliteDriver("jdbc:sqlite:${databasePath.path}").also { driver ->
            ChatDatabase.Schema.create(driver)
        }
    }
}