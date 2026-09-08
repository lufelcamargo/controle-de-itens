package com.example.controleitens.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.controleitens.data.preferences.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class UserPreferencesViewModel(
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _nomeUsuario = MutableStateFlow<String?>(null)
    val nomeUsuario: StateFlow<String?> = _nomeUsuario.asStateFlow()

    private val _carregando = MutableStateFlow(true)
    val carregando: StateFlow<Boolean> = _carregando.asStateFlow()

    init {
        carregarNome()
    }

    private fun carregarNome() {
        viewModelScope.launch {
            _nomeUsuario.value = userPreferences.nomeUsuario.first()
            _carregando.value = false
        }
    }

    fun salvarNome(nome: String) {
        viewModelScope.launch {
            val nomeLimpo = nome.trim()

            if (nomeLimpo.isBlank()) return@launch

            userPreferences.salvarNomeUsuario(nomeLimpo)
            _nomeUsuario.value = nomeLimpo
        }
    }
}