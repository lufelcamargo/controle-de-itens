package com.example.controleitens.data.local.repository

import com.example.controleitens.data.local.dao.ItemSaidaDao
import com.example.controleitens.data.local.entity.ItemSaidaEntity

class FakeItemSaidaDao : ItemSaidaDao {

    private val itens = mutableListOf<ItemSaidaEntity>()

    override suspend fun inserir(item: ItemSaidaEntity) {
        itens.add(item)
    }

    override suspend fun atualizar(item: ItemSaidaEntity) {
        val indice = itens.indexOfFirst { it.id == item.id }

        if (indice != -1) {
            itens[indice] = item
        }
    }

    override suspend fun excluir(id: String) {
        itens.removeIf { it.id == id }
    }

    override suspend fun excluirPorSaidaId(saidaId: String) {
        itens.removeIf { it.saidaId == saidaId }
    }

    override suspend fun buscarPorId(id: String): ItemSaidaEntity? {
        return itens.find { it.id == id }
    }

    override suspend fun buscarPorSaidaId(saidaId: String): List<ItemSaidaEntity> {
        return itens.filter { it.saidaId == saidaId }
    }
}