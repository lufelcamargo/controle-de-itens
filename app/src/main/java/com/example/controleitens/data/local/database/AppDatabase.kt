package com.example.controleitens.data.local.database

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.example.controleitens.data.local.dao.ItemDao
import com.example.controleitens.data.local.entity.ItemEntity

@Database(
    entities = [ItemEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao
}