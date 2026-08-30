package com.example.controleitens.domain.usecase.item

import com.example.controleitens.FakeItemRepository
import com.example.controleitens.domain.model.Item
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.fail // Usaremos o fail para controle manual seguro
import org.junit.Test

class CadastrarItemUseCaseTest {

    @Test
    fun `deve cadastrar item quando nome ainda nao existe`() = runTest {
        val repository = FakeItemRepository()
        val useCase = CadastrarItemUseCase(repository)

        val item = Item(
            id = "1",
            nome = "Notebook"
        )

        useCase(item)

        val itens = repository.buscarAtivos()

        assertEquals(1, itens.size)
        assertEquals("Notebook", itens[0].nome)
    }

    @Test
    fun `nao deve permitir cadastrar item com nome duplicado`() = runTest {
        val repository = FakeItemRepository()
        val useCase = CadastrarItemUseCase(repository)

        val primeiroItem = Item(
            id = "1",
            nome = "Notebook"
        )

        val segundoItem = Item(
            id = "2",
            nome = "Notebook"
        )

        useCase(primeiroItem)

        // Captura manual limpa e segura dentro do mesmo runTest
        try {
            useCase(segundoItem)
            fail("Era esperado uma IllegalArgumentException, mas nenhum erro foi lançado.")
        } catch (e: IllegalArgumentException) {
            // Sucesso: A exceção correta foi lançada
            assertEquals("Já existe um item com esse nome.", e.message)
        }
    }

    @Test
    fun `nao deve permitir nomes iguais ignorando maiusculas e minusculas`() = runTest {
        val repository = FakeItemRepository()
        val useCase = CadastrarItemUseCase(repository)

        val primeiroItem = Item(
            id = "1",
            nome = "Notebook"
        )

        val segundoItem = Item(
            id = "2",
            nome = "notebook"
        )

        useCase(primeiroItem)

        // Captura manual limpa e segura dentro do mesmo runTest
        try {
            useCase(segundoItem)
            fail("Era esperado uma IllegalArgumentException, mas nenhum erro foi lançado.")
        } catch (e: IllegalArgumentException) {
            // Sucesso: A exceção correta foi lançada
            assertEquals("Já existe um item com esse nome.", e.message)
        }
    }
}
