package com.example.controleitens

import com.example.controleitens.domain.model.ItemModelo
import com.example.controleitens.domain.repository.ItemModeloRepository

class FakeItemModeloRepository : ItemModeloRepository {

    private val itens = mutableListOf<ItemModelo>()

    override suspend fun adicionar(itemModelo: ItemModelo) {
        itens.add(itemModelo)
    }

    override suspend fun buscarPorId(id: String): ItemModelo? {
        return itens.find { it.id == id }
    }

    override suspend fun buscarPorModeloId(modeloId: String): List<ItemModelo> {
        return itens.filter { it.modeloId == modeloId }
    }

    override suspend fun editar(itemModelo: ItemModelo) {
        val indice = itens.indexOfFirst { it.id == itemModelo.id }

        if (indice != -1) {
            itens[indice] = itemModelo
        }
    }

    override suspend fun excluir(id: String) {
        itens.removeIf { it.id == id }
    }

    override suspend fun excluirPorModeloId(modeloId: String) {
        itens.removeIf { it.modeloId == modeloId }
    }
}