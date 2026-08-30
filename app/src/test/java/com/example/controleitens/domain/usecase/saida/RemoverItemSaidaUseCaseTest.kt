package com.example.controleitens.domain.usecase.saida

import com.example.controleitens.FakeItemSaidaRepository
import com.example.controleitens.FakeSaidaRepository
import com.example.controleitens.domain.model.ItemSaida
import com.example.controleitens.domain.model.Saida
import com.example.controleitens.domain.model.StatusSaida
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class RemoverItemSaidaUseCaseTest {

    @Test
    fun `deve remover item da saida`() = runTest {
        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = RemoverItemSaidaUseCase(
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

        useCase("1")

        assertEquals(
            0,
            itemSaidaRepository.buscarPorSaidaId("1").size
        )
    }

    @Test
    fun `nao deve permitir remover item inexistente`() = runTest {
        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = RemoverItemSaidaUseCase(
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
    fun `nao deve permitir remover item de saida finalizada`() = runTest {
        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = RemoverItemSaidaUseCase(
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
                "Não é possível remover itens de uma saída finalizada.",
                e.message
            )
            return@runTest
        }

        throw AssertionError(
            "Era esperado que a remoção fosse rejeitada."
        )
    }

    @Test
    fun `nao deve permitir remover item quando a saida nao existe`() = runTest {
        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = RemoverItemSaidaUseCase(
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
}