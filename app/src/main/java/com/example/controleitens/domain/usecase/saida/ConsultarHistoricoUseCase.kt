package com.example.controleitens.domain.usecase.saida

import com.example.controleitens.domain.model.Saida
import com.example.controleitens.domain.repository.SaidaRepository

class ConsultarHistoricoUseCase(
    private val saidaRepository: SaidaRepository
) {

    suspend operator fun invoke(): List<Saida> {
        return saidaRepository.buscarTodas()
            .filter { it.status == com.example.controleitens.domain.model.StatusSaida.FINALIZADA }
            .sortedByDescending { it.dataCriacao }
    }
}