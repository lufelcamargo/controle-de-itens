package com.example.controleitens.data.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "itens_modelo")
data class ItemModeloEntity(
    @PrimaryKey
    val id: String,
    val modeloId: String,
    val itemId: String,
    val nomeItem: String,
    val quantidade: Int
)