package com.example.controleitens.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.controleitens.domain.model.ItemSaida
import com.example.controleitens.domain.model.Saida
import com.example.controleitens.domain.model.StatusSaida
import com.example.controleitens.domain.repository.ItemSaidaRepository
import com.example.controleitens.domain.repository.SaidaRepository
import com.example.controleitens.ui.screens.ItemConfiguracao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class SaidasViewModel(
    private val saidaRepository: SaidaRepository,
    private val itemSaidaRepository: ItemSaidaRepository
) : ViewModel() {

    private val _saidas = MutableStateFlow<List<Saida>>(emptyList())
    val saidas: StateFlow<List<Saida>> = _saidas.asStateFlow()

    private val _quantidadeItens = MutableStateFlow<Map<String, Int>>(emptyMap())
    val quantidadeItens: StateFlow<Map<String, Int>> =
        _quantidadeItens.asStateFlow()

    init {
        carregarSaidas()
    }

    private fun carregarSaidas() {
        viewModelScope.launch {
            val lista = saidaRepository.buscarTodas()

            _saidas.value = lista

            val quantidades = lista.associate { saida ->
                saida.id to itemSaidaRepository
                    .buscarPorSaidaId(saida.id)
                    .size
            }

            _quantidadeItens.value = quantidades
        }
    }

    fun criarSaida(
        titulo: String,
        itens: List<ItemConfiguracao>,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            val tituloLimpo = titulo.trim()

            if (tituloLimpo.isBlank()) {
                return@launch
            }

            val saidaId = UUID.randomUUID().toString()

            val saida = Saida(
                id = saidaId,
                titulo = tituloLimpo,
                dataCriacao = System.currentTimeMillis(),
                status = StatusSaida.EM_ANDAMENTO
            )

            saidaRepository.cadastrar(saida)

            itens.forEach { item ->
                if (item.itemId.isNotBlank()) {
                    itemSaidaRepository.adicionar(
                        ItemSaida(
                            id = UUID.randomUUID().toString(),
                            saidaId = saidaId,
                            itemId = item.itemId,
                            nomeItem = item.nome,
                            quantidade = item.quantidade
                        )
                    )
                }
            }

            carregarSaidas()

            onSuccess()
        }
    }

    fun finalizarSaida(id: String) {
        viewModelScope.launch {
            val saida = saidaRepository.buscarPorId(id)

            if (saida != null && saida.status == StatusSaida.EM_ANDAMENTO) {
                saidaRepository.editar(
                    saida.copy(
                        status = StatusSaida.FINALIZADA
                    )
                )

                carregarSaidas()
            }
        }
    }

    fun excluirSaida(id: String) {
        viewModelScope.launch {
            itemSaidaRepository.excluirPorSaidaId(id)
            saidaRepository.excluir(id)

            carregarSaidas()
        }
    }
}