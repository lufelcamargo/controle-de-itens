package com.example.controleitens.domain.usecase.saida

import com.example.controleitens.FakeItemSaidaRepository
import com.example.controleitens.FakeSaidaRepository
import com.example.controleitens.domain.model.ItemSaida
import com.example.controleitens.domain.model.Saida
import com.example.controleitens.domain.model.StatusSaida
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class EditarNomeItemSaidaUseCaseTest {

    @Test
    fun `deve alterar nome do item na saida`() = runTest {
        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = EditarNomeItemSaidaUseCase(
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

        useCase("1", "Notebook da faculdade")

        val item = itemSaidaRepository.buscarPorId("1")

        assertEquals("Notebook da faculdade", item?.nomeItem)
    }

    @Test
    fun `deve remover espacos das extremidades do nome`() = runTest {
        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = EditarNomeItemSaidaUseCase(
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

        useCase("1", "  Notebook da faculdade  ")

        val item = itemSaidaRepository.buscarPorId("1")

        assertEquals("Notebook da faculdade", item?.nomeItem)
    }

    @Test
    fun `nao deve permitir nome vazio`() = runTest {
        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = EditarNomeItemSaidaUseCase(
            saidaRepository,
            itemSaidaRepository
        )

        try {
            useCase("1", "")
        } catch (e: IllegalArgumentException) {
            assertEquals("O nome é obrigatório.", e.message)
            return@runTest
        }

        throw AssertionError("Era esperado que o nome fosse rejeitado.")
    }

    @Test
    fun `nao deve permitir alterar item de saida finalizada`() = runTest {
        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = EditarNomeItemSaidaUseCase(
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
            useCase("1", "Notebook novo")
        } catch (e: IllegalArgumentException) {
            assertEquals(
                "Não é possível alterar itens de uma saída finalizada.",
                e.message
            )
            return@runTest
        }

        throw AssertionError(
            "Era esperado que a alteração fosse rejeitada."
        )
    }
}