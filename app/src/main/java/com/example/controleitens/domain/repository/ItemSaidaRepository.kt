package com.example.controleitens.domain.repository

import com.example.controleitens.domain.model.ItemSaida

interface ItemSaidaRepository {

    suspend fun adicionar(itemSaida: ItemSaida)

    suspend fun buscarPorId(id: String): ItemSaida?

    suspend fun buscarPorSaidaId(saidaId: String): List<ItemSaida>

    suspend fun editar(itemSaida: ItemSaida)

    suspend fun excluir(id: String)

    suspend fun excluirPorSaidaId(saidaId: String)
}