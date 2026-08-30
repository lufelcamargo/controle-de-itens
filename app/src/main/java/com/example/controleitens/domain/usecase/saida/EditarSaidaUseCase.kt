package com.example.controleitens.domain.usecase.saida

import com.example.controleitens.domain.model.StatusSaida
import com.example.controleitens.domain.repository.SaidaRepository

class EditarSaidaUseCase(
    private val saidaRepository: SaidaRepository
) {

    suspend operator fun invoke(
        saidaId: String,
        novoTitulo: String
    ) {
        if (novoTitulo.isBlank()) {
            throw IllegalArgumentException("O título é obrigatório.")
        }

        val saida = saidaRepository.buscarPorId(saidaId)
            ?: throw IllegalArgumentException("Saída não encontrada.")

        if (saida.status != StatusSaida.EM_ANDAMENTO) {
            throw IllegalArgumentException(
                "Não é possível editar uma saída finalizada."
            )
        }

        saidaRepository.editar(
            saida.copy(
                titulo = novoTitulo.trim()
            )
        )
    }
}