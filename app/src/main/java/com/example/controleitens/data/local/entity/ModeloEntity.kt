package com.example.controleitens.data.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "modelos")
data class ModeloEntity(
    @PrimaryKey
    val id: String,
    val titulo: String
)