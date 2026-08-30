package com.example.controleitens.data.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.example.controleitens.domain.model.StatusSaida

@Entity(tableName = "saidas")
data class SaidaEntity(
    @PrimaryKey
    val id: String,
    val titulo: String,
    val dataCriacao: Long,
    val status: StatusSaida
)