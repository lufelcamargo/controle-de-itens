package com.example.controleitens

import com.example.controleitens.domain.model.Saida
import com.example.controleitens.domain.repository.SaidaRepository

class FakeSaidaRepository : SaidaRepository {

    private val saidas = mutableListOf<Saida>()

    override suspend fun cadastrar(saida: Saida) {
        saidas.add(saida)
    }

    override suspend fun buscarPorId(id: String): Saida? {
        return saidas.find { it.id == id }
    }

    override suspend fun buscarTodas(): List<Saida> {
        return saidas.toList()
    }

    override suspend fun editar(saida: Saida) {
        val indice = saidas.indexOfFirst { it.id == saida.id }

        if (indice != -1) {
            saidas[indice] = saida
        }
    }

    override suspend fun excluir(id: String) {
        saidas.removeIf { it.id == id }
    }
}