package com.example.controleitens.domain.usecase.saida

import com.example.controleitens.FakeItemSaidaRepository
import com.example.controleitens.FakeSaidaRepository
import com.example.controleitens.domain.model.ItemSaida
import com.example.controleitens.domain.model.Saida
import com.example.controleitens.domain.model.StatusSaida
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ConferirItemUseCaseTest {

    @Test
    fun `deve conferir item da saida`() = runTest {
        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = ConferirItemUseCase(
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

        assertEquals(true, item?.conferido)
    }

    @Test
    fun `nao deve alterar quantidade ao conferir item`() = runTest {
        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = ConferirItemUseCase(
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
    fun `nao deve permitir conferir item inexistente`() = runTest {
        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = ConferirItemUseCase(
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
    fun `nao deve permitir conferir item de saida inexistente`() = runTest {
        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = ConferirItemUseCase(
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
    fun `nao deve permitir conferir item de saida finalizada`() = runTest {
        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = ConferirItemUseCase(
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
                "Não é possível conferir itens de uma saída finalizada.",
                e.message
            )
            return@runTest
        }

        throw AssertionError(
            "Era esperado que a conferência fosse rejeitada."
        )
    }
}