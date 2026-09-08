package com.example.controleitens.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(
    name = "user_preferences"
)

class UserPreferences(
    private val context: Context
) {

    private object Keys {
        val NOME_USUARIO = stringPreferencesKey("nome_usuario")
    }

    val nomeUsuario: Flow<String?> =
        context.dataStore.data.map { preferences ->
            preferences[Keys.NOME_USUARIO]
        }

    suspend fun salvarNomeUsuario(nome: String) {
        context.dataStore.edit { preferences ->
            preferences[Keys.NOME_USUARIO] = nome
        }
    }
}