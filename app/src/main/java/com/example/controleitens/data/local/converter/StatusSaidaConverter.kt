package com.example.controleitens.data.local.converter

import androidx.room3.ColumnTypeConverter
import com.example.controleitens.domain.model.StatusSaida

class StatusSaidaConverter {

    @ColumnTypeConverter
    fun fromStatus(status: StatusSaida): String {
        return status.name
    }

    @ColumnTypeConverter
    fun toStatus(value: String): StatusSaida {
        return StatusSaida.valueOf(value)
    }
}