package com.example.controleitens.domain.repository

import com.example.controleitens.domain.model.Saida

interface SaidaRepository {

    suspend fun cadastrar(saida: Saida)

    suspend fun buscarPorId(id: String): Saida?

    suspend fun buscarTodas(): List<Saida>

    suspend fun editar(saida: Saida)

    suspend fun excluir(id: String)
}