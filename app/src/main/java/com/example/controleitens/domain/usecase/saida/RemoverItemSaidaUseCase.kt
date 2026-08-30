package com.example.controleitens.domain.usecase.saida

import com.example.controleitens.domain.model.StatusSaida
import com.example.controleitens.domain.repository.ItemSaidaRepository
import com.example.controleitens.domain.repository.SaidaRepository

class RemoverItemSaidaUseCase(
    private val saidaRepository: SaidaRepository,
    private val itemSaidaRepository: ItemSaidaRepository
) {

    suspend operator fun invoke(itemSaidaId: String) {

        val itemSaida = itemSaidaRepository.buscarPorId(itemSaidaId)
            ?: throw IllegalArgumentException("Item da saída não encontrado.")

        val saida = saidaRepository.buscarPorId(itemSaida.saidaId)
            ?: throw IllegalArgumentException("Saída não encontrada.")

        if (saida.status != StatusSaida.EM_ANDAMENTO) {
            throw IllegalArgumentException(
                "Não é possível remover itens de uma saída finalizada."
            )
        }

        itemSaidaRepository.excluir(itemSaidaId)
    }
}