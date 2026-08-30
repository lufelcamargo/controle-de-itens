package com.example.controleitens.data.local.database

import android.content.Context
import androidx.room3.Room
import androidx.sqlite.driver.AndroidSQLiteDriver

object DatabaseProvider {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder<AppDatabase>(
                context.applicationContext,
                "controle_itens.db"
            )
                .setDriver(AndroidSQLiteDriver())
                .build()
                .also {
                    INSTANCE = it
                }
        }
    }
}