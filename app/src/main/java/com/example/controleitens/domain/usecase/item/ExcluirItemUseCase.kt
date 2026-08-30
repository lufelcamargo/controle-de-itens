package com.example.controleitens.domain.usecase.item

import com.example.controleitens.domain.repository.ItemRepository

class ExcluirItemUseCase(
    private val itemRepository: ItemRepository
) {

    suspend operator fun invoke(itemId: String) {

        val item = itemRepository.buscarPorId(itemId)

        if (item == null) {
            throw IllegalArgumentException("Item não encontrado.")
        }

        itemRepository.excluir(itemId)
    }
}