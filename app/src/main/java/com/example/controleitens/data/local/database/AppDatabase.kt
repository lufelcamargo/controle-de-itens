package com.example.controleitens.data.local.database

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.example.controleitens.data.local.dao.ItemDao
import com.example.controleitens.data.local.dao.ItemModeloDao
import com.example.controleitens.data.local.dao.ItemSaidaDao
import com.example.controleitens.data.local.dao.ModeloDao
import com.example.controleitens.data.local.dao.SaidaDao
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
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
    abstract fun saidaDao(): SaidaDao
    abstract fun itemSaidaDao(): ItemSaidaDao
    abstract fun modeloDao(): ModeloDao
    abstract fun itemModeloDao(): ItemModeloDao
}