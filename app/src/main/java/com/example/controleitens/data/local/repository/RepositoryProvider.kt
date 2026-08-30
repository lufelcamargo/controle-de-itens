package com.example.controleitens.data.local.repository

import android.content.Context
import com.example.controleitens.data.local.database.DatabaseProvider

object RepositoryProvider {

    fun itemRepository(context: Context): ItemRepositoryImpl {
        return ItemRepositoryImpl(
            DatabaseProvider.getDatabase(context).itemDao()
        )
    }

    fun saidaRepository(context: Context): SaidaRepositoryImpl {
        return SaidaRepositoryImpl(
            DatabaseProvider.getDatabase(context).saidaDao()
        )
    }

    fun itemSaidaRepository(context: Context): ItemSaidaRepositoryImpl {
        return ItemSaidaRepositoryImpl(
            DatabaseProvider.getDatabase(context).itemSaidaDao()
        )
    }

    fun modeloRepository(context: Context): ModeloRepositoryImpl {
        return ModeloRepositoryImpl(
            DatabaseProvider.getDatabase(context).modeloDao()
        )
    }

    fun itemModeloRepository(context: Context): ItemModeloRepositoryImpl {
        return ItemModeloRepositoryImpl(
            DatabaseProvider.getDatabase(context).itemModeloDao()
        )
    }
}