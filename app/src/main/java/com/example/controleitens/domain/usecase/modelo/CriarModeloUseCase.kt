package com.example.controleitens.domain.usecase.modelo

import com.example.controleitens.domain.model.Modelo
import com.example.controleitens.domain.repository.ModeloRepository
import java.util.UUID

class CriarModeloUseCase(
    private val modeloRepository: ModeloRepository
) {

    suspend operator fun invoke(titulo: String) {

        if (titulo.isBlank()) {
            throw IllegalArgumentException("O título é obrigatório.")
        }

        val modelo = Modelo(
            id = UUID.randomUUID().toString(),
            titulo = titulo.trim()
        )

        modeloRepository.cadastrar(modelo)
    }
}