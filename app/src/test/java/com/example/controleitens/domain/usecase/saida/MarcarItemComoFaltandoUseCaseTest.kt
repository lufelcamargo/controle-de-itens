package com.example.controleitens.domain.usecase.saida

import com.example.controleitens.FakeItemSaidaRepository
import com.example.controleitens.FakeSaidaRepository
import com.example.controleitens.domain.model.ItemSaida
import com.example.controleitens.domain.model.Saida
import com.example.controleitens.domain.model.StatusSaida
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class MarcarItemComoFaltandoUseCaseTest {

    @Test
    fun `deve marcar item como faltando`() = runTest {
        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = MarcarItemComoFaltandoUseCase(
            saidaRepository,
            itemSaidaRepository
        )

        saidaRepository.cadastrar(
            Saida(
                id = "1",
                titulo = "Faculdade",
                dataCriacao = 1L,
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

        useCase("1")

        val item = itemSaidaRepository.buscarPorId("1")

        assertEquals(true, item?.faltando)
    }

    @Test
    fun `nao deve alterar quantidade ao marcar item como faltando`() = runTest {
        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = MarcarItemComoFaltandoUseCase(
            saidaRepository,
            itemSaidaRepository
        )

        saidaRepository.cadastrar(
            Saida(
                id = "1",
                titulo = "Faculdade",
                dataCriacao = 1L,
                status = StatusSaida.EM_ANDAMENTO
            )
        )

        itemSaidaRepository.adicionar(
            ItemSaida(
                id = "1",
                saidaId = "1",
                itemId = "10",
                nomeItem = "Notebook",
                quantidade = 3
            )
        )

        useCase("1")

        val item = itemSaidaRepository.buscarPorId("1")

        assertEquals(3, item?.quantidade)
    }

    @Test
    fun `nao deve permitir marcar item inexistente como faltando`() = runTest {
        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = MarcarItemComoFaltandoUseCase(
            saidaRepository,
            itemSaidaRepository
        )

        try {
            useCase("999")
        } catch (e: IllegalArgumentException) {
            assertEquals(
                "Item da saída não encontrado.",
                e.message
            )
            return@runTest
        }

        throw AssertionError(
            "Era esperado que o item inexistente fosse rejeitado."
        )
    }

    @Test
    fun `nao deve permitir marcar item de saida inexistente como faltando`() = runTest {
        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = MarcarItemComoFaltandoUseCase(
            saidaRepository,
            itemSaidaRepository
        )

        itemSaidaRepository.adicionar(
            ItemSaida(
                id = "1",
                saidaId = "999",
                itemId = "10",
                nomeItem = "Notebook",
                quantidade = 1
            )
        )

        try {
            useCase("1")
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

    @Test
    fun `nao deve permitir marcar item de saida finalizada como faltando`() = runTest {
        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = MarcarItemComoFaltandoUseCase(
            saidaRepository,
            itemSaidaRepository
        )

        saidaRepository.cadastrar(
            Saida(
                id = "1",
                titulo = "Faculdade",
                dataCriacao = 1L,
                status = StatusSaida.FINALIZADA
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

        try {
            useCase("1")
        } catch (e: IllegalArgumentException) {
            assertEquals(
                "Não é possível marcar itens de uma saída finalizada.",
                e.message
            )
            return@runTest
        }

        throw AssertionError(
            "Era esperado que a alteração fosse rejeitada."
        )
    }
}