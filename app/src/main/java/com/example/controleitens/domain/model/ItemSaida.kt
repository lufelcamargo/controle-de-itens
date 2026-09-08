package com.example.controleitens.domain.model

data class ItemSaida(
    val id: String,
    val saidaId: String,
    val itemId: String,
    val nomeItem: String,
    val quantidade: Int,
    val conferido: Boolean = false
)