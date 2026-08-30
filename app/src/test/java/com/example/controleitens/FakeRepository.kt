package com.example.controleitens

import com.example.controleitens.domain.model.Item
import com.example.controleitens.domain.repository.ItemRepository

class FakeItemRepository : ItemRepository {

    private val itens = mutableListOf<Item>()

    override suspend fun cadastrar(item: Item) {
        itens.add(item)
    }

    override suspend fun buscarAtivos(): List<Item> {
        return itens.filter { it.ativo }
    }

    override suspend fun buscarPorId(id: String): Item? {
        return itens.find { it.id == id }
    }

    override suspend fun editar(item: Item) {
        val indice = itens.indexOfFirst { it.id == item.id }

        if (indice != -1) {
            itens[indice] = item
        }
    }

    override suspend fun excluir(id: String) {
        val indice = itens.indexOfFirst { it.id == id }

        if (indice != -1) {
            itens[indice] = itens[indice].copy(ativo = false)
        }
    }
}