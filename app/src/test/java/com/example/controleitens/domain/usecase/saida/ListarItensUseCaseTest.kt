package com.example.controleitens.domain.usecase.item

import com.example.controleitens.FakeItemRepository
import com.example.controleitens.domain.model.Item
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ListarItensUseCaseTest {

    @Test
    fun `deve retornar apenas itens ativos`() = runTest {

        val repository = FakeItemRepository()
        val useCase = ListarItensUseCase(repository)

        repository.cadastrar(
            Item(
                id = "1",
                nome = "Notebook",
                ativo = true
            )
        )

        repository.cadastrar(
            Item(
                id = "2",
                nome = "Celular",
                ativo = false
            )
        )

        repository.cadastrar(
            Item(
                id = "3",
                nome = "Tablet",
                ativo = true
            )
        )

        val itens = useCase()

        assertEquals(2, itens.size)
        assertEquals("Notebook", itens[0].nome)
        assertEquals("Tablet", itens[1].nome)
    }

    @Test
    fun `deve retornar lista vazia quando nao existem itens ativos`() = runTest {

        val repository = FakeItemRepository()
        val useCase = ListarItensUseCase(repository)

        repository.cadastrar(
            Item(
                id = "1",
                nome = "Notebook",
                ativo = false
            )
        )

        val itens = useCase()

        assertEquals(0, itens.size)
    }
}