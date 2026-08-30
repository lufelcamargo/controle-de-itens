package com.example.controleitens.domain.usecase

import android.content.Context
import com.example.controleitens.data.local.repository.RepositoryProvider
import com.example.controleitens.domain.usecase.item.CadastrarItemUseCase
import com.example.controleitens.domain.usecase.item.EditarItemUseCase
import com.example.controleitens.domain.usecase.item.ExcluirItemUseCase
import com.example.controleitens.domain.usecase.saida.ConferirItemUseCase

import com.example.controleitens.domain.usecase.modelo.CriarModeloUseCase

import com.example.controleitens.domain.usecase.saida.AdicionarItemSaidaUseCase
import com.example.controleitens.domain.usecase.saida.AlterarQuantidadeItemSaidaUseCase
import com.example.controleitens.domain.usecase.saida.ConsultarHistoricoUseCase
import com.example.controleitens.domain.usecase.saida.CriarSaidaModeloUseCase
import com.example.controleitens.domain.usecase.saida.CriarSaidaUseCase
import com.example.controleitens.domain.usecase.saida.EditarNomeItemSaidaUseCase
import com.example.controleitens.domain.usecase.saida.EditarSaidaUseCase
import com.example.controleitens.domain.usecase.saida.ExcluirSaidaUseCase
import com.example.controleitens.domain.usecase.saida.FinalizarSaidaUseCase
import com.example.controleitens.domain.usecase.saida.ListarSaidasUseCase
import com.example.controleitens.domain.usecase.saida.MarcarItemComoFaltandoUseCase
import com.example.controleitens.domain.usecase.saida.RemoverItemSaidaUseCase

import com.example.controleitens.domain.usecase.item.ListarItensUseCase

object UseCaseProvider {

    fun cadastrarItem(context: Context): CadastrarItemUseCase {
        return CadastrarItemUseCase(
            RepositoryProvider.itemRepository(context)
        )
    }

    fun editarItem(context: Context): EditarItemUseCase {
        return EditarItemUseCase(
            RepositoryProvider.itemRepository(context)
        )
    }

    fun excluirItem(context: Context): ExcluirItemUseCase {
        return ExcluirItemUseCase(
            RepositoryProvider.itemRepository(context)
        )
    }

    fun conferirItem(context: Context): ConferirItemUseCase {
        return ConferirItemUseCase(
            RepositoryProvider.saidaRepository(context),
            RepositoryProvider.itemSaidaRepository(context)
        )
    }

    fun criarModelo(context: Context): CriarModeloUseCase {
        return CriarModeloUseCase(
            RepositoryProvider.modeloRepository(context)
        )
    }

    fun criarSaida(context: Context): CriarSaidaUseCase {
        return CriarSaidaUseCase(
            RepositoryProvider.saidaRepository(context)
        )
    }

    fun editarSaida(context: Context): EditarSaidaUseCase {
        return EditarSaidaUseCase(
            RepositoryProvider.saidaRepository(context)
        )
    }

    fun excluirSaida(context: Context): ExcluirSaidaUseCase {
        return ExcluirSaidaUseCase(
            RepositoryProvider.saidaRepository(context),
            RepositoryProvider.itemSaidaRepository(context)
        )
    }

    fun finalizarSaida(context: Context): FinalizarSaidaUseCase {
        return FinalizarSaidaUseCase(
            RepositoryProvider.saidaRepository(context)
        )
    }

    fun listarSaidas(context: Context): ListarSaidasUseCase {
        return ListarSaidasUseCase(
            RepositoryProvider.saidaRepository(context)
        )
    }

    fun editarNomeItemSaida(context: Context): EditarNomeItemSaidaUseCase {
        return EditarNomeItemSaidaUseCase(
            RepositoryProvider.saidaRepository(context),
            RepositoryProvider.itemSaidaRepository(context)
        )
    }

    fun marcarItemComoFaltando(context: Context): MarcarItemComoFaltandoUseCase {
        return MarcarItemComoFaltandoUseCase(
            RepositoryProvider.saidaRepository(context),
            RepositoryProvider.itemSaidaRepository(context)
        )
    }

    fun removerItemSaida(context: Context): RemoverItemSaidaUseCase {
        return RemoverItemSaidaUseCase(
            RepositoryProvider.saidaRepository(context),
            RepositoryProvider.itemSaidaRepository(context)
        )
    }

    fun adicionarItemSaida(context: Context): AdicionarItemSaidaUseCase {
        return AdicionarItemSaidaUseCase(
            RepositoryProvider.itemRepository(context),
            RepositoryProvider.saidaRepository(context),
            RepositoryProvider.itemSaidaRepository(context)
        )
    }

    fun alterarQuantidadeItemSaida(
        context: Context
    ): AlterarQuantidadeItemSaidaUseCase {
        return AlterarQuantidadeItemSaidaUseCase(
            RepositoryProvider.saidaRepository(context),
            RepositoryProvider.itemSaidaRepository(context)
        )
    }

    fun consultarHistorico(context: Context): ConsultarHistoricoUseCase {
        return ConsultarHistoricoUseCase(
            RepositoryProvider.saidaRepository(context)
        )
    }

    fun criarSaidaModelo(context: Context): CriarSaidaModeloUseCase {
        return CriarSaidaModeloUseCase(
            RepositoryProvider.modeloRepository(context),
            RepositoryProvider.itemModeloRepository(context),
            RepositoryProvider.saidaRepository(context),
            RepositoryProvider.itemSaidaRepository(context)
        )
    }

    fun listarItens(context: Context): ListarItensUseCase {
        return ListarItensUseCase(
            RepositoryProvider.itemRepository(context)
        )
    }
}