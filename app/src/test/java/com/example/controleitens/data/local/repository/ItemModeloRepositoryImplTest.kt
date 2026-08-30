package com.example.controleitens.data.local.repository

import com.example.controleitens.domain.model.ItemModelo
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ItemModeloRepositoryImplTest {

    @Test
    fun `deve adicionar e buscar item do modelo`() = runTest {
        val dao = FakeItemModeloDao()
        val repository = ItemModeloRepositoryImpl(dao)

        repository.adicionar(
            ItemModelo(
                id = "1",
                modeloId = "10",
                itemId = "20",
                nomeItem = "Notebook",
                quantidade = 1
            )
        )

        val resultado = repository.buscarPorId("1")

        assertEquals("Notebook", resultado?.nomeItem)
        assertEquals(1, resultado?.quantidade)
    }

    @Test
    fun `deve editar item do modelo`() = runTest {
        val dao = FakeItemModeloDao()
        val repository = ItemModeloRepositoryImpl(dao)

        repository.adicionar(
            ItemModelo(
                id = "1",
                modeloId = "10",
                itemId = "20",
                nomeItem = "Notebook",
                quantidade = 1
            )
        )

        repository.editar(
            ItemModelo(
                id = "1",
                modeloId = "10",
                itemId = "20",
                nomeItem = "Notebook",
                quantidade = 3
            )
        )

        val resultado = repository.buscarPorId("1")

        assertEquals(3, resultado?.quantidade)
    }

    @Test
    fun `deve excluir item do modelo`() = runTest {
        val dao = FakeItemModeloDao()
        val repository = ItemModeloRepositoryImpl(dao)

        repository.adicionar(
            ItemModelo(
                id = "1",
                modeloId = "10",
                itemId = "20",
                nomeItem = "Notebook",
                quantidade = 1
            )
        )

        repository.excluir("1")

        assertNull(repository.buscarPorId("1"))
    }

    @Test
    fun `deve buscar itens de um modelo`() = runTest {
        val dao = FakeItemModeloDao()
        val repository = ItemModeloRepositoryImpl(dao)

        repository.adicionar(
            ItemModelo(
                id = "1",
                modeloId = "10",
                itemId = "20",
                nomeItem = "Notebook",
                quantidade = 1
            )
        )

        repository.adicionar(
            ItemModelo(
                id = "2",
                modeloId = "10",
                itemId = "30",
                nomeItem = "Caderno",
                quantidade = 2
            )
        )

        repository.adicionar(
            ItemModelo(
                id = "3",
                modeloId = "99",
                itemId = "40",
                nomeItem = "Caneta",
                quantidade = 5
            )
        )

        val resultado = repository.buscarPorModeloId("10")

        assertEquals(2, resultado.size)
        assertEquals("Notebook", resultado[0].nomeItem)
        assertEquals("Caderno", resultado[1].nomeItem)
    }

    @Test
    fun `deve excluir todos os itens de um modelo`() = runTest {
        val dao = FakeItemModeloDao()
        val repository = ItemModeloRepositoryImpl(dao)

        repository.adicionar(
            ItemModelo(
                id = "1",
                modeloId = "10",
                itemId = "20",
                nomeItem = "Notebook",
                quantidade = 1
            )
        )

        repository.adicionar(
            ItemModelo(
                id = "2",
                modeloId = "10",
                itemId = "30",
                nomeItem = "Caderno",
                quantidade = 2
            )
        )

        repository.excluirPorModeloId("10")

        assertTrue(
            repository.buscarPorModeloId("10").isEmpty()
        )
    }
}