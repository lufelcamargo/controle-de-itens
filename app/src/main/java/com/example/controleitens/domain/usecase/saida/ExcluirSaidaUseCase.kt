package com.example.controleitens.domain.usecase.saida

import com.example.controleitens.domain.repository.ItemSaidaRepository
import com.example.controleitens.domain.repository.SaidaRepository

class ExcluirSaidaUseCase(
    private val saidaRepository: SaidaRepository,
    private val itemSaidaRepository: ItemSaidaRepository
) {

    suspend operator fun invoke(saidaId: String) {

        val saida = saidaRepository.buscarPorId(saidaId)
            ?: throw IllegalArgumentException("Saída não encontrada.")

        itemSaidaRepository.excluirPorSaidaId(saida.id)
        saidaRepository.excluir(saida.id)
    }
}