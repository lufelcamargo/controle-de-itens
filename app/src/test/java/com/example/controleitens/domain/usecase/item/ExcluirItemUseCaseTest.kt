package com.example.controleitens.domain.usecase.item

import com.example.controleitens.FakeItemRepository
import com.example.controleitens.domain.model.Item
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExcluirItemUseCaseTest {

    @Test
    fun `deve excluir item logicamente`() = runTest {

        val repository = FakeItemRepository()
        val useCase = ExcluirItemUseCase(repository)

        val item = Item(
            id = "1",
            nome = "Notebook",
            ativo = true
        )

        repository.cadastrar(item)

        useCase("1")

        val itemExcluido = repository.buscarPorId("1")

        assertNotNull(itemExcluido)
        assertFalse(itemExcluido!!.ativo)
    }

    @Test
    fun `item excluido nao deve aparecer entre os itens ativos`() = runTest {

        val repository = FakeItemRepository()
        val useCase = ExcluirItemUseCase(repository)

        val item = Item(
            id = "1",
            nome = "Notebook",
            ativo = true
        )

        repository.cadastrar(item)

        useCase("1")

        val itensAtivos = repository.buscarAtivos()

        assertTrue(itensAtivos.isEmpty())
    }

    @Test
    fun `nao deve permitir excluir item inexistente`() = runTest {

        val repository = FakeItemRepository()
        val useCase = ExcluirItemUseCase(repository)

        try {
            useCase("999")
        } catch (e: IllegalArgumentException) {
            assertEquals("Item não encontrado.", e.message)
            return@runTest
        }

        throw AssertionError(
            "Era esperado que a exclusão de um item inexistente fosse rejeitada."
        )
    }

    @Test
    fun `outros itens ativos devem permanecer apos exclusao`() = runTest {

        val repository = FakeItemRepository()
        val useCase = ExcluirItemUseCase(repository)

        val notebook = Item(
            id = "1",
            nome = "Notebook",
            ativo = true
        )

        val celular = Item(
            id = "2",
            nome = "Celular",
            ativo = true
        )

        repository.cadastrar(notebook)
        repository.cadastrar(celular)

        useCase("1")

        val itensAtivos = repository.buscarAtivos()

        assertEquals(1, itensAtivos.size)
        assertEquals("Celular", itensAtivos[0].nome)
    }
}