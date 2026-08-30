package com.example.controleitens.domain.usecase.saida

import com.example.controleitens.domain.model.StatusSaida
import com.example.controleitens.domain.repository.SaidaRepository

class FinalizarSaidaUseCase(
    private val saidaRepository: SaidaRepository
) {

    suspend operator fun invoke(saidaId: String) {

        val saida = saidaRepository.buscarPorId(saidaId)
            ?: throw IllegalArgumentException("Saída não encontrada.")

        if (saida.status != StatusSaida.EM_ANDAMENTO) {
            throw IllegalArgumentException(
                "A saída já está finalizada."
            )
        }

        saidaRepository.editar(
            saida.copy(
                status = StatusSaida.FINALIZADA
            )
        )
    }
}