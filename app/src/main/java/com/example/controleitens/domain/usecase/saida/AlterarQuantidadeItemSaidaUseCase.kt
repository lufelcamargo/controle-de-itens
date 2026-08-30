package com.example.controleitens.domain.usecase.saida

import com.example.controleitens.domain.model.StatusSaida
import com.example.controleitens.domain.repository.ItemSaidaRepository
import com.example.controleitens.domain.repository.SaidaRepository

class AlterarQuantidadeItemSaidaUseCase(
    private val saidaRepository: SaidaRepository,
    private val itemSaidaRepository: ItemSaidaRepository
) {

    suspend operator fun invoke(
        itemSaidaId: String,
        novaQuantidade: Int
    ) {

        if (novaQuantidade <= 0) {
            throw IllegalArgumentException(
                "A quantidade deve ser maior que zero."
            )
        }

        val itemSaida = itemSaidaRepository.buscarPorId(itemSaidaId)

        if (itemSaida == null) {
            throw IllegalArgumentException(
                "Item da saída não encontrado."
            )
        }

        val saida = saidaRepository.buscarPorId(itemSaida.saidaId)

        if (saida == null) {
            throw IllegalArgumentException(
                "Saída não encontrada."
            )
        }

        if (saida.status != StatusSaida.EM_ANDAMENTO) {
            throw IllegalArgumentException(
                "Não é possível alterar itens de uma saída finalizada."
            )
        }

        val itemSaidaAtualizado = itemSaida.copy(
            quantidade = novaQuantidade
        )

        itemSaidaRepository.editar(itemSaidaAtualizado)
    }
}