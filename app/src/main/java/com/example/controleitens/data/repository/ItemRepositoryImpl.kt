package com.example.controleitens.data.local.repository

import com.example.controleitens.data.local.dao.ItemDao
import com.example.controleitens.data.local.entity.ItemEntity
import com.example.controleitens.domain.model.Item
import com.example.controleitens.domain.repository.ItemRepository

class ItemRepositoryImpl(
    private val itemDao: ItemDao
) : ItemRepository {

    override suspend fun cadastrar(item: Item) {
        itemDao.inserir(item.toEntity())
    }

    override suspend fun editar(item: Item) {
        itemDao.atualizar(item.toEntity())
    }

    override suspend fun excluir(item: Item) {
        itemDao.atualizar(
            item.toEntity().copy(ativo = false)
        )
    }

    override suspend fun buscarPorId(id: String): Item? {
        return itemDao.buscarPorId(id)?.toDomain()
    }

    override suspend fun buscarAtivos(): List<Item> {
        return itemDao.buscarAtivos().map { it.toDomain() }
    }

    private fun Item.toEntity(): ItemEntity {
        return ItemEntity(
            id = id,
            nome = nome,
            ativo = ativo
        )
    }

    private fun ItemEntity.toDomain(): Item {
        return Item(
            id = id,
            nome = nome,
            ativo = ativo
        )
    }
}