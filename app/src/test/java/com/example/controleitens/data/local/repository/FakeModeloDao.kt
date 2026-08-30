package com.example.controleitens.data.local.repository

import com.example.controleitens.data.local.dao.ModeloDao
import com.example.controleitens.data.local.entity.ModeloEntity

class FakeModeloDao : ModeloDao {

    private val modelos = mutableListOf<ModeloEntity>()

    override suspend fun inserir(modelo: ModeloEntity) {
        modelos.add(modelo)
    }

    override suspend fun atualizar(modelo: ModeloEntity) {
        val indice = modelos.indexOfFirst { it.id == modelo.id }

        if (indice != -1) {
            modelos[indice] = modelo
        }
    }

    override suspend fun excluir(modelo: ModeloEntity) {
        modelos.removeIf { it.id == modelo.id }
    }

    override suspend fun buscarPorId(id: String): ModeloEntity? {
        return modelos.find { it.id == id }
    }

    override suspend fun buscarTodas(): List<ModeloEntity> {
        return modelos.toList()
    }
}