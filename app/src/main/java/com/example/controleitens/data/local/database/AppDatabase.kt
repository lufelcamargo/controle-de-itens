package com.example.controleitens.data.local.database

import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.TypeConverters
import com.example.controleitens.data.local.converter.StatusSaidaConverter
import com.example.controleitens.data.local.dao.ItemDao
import com.example.controleitens.data.local.entity.ItemEntity
import com.example.controleitens.data.local.entity.ItemModeloEntity
import com.example.controleitens.data.local.entity.ItemSaidaEntity
import com.example.controleitens.data.local.entity.ModeloEntity
import com.example.controleitens.data.local.entity.SaidaEntity

@Database(
    entities = [
        ItemEntity::class,
        SaidaEntity::class,
        ItemSaidaEntity::class,
        ModeloEntity::class,
        ItemModeloEntity::class
    ],
    version = 1
)
@TypeConverters(StatusSaidaConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao
}