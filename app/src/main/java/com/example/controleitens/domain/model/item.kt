package com.example.controleitens.domain.model

data class Item(
    val id: String,
    val nome: String,
    val ativo: Boolean = true
)