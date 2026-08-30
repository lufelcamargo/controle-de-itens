package com.example.controleitens.data.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "itens")
data class ItemEntity(
    @PrimaryKey
    val id: String,

    val nome: String,

    val ativo: Boolean
)