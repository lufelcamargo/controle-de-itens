package com.example.controleitens.data.local.repository

import com.example.controleitens.data.local.dao.ItemDao
import com.example.controleitens.data.local.entity.ItemEntity
import com.example.controleitens.domain.model.Item
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ItemRepositoryImplTest {

    @Test
    fun `deve cadastrar e buscar item`() = runTest {
        val dao = FakeItemDao()
        val repository = ItemRepositoryImpl(dao)

        val item = Item(
            id = "1",
            nome = "Notebook",
            ativo = true
        )

        repository.cadastrar(item)

        val resultado = repository.buscarPorId("1")

        assertNotNull(resultado)
        assertEquals("Notebook", resultado?.nome)
        assertEquals(true, resultado?.ativo)
    }

    @Test
    fun `deve editar item`() = runTest {
        val dao = FakeItemDao()
        val repository = ItemRepositoryImpl(dao)

        repository.cadastrar(
            Item(
                id = "1",
                nome = "Notebook",
                ativo = true
            )
        )

        repository.editar(
            Item(
                id = "1",
                nome = "Notebook Samsung",
                ativo = true
            )
        )

        val resultado = repository.buscarPorId("1")

        assertEquals("Notebook Samsung", resultado?.nome)
    }

    @Test
    fun `deve excluir item logicamente`() = runTest {
        val dao = FakeItemDao()
        val repository = ItemRepositoryImpl(dao)

        repository.cadastrar(
            Item(
                id = "1",
                nome = "Notebook",
                ativo = true
            )
        )

        repository.excluir("1")

        val resultado = repository.buscarPorId("1")

        assertNotNull(resultado)
        assertFalse(resultado!!.ativo)

        assertEquals(0, repository.buscarAtivos().size)
    }

    @Test
    fun `deve retornar apenas itens ativos`() = runTest {
        val dao = FakeItemDao()
        val repository = ItemRepositoryImpl(dao)

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
                nome = "Mouse",
                ativo = false
            )
        )

        val ativos = repository.buscarAtivos()

        assertEquals(1, ativos.size)
        assertEquals("Notebook", ativos[0].nome)
    }

    @Test
    fun `deve retornar null para item inexistente`() = runTest {
        val dao = FakeItemDao()
        val repository = ItemRepositoryImpl(dao)

        val resultado = repository.buscarPorId("999")

        assertNull(resultado)
    }
}