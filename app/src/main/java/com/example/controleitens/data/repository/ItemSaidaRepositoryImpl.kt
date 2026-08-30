package com.example.controleitens.data.local.repository

import com.example.controleitens.data.local.dao.ItemSaidaDao
import com.example.controleitens.data.local.entity.ItemSaidaEntity
import com.example.controleitens.domain.model.ItemSaida
import com.example.controleitens.domain.repository.ItemSaidaRepository

class ItemSaidaRepositoryImpl(
    private val itemSaidaDao: ItemSaidaDao
) : ItemSaidaRepository {

    override suspend fun adicionar(itemSaida: ItemSaida) {
        itemSaidaDao.inserir(itemSaida.toEntity())
    }

    override suspend fun editar(itemSaida: ItemSaida) {
        itemSaidaDao.atualizar(itemSaida.toEntity())
    }

    override suspend fun excluir(id: String) {
        itemSaidaDao.excluir(id)
    }

    override suspend fun excluirPorSaidaId(saidaId: String) {
        itemSaidaDao.excluirPorSaidaId(saidaId)
    }

    override suspend fun buscarPorId(id: String): ItemSaida? {
        return itemSaidaDao.buscarPorId(id)?.toDomain()
    }

    override suspend fun buscarPorSaidaId(saidaId: String): List<ItemSaida> {
        return itemSaidaDao.buscarPorSaidaId(saidaId)
            .map { it.toDomain() }
    }

    private fun ItemSaida.toEntity(): ItemSaidaEntity {
        return ItemSaidaEntity(
            id = id,
            saidaId = saidaId,
            itemId = itemId,
            nomeItem = nomeItem,
            quantidade = quantidade,
            conferido = conferido,
            faltando = faltando
        )
    }

    private fun ItemSaidaEntity.toDomain(): ItemSaida {
        return ItemSaida(
            id = id,
            saidaId = saidaId,
            itemId = itemId,
            nomeItem = nomeItem,
            quantidade = quantidade,
            conferido = conferido,
            faltando = faltando
        )
    }
}