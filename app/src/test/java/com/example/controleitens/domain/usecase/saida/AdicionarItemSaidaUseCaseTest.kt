package com.example.controleitens.domain.usecase.saida

import com.example.controleitens.FakeItemRepository
import com.example.controleitens.FakeItemSaidaRepository
import com.example.controleitens.FakeSaidaRepository
import com.example.controleitens.domain.model.Item
import com.example.controleitens.domain.model.Saida
import com.example.controleitens.domain.model.StatusSaida
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AdicionarItemSaidaUseCaseTest {

    @Test
    fun `deve adicionar item ativo a saida em andamento`() = runTest {

        val itemRepository = FakeItemRepository()
        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = AdicionarItemSaidaUseCase(
            itemRepository,
            saidaRepository,
            itemSaidaRepository
        )

        val item = Item(
            id = "1",
            nome = "Notebook",
            ativo = true
        )

        val saida = Saida(
            id = "1",
            titulo = "Faculdade",
            dataCriacao = System.currentTimeMillis(),
            status = StatusSaida.EM_ANDAMENTO
        )

        itemRepository.cadastrar(item)
        saidaRepository.cadastrar(saida)

        useCase(
            saidaId = "1",
            itemId = "1",
            quantidade = 1
        )

        val itens = itemSaidaRepository.buscarPorSaidaId("1")

        assertEquals(1, itens.size)
        assertEquals("1", itens[0].itemId)
        assertEquals("1", itens[0].saidaId)
        assertEquals("Notebook", itens[0].nomeItem)
        assertEquals(1, itens[0].quantidade)
    }

    @Test
    fun `deve preservar a quantidade informada`() = runTest {

        val itemRepository = FakeItemRepository()
        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = AdicionarItemSaidaUseCase(
            itemRepository,
            saidaRepository,
            itemSaidaRepository
        )

        val item = Item(
            id = "1",
            nome = "Caneta",
            ativo = true
        )

        val saida = Saida(
            id = "1",
            titulo = "Faculdade",
            dataCriacao = System.currentTimeMillis(),
            status = StatusSaida.EM_ANDAMENTO
        )

        itemRepository.cadastrar(item)
        saidaRepository.cadastrar(saida)

        useCase(
            saidaId = "1",
            itemId = "1",
            quantidade = 5
        )

        val itemSaida = itemSaidaRepository
            .buscarPorSaidaId("1")[0]

        assertEquals(5, itemSaida.quantidade)
    }

    @Test
    fun `deve gerar id para o ItemSaida`() = runTest {

        val itemRepository = FakeItemRepository()
        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = AdicionarItemSaidaUseCase(
            itemRepository,
            saidaRepository,
            itemSaidaRepository
        )

        itemRepository.cadastrar(
            Item(
                id = "1",
                nome = "Notebook",
                ativo = true
            )
        )

        saidaRepository.cadastrar(
            Saida(
                id = "1",
                titulo = "Faculdade",
                dataCriacao = System.currentTimeMillis(),
                status = StatusSaida.EM_ANDAMENTO
            )
        )

        useCase("1", "1", 1)

        val itemSaida = itemSaidaRepository
            .buscarPorSaidaId("1")[0]

        assertNotNull(itemSaida.id)
        assertTrue(itemSaida.id.isNotBlank())
    }

    @Test
    fun `nao deve permitir quantidade igual a zero`() = runTest {

        val itemRepository = FakeItemRepository()
        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = AdicionarItemSaidaUseCase(
            itemRepository,
            saidaRepository,
            itemSaidaRepository
        )

        try {
            useCase("1", "1", 0)
        } catch (e: IllegalArgumentException) {
            return@runTest
        }

        throw AssertionError(
            "Era esperado que a quantidade fosse rejeitada."
        )
    }

    @Test
    fun `nao deve permitir quantidade negativa`() = runTest {

        val itemRepository = FakeItemRepository()
        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = AdicionarItemSaidaUseCase(
            itemRepository,
            saidaRepository,
            itemSaidaRepository
        )

        try {
            useCase("1", "1", -1)
        } catch (e: IllegalArgumentException) {
            return@runTest
        }

        throw AssertionError(
            "Era esperado que a quantidade fosse rejeitada."
        )
    }

    @Test
    fun `nao deve permitir adicionar item a saida inexistente`() = runTest {

        val itemRepository = FakeItemRepository()
        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = AdicionarItemSaidaUseCase(
            itemRepository,
            saidaRepository,
            itemSaidaRepository
        )

        itemRepository.cadastrar(
            Item(
                id = "1",
                nome = "Notebook",
                ativo = true
            )
        )

        try {
            useCase("999", "1", 1)
        } catch (e: IllegalArgumentException) {
            assertEquals("Saída não encontrada.", e.message)
            return@runTest
        }

        throw AssertionError(
            "Era esperado que a saída inexistente fosse rejeitada."
        )
    }

    @Test
    fun `nao deve permitir adicionar item inexistente`() = runTest {

        val itemRepository = FakeItemRepository()
        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = AdicionarItemSaidaUseCase(
            itemRepository,
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

        try {
            useCase("1", "999", 1)
        } catch (e: IllegalArgumentException) {
            assertEquals("Item não encontrado.", e.message)
            return@runTest
        }

        throw AssertionError(
            "Era esperado que o item inexistente fosse rejeitado."
        )
    }

    @Test
    fun `nao deve permitir adicionar item inativo`() = runTest {

        val itemRepository = FakeItemRepository()
        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = AdicionarItemSaidaUseCase(
            itemRepository,
            saidaRepository,
            itemSaidaRepository
        )

        itemRepository.cadastrar(
            Item(
                id = "1",
                nome = "Notebook",
                ativo = false
            )
        )

        saidaRepository.cadastrar(
            Saida(
                id = "1",
                titulo = "Faculdade",
                dataCriacao = System.currentTimeMillis(),
                status = StatusSaida.EM_ANDAMENTO
            )
        )

        try {
            useCase("1", "1", 1)
        } catch (e: IllegalArgumentException) {
            assertEquals(
                "Não é possível adicionar um item inativo.",
                e.message
            )
            return@runTest
        }

        throw AssertionError(
            "Era esperado que o item inativo fosse rejeitado."
        )
    }

    @Test
    fun `nao deve permitir adicionar item a saida finalizada`() = runTest {

        val itemRepository = FakeItemRepository()
        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = AdicionarItemSaidaUseCase(
            itemRepository,
            saidaRepository,
            itemSaidaRepository
        )

        itemRepository.cadastrar(
            Item(
                id = "1",
                nome = "Notebook",
                ativo = true
            )
        )

        saidaRepository.cadastrar(
            Saida(
                id = "1",
                titulo = "Faculdade",
                dataCriacao = System.currentTimeMillis(),
                status = StatusSaida.FINALIZADA
            )
        )

        try {
            useCase("1", "1", 1)
        } catch (e: IllegalArgumentException) {
            assertEquals(
                "Não é possível adicionar itens a uma saída finalizada.",
                e.message
            )
            return@runTest
        }

        throw AssertionError(
            "Era esperado que a saída finalizada fosse rejeitada."
        )
    }

    @Test
    fun `nao deve permitir adicionar item que ja esta na saida`() = runTest {

        val itemRepository = FakeItemRepository()
        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = AdicionarItemSaidaUseCase(
            itemRepository,
            saidaRepository,
            itemSaidaRepository
        )

        itemRepository.cadastrar(
            Item(
                id = "1",
                nome = "Notebook",
                ativo = true
            )
        )

        saidaRepository.cadastrar(
            Saida(
                id = "1",
                titulo = "Faculdade",
                dataCriacao = System.currentTimeMillis(),
                status = StatusSaida.EM_ANDAMENTO
            )
        )

        useCase("1", "1", 1)

        try {
            useCase("1", "1", 1)
        } catch (e: IllegalArgumentException) {
            assertEquals(
                "O item já foi adicionado a esta saída.",
                e.message
            )
            return@runTest
        }

        throw AssertionError(
            "Era esperado que o item duplicado fosse rejeitado."
        )
    }

    @Test
    fun `deve copiar o nome atual do item para o ItemSaida`() = runTest {

        val itemRepository = FakeItemRepository()
        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = AdicionarItemSaidaUseCase(
            itemRepository,
            saidaRepository,
            itemSaidaRepository
        )

        itemRepository.cadastrar(
            Item(
                id = "1",
                nome = "Notebook",
                ativo = true
            )
        )

        saidaRepository.cadastrar(
            Saida(
                id = "1",
                titulo = "Faculdade",
                dataCriacao = System.currentTimeMillis(),
                status = StatusSaida.EM_ANDAMENTO
            )
        )

        useCase("1", "1", 1)

        val itemSaida = itemSaidaRepository
            .buscarPorSaidaId("1")[0]

        assertEquals("Notebook", itemSaida.nomeItem)
    }
}