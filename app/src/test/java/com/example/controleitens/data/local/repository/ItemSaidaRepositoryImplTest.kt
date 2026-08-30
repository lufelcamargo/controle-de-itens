package com.example.controleitens.data.local.repository

import com.example.controleitens.domain.model.ItemSaida
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ItemSaidaRepositoryImplTest {

    @Test
    fun `deve adicionar e buscar item da saida`() = runTest {
        val dao = FakeItemSaidaDao()
        val repository = ItemSaidaRepositoryImpl(dao)

        repository.adicionar(
            ItemSaida(
                id = "1",
                saidaId = "10",
                itemId = "20",
                nomeItem = "Notebook",
                quantidade = 2
            )
        )

        val resultado = repository.buscarPorId("1")

        assertEquals("Notebook", resultado?.nomeItem)
        assertEquals(2, resultado?.quantidade)
    }

    @Test
    fun `deve editar item da saida`() = runTest {
        val dao = FakeItemSaidaDao()
        val repository = ItemSaidaRepositoryImpl(dao)

        repository.adicionar(
            ItemSaida(
                id = "1",
                saidaId = "10",
                itemId = "20",
                nomeItem = "Notebook",
                quantidade = 1
            )
        )

        repository.editar(
            ItemSaida(
                id = "1",
                saidaId = "10",
                itemId = "20",
                nomeItem = "Notebook",
                quantidade = 3,
                conferido = true
            )
        )

        val resultado = repository.buscarPorId("1")

        assertEquals(3, resultado?.quantidade)
        assertTrue(resultado?.conferido == true)
    }

    @Test
    fun `deve excluir item da saida`() = runTest {
        val dao = FakeItemSaidaDao()
        val repository = ItemSaidaRepositoryImpl(dao)

        repository.adicionar(
            ItemSaida(
                id = "1",
                saidaId = "10",
                itemId = "20",
                nomeItem = "Notebook",
                quantidade = 1
            )
        )

        repository.excluir("1")

        assertNull(repository.buscarPorId("1"))
    }

    @Test
    fun `deve buscar itens de uma saida`() = runTest {
        val dao = FakeItemSaidaDao()
        val repository = ItemSaidaRepositoryImpl(dao)

        repository.adicionar(
            ItemSaida(
                id = "1",
                saidaId = "10",
                itemId = "20",
                nomeItem = "Notebook",
                quantidade = 1
            )
        )

        repository.adicionar(
            ItemSaida(
                id = "2",
                saidaId = "10",
                itemId = "30",
                nomeItem = "Caderno",
                quantidade = 2
            )
        )

        repository.adicionar(
            ItemSaida(
                id = "3",
                saidaId = "99",
                itemId = "40",
                nomeItem = "Caneta",
                quantidade = 5
            )
        )

        val resultado = repository.buscarPorSaidaId("10")

        assertEquals(2, resultado.size)
        assertEquals("Notebook", resultado[0].nomeItem)
        assertEquals("Caderno", resultado[1].nomeItem)
    }

    @Test
    fun `deve excluir todos os itens de uma saida`() = runTest {
        val dao = FakeItemSaidaDao()
        val repository = ItemSaidaRepositoryImpl(dao)

        repository.adicionar(
            ItemSaida(
                id = "1",
                saidaId = "10",
                itemId = "20",
                nomeItem = "Notebook",
                quantidade = 1
            )
        )

        repository.adicionar(
            ItemSaida(
                id = "2",
                saidaId = "10",
                itemId = "30",
                nomeItem = "Caderno",
                quantidade = 2
            )
        )

        repository.excluirPorSaidaId("10")

        assertTrue(
            repository.buscarPorSaidaId("10").isEmpty()
        )
    }
}