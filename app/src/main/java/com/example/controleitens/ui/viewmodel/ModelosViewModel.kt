package com.example.controleitens.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.controleitens.domain.model.ItemModelo
import com.example.controleitens.domain.model.Modelo
import com.example.controleitens.domain.repository.ItemModeloRepository
import com.example.controleitens.domain.repository.ModeloRepository
import com.example.controleitens.ui.screens.ItemConfiguracao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class ModelosViewModel(
    private val modeloRepository: ModeloRepository,
    private val itemModeloRepository: ItemModeloRepository
) : ViewModel() {

    private val _modelos = MutableStateFlow<List<Modelo>>(emptyList())
    val modelos: StateFlow<List<Modelo>> = _modelos.asStateFlow()

    init {
        carregarModelos()
    }

    private fun carregarModelos() {
        viewModelScope.launch {
            _modelos.value = modeloRepository.buscarTodas()
        }
    }

    fun criarModelo(
        titulo: String,
        itens: List<ItemConfiguracao>,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            val tituloLimpo = titulo.trim()

            if (tituloLimpo.isBlank()) return@launch

            val modeloId = UUID.randomUUID().toString()

            val modelo = Modelo(
                id = modeloId,
                titulo = tituloLimpo
            )

            modeloRepository.cadastrar(modelo)

            itens.forEach { item ->
                if (item.itemId.isNotBlank()) {
                    itemModeloRepository.adicionar(
                        ItemModelo(
                            id = UUID.randomUUID().toString(),
                            modeloId = modeloId,
                            itemId = item.itemId,
                            nomeItem = item.nome,
                            quantidade = item.quantidade
                        )
                    )
                }
            }

            carregarModelos()
            onSuccess()
        }
    }
    fun buscarItensDoModelo(
        modeloId: String,
        onResult: (List<ItemModelo>) -> Unit
    ) {
        viewModelScope.launch {
            val itens = itemModeloRepository.buscarPorModeloId(modeloId)
            onResult(itens)
        }
    }
}