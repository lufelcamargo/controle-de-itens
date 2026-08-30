package com.example.controleitens.data.local.repository

import com.example.controleitens.data.local.dao.SaidaDao
import com.example.controleitens.data.local.entity.SaidaEntity
import com.example.controleitens.domain.model.Saida
import com.example.controleitens.domain.repository.SaidaRepository

class SaidaRepositoryImpl(
    private val saidaDao: SaidaDao
) : SaidaRepository {

    override suspend fun cadastrar(saida: Saida) {
        saidaDao.inserir(saida.toEntity())
    }

    override suspend fun editar(saida: Saida) {
        saidaDao.atualizar(saida.toEntity())
    }

    override suspend fun buscarPorId(id: String): Saida? {
        return saidaDao.buscarPorId(id)?.toDomain()
    }

    override suspend fun buscarTodas(): List<Saida> {
        return saidaDao.buscarTodas().map { it.toDomain() }
    }

    override suspend fun excluir(id: String) {
        saidaDao.excluir(id)
    }

    private fun Saida.toEntity(): SaidaEntity {
        return SaidaEntity(
            id = id,
            titulo = titulo,
            dataCriacao = dataCriacao,
            status = status
        )
    }

    private fun SaidaEntity.toDomain(): Saida {
        return Saida(
            id = id,
            titulo = titulo,
            dataCriacao = dataCriacao,
            status = status
        )
    }
}