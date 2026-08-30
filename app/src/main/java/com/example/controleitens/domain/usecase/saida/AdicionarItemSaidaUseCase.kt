package com.example.controleitens.domain.usecase.saida

import com.example.controleitens.domain.model.ItemSaida
import com.example.controleitens.domain.model.StatusSaida
import com.example.controleitens.domain.repository.ItemRepository
import com.example.controleitens.domain.repository.ItemSaidaRepository
import com.example.controleitens.domain.repository.SaidaRepository
import java.util.UUID

class AdicionarItemSaidaUseCase(
    private val itemRepository: ItemRepository,
    private val saidaRepository: SaidaRepository,
    private val itemSaidaRepository: ItemSaidaRepository
) {

    suspend operator fun invoke(
        saidaId: String,
        itemId: String,
        quantidade: Int
    ) {

        if (quantidade <= 0) {
            throw IllegalArgumentException(
                "A quantidade deve ser maior que zero."
            )
        }

        val saida = saidaRepository.buscarPorId(saidaId)

        if (saida == null) {
            throw IllegalArgumentException(
                "Saída não encontrada."
            )
        }

        if (saida.status != StatusSaida.EM_ANDAMENTO) {
            throw IllegalArgumentException(
                "Não é possível adicionar itens a uma saída finalizada."
            )
        }

        val item = itemRepository.buscarPorId(itemId)

        if (item == null) {
            throw IllegalArgumentException(
                "Item não encontrado."
            )
        }

        if (!item.ativo) {
            throw IllegalArgumentException(
                "Não é possível adicionar um item inativo."
            )
        }

        val itensDaSaida =
            itemSaidaRepository.buscarPorSaidaId(saidaId)

        val itemJaAdicionado = itensDaSaida.any {
            it.itemId == itemId
        }

        if (itemJaAdicionado) {
            throw IllegalArgumentException(
                "O item já foi adicionado a esta saída."
            )
        }

        val itemSaida = ItemSaida(
            id = UUID.randomUUID().toString(),
            saidaId = saidaId,
            itemId = itemId,
            nomeItem = item.nome,
            quantidade = quantidade
        )

        itemSaidaRepository.adicionar(itemSaida)
    }
}