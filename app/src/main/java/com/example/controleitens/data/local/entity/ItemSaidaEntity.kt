package com.example.controleitens.data.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "itens_saida")
data class ItemSaidaEntity(
    @PrimaryKey
    val id: String,
    val saidaId: String,
    val itemId: String,
    val nomeItem: String,
    val quantidade: Int,
    val conferido: Boolean
)