package com.example.controleitens.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.controleitens.domain.repository.ItemSaidaRepository
import com.example.controleitens.domain.repository.SaidaRepository

class SaidasViewModelFactory(
    private val saidaRepository: SaidaRepository,
    private val itemSaidaRepository: ItemSaidaRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SaidasViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SaidasViewModel(
                saidaRepository,
                itemSaidaRepository
            ) as T
        }

        throw IllegalArgumentException("ViewModel desconhecido")
    }
}