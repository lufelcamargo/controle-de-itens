package com.example.controleitens.domain.repository

import com.example.controleitens.domain.model.Item

interface ItemRepository {

    suspend fun cadastrar(item: Item)

    suspend fun buscarAtivos(): List<Item>

    suspend fun buscarPorId(id: String): Item?

    suspend fun editar(item: Item)

    suspend fun excluir(id: String)
}