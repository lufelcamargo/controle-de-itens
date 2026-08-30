package com.example.controleitens.domain.usecase.saida

import com.example.controleitens.domain.model.Saida
import com.example.controleitens.domain.model.StatusSaida
import com.example.controleitens.domain.repository.SaidaRepository
import java.util.UUID

class CriarSaidaUseCase(
    private val saidaRepository: SaidaRepository
) {

    suspend operator fun invoke(titulo: String) {

        if (titulo.isBlank()) {
            throw IllegalArgumentException("O título é obrigatório.")
        }

        val saida = Saida(
            id = UUID.randomUUID().toString(),
            titulo = titulo.trim(),
            dataCriacao = System.currentTimeMillis(),
            status = StatusSaida.EM_ANDAMENTO
        )

        saidaRepository.cadastrar(saida)
    }
}