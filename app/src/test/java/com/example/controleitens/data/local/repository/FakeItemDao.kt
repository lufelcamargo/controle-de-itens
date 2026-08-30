package com.example.controleitens.data.local.repository

import com.example.controleitens.data.local.dao.ItemDao
import com.example.controleitens.data.local.entity.ItemEntity

class FakeItemDao : ItemDao {

    private val itens = mutableListOf<ItemEntity>()

    override suspend fun inserir(item: ItemEntity) {
        itens.add(item)
    }

    override suspend fun atualizar(item: ItemEntity) {
        val indice = itens.indexOfFirst { it.id == item.id }

        if (indice != -1) {
            itens[indice] = item
        }
    }

    override suspend fun desativar(id: String) {
        val indice = itens.indexOfFirst { it.id == id }

        if (indice != -1) {
            itens[indice] = itens[indice].copy(ativo = false)
        }
    }

    override suspend fun buscarPorId(id: String): ItemEntity? {
        return itens.find { it.id == id }
    }

    override suspend fun buscarAtivos(): List<ItemEntity> {
        return itens.filter { it.ativo }
    }
}