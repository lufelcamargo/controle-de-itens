package com.example.controleitens.domain.usecase.saida

import com.example.controleitens.domain.model.ItemSaida
import com.example.controleitens.domain.model.Saida
import com.example.controleitens.domain.model.StatusSaida
import com.example.controleitens.domain.repository.ItemModeloRepository
import com.example.controleitens.domain.repository.ItemSaidaRepository
import com.example.controleitens.domain.repository.ModeloRepository
import com.example.controleitens.domain.repository.SaidaRepository
import java.util.UUID

class CriarSaidaModeloUseCase(
    private val modeloRepository: ModeloRepository,
    private val itemModeloRepository: ItemModeloRepository,
    private val saidaRepository: SaidaRepository,
    private val itemSaidaRepository: ItemSaidaRepository
) {

    suspend operator fun invoke(
        modeloId: String
    ): String {

        val modelo = modeloRepository.buscarPorId(modeloId)
            ?: throw IllegalArgumentException("Modelo não encontrado.")

        val saidaId = UUID.randomUUID().toString()

        val saida = Saida(
            id = saidaId,
            titulo = modelo.titulo,
            dataCriacao = System.currentTimeMillis(),
            status = StatusSaida.EM_ANDAMENTO
        )

        saidaRepository.cadastrar(saida)

        val itensModelo = itemModeloRepository.buscarPorModeloId(modeloId)

        itensModelo.forEach { itemModelo ->
            itemSaidaRepository.adicionar(
                ItemSaida(
                    id = UUID.randomUUID().toString(),
                    saidaId = saidaId,
                    itemId = itemModelo.itemId,
                    nomeItem = itemModelo.nomeItem,
                    quantidade = itemModelo.quantidade
                )
            )
        }

        return saidaId
    }
}