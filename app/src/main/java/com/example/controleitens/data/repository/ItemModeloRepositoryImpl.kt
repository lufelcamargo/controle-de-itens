package com.example.controleitens.data.local.repository

import com.example.controleitens.data.local.dao.ItemModeloDao
import com.example.controleitens.data.local.entity.ItemModeloEntity
import com.example.controleitens.domain.model.ItemModelo
import com.example.controleitens.domain.repository.ItemModeloRepository

class ItemModeloRepositoryImpl(
    private val itemModeloDao: ItemModeloDao
) : ItemModeloRepository {

    override suspend fun adicionar(itemModelo: ItemModelo) {
        itemModeloDao.inserir(itemModelo.toEntity())
    }

    override suspend fun editar(itemModelo: ItemModelo) {
        itemModeloDao.atualizar(itemModelo.toEntity())
    }

    override suspend fun excluir(id: String) {
        val itemModelo = itemModeloDao.buscarPorId(id)

        if (itemModelo != null) {
            itemModeloDao.excluir(itemModelo)
        }
    }

    override suspend fun excluirPorModeloId(modeloId: String) {
        itemModeloDao.excluirPorModeloId(modeloId)
    }

    override suspend fun buscarPorId(id: String): ItemModelo? {
        return itemModeloDao.buscarPorId(id)?.toDomain()
    }

    override suspend fun buscarPorModeloId(modeloId: String): List<ItemModelo> {
        return itemModeloDao.buscarPorModeloId(modeloId)
            .map { it.toDomain() }
    }

    private fun ItemModelo.toEntity(): ItemModeloEntity {
        return ItemModeloEntity(
            id = id,
            modeloId = modeloId,
            itemId = itemId,
            nomeItem = nomeItem,
            quantidade = quantidade
        )
    }

    private fun ItemModeloEntity.toDomain(): ItemModelo {
        return ItemModelo(
            id = id,
            modeloId = modeloId,
            itemId = itemId,
            nomeItem = nomeItem,
            quantidade = quantidade
        )
    }
}