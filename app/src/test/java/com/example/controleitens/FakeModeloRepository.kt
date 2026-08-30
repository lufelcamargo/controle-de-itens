package com.example.controleitens

import com.example.controleitens.domain.model.Modelo
import com.example.controleitens.domain.repository.ModeloRepository

class FakeModeloRepository : ModeloRepository {

    private val modelos = mutableListOf<Modelo>()

    override suspend fun cadastrar(modelo: Modelo) {
        modelos.add(modelo)
    }

    override suspend fun buscarPorId(id: String): Modelo? {
        return modelos.find { it.id == id }
    }

    override suspend fun buscarTodas(): List<Modelo> {
        return modelos.toList()
    }

    override suspend fun editar(modelo: Modelo) {
        val indice = modelos.indexOfFirst { it.id == modelo.id }

        if (indice != -1) {
            modelos[indice] = modelo
        }
    }

    override suspend fun excluir(id: String) {
        modelos.removeIf { it.id == id }
    }
}