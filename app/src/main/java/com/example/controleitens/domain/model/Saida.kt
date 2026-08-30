package com.example.controleitens.domain.model

data class Saida(
    val id: String,
    val titulo: String,
    val dataCriacao: Long,
    val status: StatusSaida
)