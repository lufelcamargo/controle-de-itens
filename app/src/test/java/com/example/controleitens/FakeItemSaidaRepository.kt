package com.example.controleitens

import com.example.controleitens.domain.model.ItemSaida
import com.example.controleitens.domain.repository.ItemSaidaRepository

class FakeItemSaidaRepository : ItemSaidaRepository {

    private val itens = mutableListOf<ItemSaida>()

    override suspend fun adicionar(itemSaida: ItemSaida) {
        itens.add(itemSaida)
    }

    override suspend fun buscarPorId(id: String): ItemSaida? {
        return itens.find { it.id == id }
    }

    override suspend fun buscarPorSaidaId(saidaId: String): List<ItemSaida> {
        return itens.filter { it.saidaId == saidaId }
    }

    override suspend fun editar(itemSaida: ItemSaida) {
        val indice = itens.indexOfFirst { it.id == itemSaida.id }

        if (indice != -1) {
            itens[indice] = itemSaida
        }
    }

    override suspend fun excluir(id: String) {
        itens.removeIf { it.id == id }
    }

    override suspend fun excluirPorSaidaId(saidaId: String) {
        itens.removeIf { it.saidaId == saidaId }
    }
}