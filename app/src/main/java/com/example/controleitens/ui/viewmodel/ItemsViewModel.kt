package com.example.controleitens.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.controleitens.domain.model.Item
import com.example.controleitens.domain.repository.ItemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ItemsViewModel(
    private val repository: ItemRepository
) : ViewModel() {

    private val _itens = MutableStateFlow<List<Item>>(emptyList())
    val itens: StateFlow<List<Item>> = _itens.asStateFlow()

    init {
        carregarItens()
    }

    private fun carregarItens() {
        viewModelScope.launch {
            _itens.value = repository.buscarAtivos()
        }
    }

    fun cadastrarItem(
        nome: String,
        onSuccess: (Item) -> Unit = {}
    ) {
        viewModelScope.launch {
            val item = Item(
                id = java.util.UUID.randomUUID().toString(),
                nome = nome.trim(),
                ativo = true
            )

            repository.cadastrar(item)

            _itens.value = repository.buscarAtivos()

            onSuccess(item)
        }
    }

    fun editarItem(id: String, novoNome: String) {
        viewModelScope.launch {
            val item = repository.buscarPorId(id)

            if (item != null) {
                repository.editar(
                    item.copy(
                        nome = novoNome.trim()
                    )
                )

                carregarItens()
            }
        }
    }

    fun excluirItem(id: String) {
        viewModelScope.launch {
            repository.excluir(id)
            carregarItens()
        }
    }
}