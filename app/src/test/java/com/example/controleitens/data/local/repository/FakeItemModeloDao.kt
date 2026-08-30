package com.example.controleitens.data.local.repository

import com.example.controleitens.data.local.dao.ItemModeloDao
import com.example.controleitens.data.local.entity.ItemModeloEntity

class FakeItemModeloDao : ItemModeloDao {

    private val itens = mutableListOf<ItemModeloEntity>()

    override suspend fun inserir(itemModelo: ItemModeloEntity) {
        itens.add(itemModelo)
    }

    override suspend fun atualizar(itemModelo: ItemModeloEntity) {
        val indice = itens.indexOfFirst { it.id == itemModelo.id }

        if (indice != -1) {
            itens[indice] = itemModelo
        }
    }

    override suspend fun excluir(itemModelo: ItemModeloEntity) {
        itens.removeIf { it.id == itemModelo.id }
    }

    override suspend fun excluirPorModeloId(modeloId: String) {
        itens.removeIf { it.modeloId == modeloId }
    }

    override suspend fun buscarPorId(id: String): ItemModeloEntity? {
        return itens.find { it.id == id }
    }

    override suspend fun buscarPorModeloId(modeloId: String): List<ItemModeloEntity> {
        return itens.filter { it.modeloId == modeloId }
    }
}