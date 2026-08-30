package com.example.controleitens.domain.usecase.item

import com.example.controleitens.domain.model.Item
import com.example.controleitens.domain.repository.ItemRepository

class ListarItensUseCase(
    private val itemRepository: ItemRepository
) {

    suspend operator fun invoke(): List<Item> {
        return itemRepository.buscarAtivos()
    }
}