package com.example.controleitens.domain.usecase.item

import com.example.controleitens.domain.model.Itemdir /a
import com.example.controleitens.domain.repository.ItemRepository

class CadastrarItemUseCase(
    private val itemRepository: ItemRepository
) {

    suspend operator fun invoke(item: Item) {

        val itensAtivos = itemRepository.buscarAtivos()

        val nomeJaExiste = itensAtivos.any {
            it.nome.equals(item.nome, ignoreCase = true)
        }

        if (nomeJaExiste) {
            throw IllegalArgumentException("Já existe um item com esse nome.")
        }

        itemRepository.cadastrar(item)
    }
}