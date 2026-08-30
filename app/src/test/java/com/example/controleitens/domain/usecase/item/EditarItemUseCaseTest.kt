package com.example.controleitens.domain.usecase.item

import com.example.controleitens.FakeItemRepository
import com.example.controleitens.domain.model.Item
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class EditarItemUseCaseTest {

    @Test
    fun `deve editar item normalmente`() = runTest {

        val repository = FakeItemRepository()
        val useCase = EditarItemUseCase(repository)

        val itemOriginal = Item(
            id = "1",
            nome = "Notebook"
        )

        val itemEditado = Item(
            id = "1",
            nome = "Notebook pessoal"
        )

        repository.cadastrar(itemOriginal)

        useCase(itemEditado)

        val item = repository.buscarPorId("1")

        assertEquals("Notebook pessoal", item?.nome)
    }

    @Test
    fun `nao deve permitir editar item para nome ja existente`() = runTest {

        val repository = FakeItemRepository()
        val useCase = EditarItemUseCase(repository)

        val notebook = Item(
            id = "1",
            nome = "Notebook"
        )

        val celular = Item(
            id = "2",
            nome = "Celular"
        )

        repository.cadastrar(notebook)
        repository.cadastrar(celular)

        val celularEditado = Item(
            id = "2",
            nome = "Notebook"
        )

        try {
            useCase(celularEditado)
        } catch (e: IllegalArgumentException) {
            return@runTest
        }

        throw AssertionError(
            "Era esperado que a edição fosse rejeitada."
        )
    }

    @Test
    fun `nao deve permitir editar item para nome existente ignorando maiusculas e minusculas`() = runTest {

        val repository = FakeItemRepository()
        val useCase = EditarItemUseCase(repository)

        val notebook = Item(
            id = "1",
            nome = "Notebook"
        )

        val celular = Item(
            id = "2",
            nome = "Celular"
        )

        repository.cadastrar(notebook)
        repository.cadastrar(celular)

        val celularEditado = Item(
            id = "2",
            nome = "NOTEBOOK"
        )

        try {
            useCase(celularEditado)
        } catch (e: IllegalArgumentException) {
            return@runTest
        }

        throw AssertionError(
            "Era esperado que a edição fosse rejeitada."
        )
    }

    @Test
    fun `deve permitir manter o proprio nome`() = runTest {

        val repository = FakeItemRepository()
        val useCase = EditarItemUseCase(repository)

        val item = Item(
            id = "1",
            nome = "Notebook"
        )

        repository.cadastrar(item)

        val itemEditado = Item(
            id = "1",
            nome = "Notebook"
        )

        useCase(itemEditado)

        val resultado = repository.buscarPorId("1")

        assertEquals("Notebook", resultado?.nome)
    }

    @Test
    fun `nao deve permitir editar item inexistente`() = runTest {

        val repository = FakeItemRepository()
        val useCase = EditarItemUseCase(repository)

        val item = Item(
            id = "999",
            nome = "Notebook"
        )

        try {
            useCase(item)
        } catch (e: IllegalArgumentException) {
            assertEquals("Item não encontrado.", e.message)
            return@runTest
        }

        throw AssertionError(
            "Era esperado que a edição de um item inexistente fosse rejeitada."
        )
    }
}