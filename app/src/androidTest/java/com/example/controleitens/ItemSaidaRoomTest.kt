package com.example.controleitens

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.controleitens.data.local.repository.RepositoryProvider
import com.example.controleitens.domain.model.ItemSaida
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ItemSaidaRoomTest {

    private val context: Context
        get() = ApplicationProvider.getApplicationContext()

    @Test
    fun deveCadastrarEBuscarItemSaidaNoRoom() = runTest {
        val repository = RepositoryProvider.itemSaidaRepository(context)

        val itemSaida = ItemSaida(
            id = "teste-cadastro-item-saida",
            saidaId = "saida-1",
            itemId = "item-1",
            nomeItem = "Notebook",
            quantidade = 2
        )

        repository.adicionar(itemSaida)

        val resultado = repository.buscarPorId(itemSaida.id)

        assertNotNull(resultado)
        assertEquals("Notebook", resultado?.nomeItem)
        assertEquals(2, resultado?.quantidade)
        assertEquals(false, resultado?.conferido)
        assertEquals(false, resultado?.faltando)

        repository.excluir(itemSaida.id)
    }

    @Test
    fun deveEditarItemSaidaNoRoom() = runTest {
        val repository = RepositoryProvider.itemSaidaRepository(context)

        val itemSaida = ItemSaida(
            id = "teste-edicao-item-saida",
            saidaId = "saida-2",
            itemId = "item-2",
            nomeItem = "Caderno",
            quantidade = 1
        )

        repository.adicionar(itemSaida)

        val itemEditado = itemSaida.copy(
            quantidade = 5,
            conferido = true
        )

        repository.editar(itemEditado)

        val resultado = repository.buscarPorId(itemSaida.id)

        assertNotNull(resultado)
        assertEquals(5, resultado?.quantidade)
        assertTrue(resultado?.conferido == true)

        repository.excluir(itemSaida.id)
    }

    @Test
    fun deveBuscarItensPorSaidaId() = runTest {
        val repository = RepositoryProvider.itemSaidaRepository(context)

        val item1 = ItemSaida(
            id = "teste-busca-saida-1",
            saidaId = "saida-3",
            itemId = "item-1",
            nomeItem = "Notebook",
            quantidade = 1
        )

        val item2 = ItemSaida(
            id = "teste-busca-saida-2",
            saidaId = "saida-3",
            itemId = "item-2",
            nomeItem = "Mouse",
            quantidade = 2
        )

        repository.adicionar(item1)
        repository.adicionar(item2)

        val resultado = repository.buscarPorSaidaId("saida-3")

        assertEquals(2, resultado.size)
        assertTrue(resultado.any { it.id == item1.id })
        assertTrue(resultado.any { it.id == item2.id })

        repository.excluirPorSaidaId("saida-3")
    }

    @Test
    fun deveExcluirTodosOsItensDeUmaSaida() = runTest {
        val repository = RepositoryProvider.itemSaidaRepository(context)

        val item1 = ItemSaida(
            id = "teste-exclusao-saida-1",
            saidaId = "saida-4",
            itemId = "item-1",
            nomeItem = "Notebook",
            quantidade = 1
        )

        val item2 = ItemSaida(
            id = "teste-exclusao-saida-2",
            saidaId = "saida-4",
            itemId = "item-2",
            nomeItem = "Mouse",
            quantidade = 2
        )

        repository.adicionar(item1)
        repository.adicionar(item2)

        repository.excluirPorSaidaId("saida-4")

        val resultado = repository.buscarPorSaidaId("saida-4")

        assertTrue(resultado.isEmpty())
    }

    @Test
    fun devePersistirEstadoDeConferenciaEFaltando() = runTest {
        val repository = RepositoryProvider.itemSaidaRepository(context)

        val itemSaida = ItemSaida(
            id = "teste-status-item-saida",
            saidaId = "saida-5",
            itemId = "item-5",
            nomeItem = "Projetor",
            quantidade = 1,
            conferido = true,
            faltando = true
        )

        repository.adicionar(itemSaida)

        val novoRepository = RepositoryProvider.itemSaidaRepository(context)
        val resultado = novoRepository.buscarPorId(itemSaida.id)

        assertNotNull(resultado)
        assertTrue(resultado?.conferido == true)
        assertTrue(resultado?.faltando == true)

        novoRepository.excluir(itemSaida.id)
    }
}