package com.example.controleitens.domain.usecase.saida

import com.example.controleitens.FakeItemSaidaRepository
import com.example.controleitens.FakeSaidaRepository
import com.example.controleitens.domain.model.ItemSaida
import com.example.controleitens.domain.model.Saida
import com.example.controleitens.domain.model.StatusSaida
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class AlterarQuantidadeItemSaidaUseCaseTest {

    @Test
    fun `deve alterar quantidade do item da saida`() = runTest {

        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = AlterarQuantidadeItemSaidaUseCase(
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

        useCase(
            itemSaidaId = "1",
            novaQuantidade = 3
        )

        val itemSaida = itemSaidaRepository.buscarPorId("1")

        assertEquals(3, itemSaida?.quantidade)
    }

    @Test
    fun `deve manter nome personalizado ao alterar quantidade`() = runTest {

        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = AlterarQuantidadeItemSaidaUseCase(
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
                nomeItem = "Notebook da faculdade",
                quantidade = 1
            )
        )

        useCase(
            itemSaidaId = "1",
            novaQuantidade = 5
        )

        val itemSaida = itemSaidaRepository.buscarPorId("1")

        assertEquals("Notebook da faculdade", itemSaida?.nomeItem)
        assertEquals(5, itemSaida?.quantidade)
    }

    @Test
    fun `nao deve permitir quantidade igual a zero`() = runTest {

        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = AlterarQuantidadeItemSaidaUseCase(
            saidaRepository,
            itemSaidaRepository
        )

        try {
            useCase(
                itemSaidaId = "1",
                novaQuantidade = 0
            )
        } catch (e: IllegalArgumentException) {
            assertEquals(
                "A quantidade deve ser maior que zero.",
                e.message
            )
            return@runTest
        }

        throw AssertionError(
            "Era esperado que a quantidade fosse rejeitada."
        )
    }

    @Test
    fun `nao deve permitir quantidade negativa`() = runTest {

        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = AlterarQuantidadeItemSaidaUseCase(
            saidaRepository,
            itemSaidaRepository
        )

        try {
            useCase(
                itemSaidaId = "1",
                novaQuantidade = -2
            )
        } catch (e: IllegalArgumentException) {
            assertEquals(
                "A quantidade deve ser maior que zero.",
                e.message
            )
            return@runTest
        }

        throw AssertionError(
            "Era esperado que a quantidade fosse rejeitada."
        )
    }

    @Test
    fun `nao deve permitir alterar item da saida inexistente`() = runTest {

        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = AlterarQuantidadeItemSaidaUseCase(
            saidaRepository,
            itemSaidaRepository
        )

        try {
            useCase(
                itemSaidaId = "999",
                novaQuantidade = 3
            )
        } catch (e: IllegalArgumentException) {
            assertEquals(
                "Item da saída não encontrado.",
                e.message
            )
            return@runTest
        }

        throw AssertionError(
            "Era esperado que o item da saída inexistente fosse rejeitado."
        )
    }

    @Test
    fun `nao deve permitir alterar item de saida inexistente`() = runTest {

        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = AlterarQuantidadeItemSaidaUseCase(
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
            useCase(
                itemSaidaId = "1",
                novaQuantidade = 3
            )
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
    fun `nao deve permitir alterar item de saida finalizada`() = runTest {

        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = AlterarQuantidadeItemSaidaUseCase(
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
            useCase(
                itemSaidaId = "1",
                novaQuantidade = 3
            )
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