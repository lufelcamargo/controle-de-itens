package com.example.controleitens.domain.repository

import com.example.controleitens.domain.model.ItemModelo

interface ItemModeloRepository {

    suspend fun adicionar(itemModelo: ItemModelo)

    suspend fun buscarPorId(id: String): ItemModelo?

    suspend fun buscarPorModeloId(modeloId: String): List<ItemModelo>

    suspend fun editar(itemModelo: ItemModelo)

    suspend fun excluir(id: String)

    suspend fun excluirPorModeloId(modeloId: String)
}