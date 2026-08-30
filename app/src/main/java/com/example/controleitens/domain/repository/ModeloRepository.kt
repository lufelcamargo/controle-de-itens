package com.example.controleitens.domain.repository

import com.example.controleitens.domain.model.Modelo

interface ModeloRepository {

    suspend fun cadastrar(modelo: Modelo)

    suspend fun buscarPorId(id: String): Modelo?

    suspend fun buscarTodas(): List<Modelo>

    suspend fun editar(modelo: Modelo)

    suspend fun excluir(id: String)
}