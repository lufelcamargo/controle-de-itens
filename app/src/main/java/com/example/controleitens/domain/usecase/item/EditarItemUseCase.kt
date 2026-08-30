package com.example.controleitens.domain.usecase.item

import com.example.controleitens.domain.model.Item
import com.example.controleitens.domain.repository.ItemRepository

class EditarItemUseCase(
    private val itemRepository: ItemRepository
) {

    suspend operator fun invoke(item: Item) {

        val itemAtual = itemRepository.buscarPorId(item.id)

        if (itemAtual == null) {
            throw IllegalArgumentException("Item não encontrado.")
        }

        val itensAtivos = itemRepository.buscarAtivos()

        val nomeJaExiste = itensAtivos.any {
            it.id != item.id &&
                    it.nome.equals(item.nome, ignoreCase = true)
        }

        if (nomeJaExiste) {
            throw IllegalArgumentException("Já existe um item com esse nome.")
        }

        itemRepository.editar(item)
    }
}