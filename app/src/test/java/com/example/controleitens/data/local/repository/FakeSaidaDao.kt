package com.example.controleitens.data.local.repository

import com.example.controleitens.data.local.dao.SaidaDao
import com.example.controleitens.data.local.entity.SaidaEntity

class FakeSaidaDao : SaidaDao {

    private val saidas = mutableListOf<SaidaEntity>()

    override suspend fun inserir(saida: SaidaEntity) {
        saidas.add(saida)
    }

    override suspend fun atualizar(saida: SaidaEntity) {
        val indice = saidas.indexOfFirst { it.id == saida.id }

        if (indice != -1) {
            saidas[indice] = saida
        }
    }

    override suspend fun excluir(id: String) {
        saidas.removeIf { it.id == id }
    }

    override suspend fun buscarPorId(id: String): SaidaEntity? {
        return saidas.find { it.id == id }
    }

    override suspend fun buscarTodas(): List<SaidaEntity> {
        return saidas.toList()
    }
}