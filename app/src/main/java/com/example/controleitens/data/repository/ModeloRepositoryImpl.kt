package com.example.controleitens.data.local.repository

import com.example.controleitens.data.local.dao.ModeloDao
import com.example.controleitens.data.local.entity.ModeloEntity
import com.example.controleitens.domain.model.Modelo
import com.example.controleitens.domain.repository.ModeloRepository

class ModeloRepositoryImpl(
    private val modeloDao: ModeloDao
) : ModeloRepository {

    override suspend fun cadastrar(modelo: Modelo) {
        modeloDao.inserir(modelo.toEntity())
    }

    override suspend fun editar(modelo: Modelo) {
        modeloDao.atualizar(modelo.toEntity())
    }

    override suspend fun excluir(id: String) {
        val modelo = modeloDao.buscarPorId(id)

        if (modelo != null) {
            modeloDao.excluir(modelo)
        }
    }

    override suspend fun buscarPorId(id: String): Modelo? {
        return modeloDao.buscarPorId(id)?.toDomain()
    }

    override suspend fun buscarTodas(): List<Modelo> {
        return modeloDao.buscarTodas().map { it.toDomain() }
    }

    private fun Modelo.toEntity(): ModeloEntity {
        return ModeloEntity(
            id = id,
            titulo = titulo
        )
    }

    private fun ModeloEntity.toDomain(): Modelo {
        return Modelo(
            id = id,
            titulo = titulo
        )
    }
}