package com.example.controleitens.domain.usecase.saida

import com.example.controleitens.FakeItemSaidaRepository
import com.example.controleitens.FakeSaidaRepository
import com.example.controleitens.domain.model.ItemSaida
import com.example.controleitens.domain.model.Saida
import com.example.controleitens.domain.model.StatusSaida
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ExcluirSaidaUseCaseTest {

    @Test
    fun `deve excluir saida`() = runTest {
        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = ExcluirSaidaUseCase(
            saidaRepository,
            itemSaidaRepository
        )

        saidaRepository.cadastrar(
            Saida(
                id = "1",
                titulo = "Faculdade",
                dataCriacao = System.currentTimeMillis(),
                status = StatusSaida.EM_ANDAMENTO
            )
        )

        useCase("1")

        assertEquals(null, saidaRepository.buscarPorId("1"))
    }

    @Test
    fun `deve excluir itens associados a saida`() = runTest {
        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = ExcluirSaidaUseCase(
            saidaRepository,
            itemSaidaRepository
        )

        saidaRepository.cadastrar(
            Saida(
                id = "1",
                titulo = "Faculdade",
                dataCriacao = System.currentTimeMillis(),
                status = StatusSaida.EM_ANDAMENTO
            )
        )

        itemSaidaRepository.adicionar(
            ItemSaida(
                id = "1",
                saidaId = "1",
                itemId = "10",
                nomeItem = "Notebook",
                quantidade = 1
            )
        )

        itemSaidaRepository.adicionar(
            ItemSaida(
                id = "2",
                saidaId = "1",
                itemId = "20",
                nomeItem = "Caneta",
                quantidade = 2
            )
        )

        useCase("1")

        assertEquals(
            0,
            itemSaidaRepository.buscarPorSaidaId("1").size
        )
    }

    @Test
    fun `deve permitir excluir saida finalizada`() = runTest {
        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = ExcluirSaidaUseCase(
            saidaRepository,
            itemSaidaRepository
        )

        saidaRepository.cadastrar(
            Saida(
                id = "1",
                titulo = "Faculdade",
                dataCriacao = System.currentTimeMillis(),
                status = StatusSaida.FINALIZADA
            )
        )

        useCase("1")

        assertEquals(null, saidaRepository.buscarPorId("1"))
    }

    @Test
    fun `nao deve permitir excluir saida inexistente`() = runTest {
        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = ExcluirSaidaUseCase(
            saidaRepository,
            itemSaidaRepository
        )

        try {
            useCase("999")
        } catch (e: IllegalArgumentException) {
            assertEquals(
                "Saída não encontrada.",
                e.message
            )
            return@runTest
        }

        throw AssertionError(
            "Era esperado que a saída inexistente fosse rejeitada."
        )
    }
}