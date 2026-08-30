package com.example.controleitens

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.controleitens.data.local.repository.RepositoryProvider
import com.example.controleitens.domain.model.ItemModelo
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ItemModeloRoomTest {

    private val context: Context
        get() = ApplicationProvider.getApplicationContext()

    @Test
    fun deveAdicionarEBuscarItemModeloNoRoom() = runTest {
        val repository = RepositoryProvider.itemModeloRepository(context)

        val itemModelo = ItemModelo(
            id = "teste-cadastro-item-modelo",
            modeloId = "modelo-1",
            itemId = "item-1",
            nomeItem = "Notebook",
            quantidade = 2
        )

        repository.adicionar(itemModelo)

        val resultado = repository.buscarPorId(itemModelo.id)

        assertNotNull(resultado)
        assertEquals("modelo-1", resultado?.modeloId)
        assertEquals("item-1", resultado?.itemId)
        assertEquals("Notebook", resultado?.nomeItem)
        assertEquals(2, resultado?.quantidade)

        repository.excluir(itemModelo.id)
    }

    @Test
    fun deveEditarItemModeloNoRoom() = runTest {
        val repository = RepositoryProvider.itemModeloRepository(context)

        val itemModelo = ItemModelo(
            id = "teste-edicao-item-modelo",
            modeloId = "modelo-2",
            itemId = "item-2",
            nomeItem = "Caderno",
            quantidade = 1
        )

        repository.adicionar(itemModelo)

        val itemModeloEditado = itemModelo.copy(
            quantidade = 5
        )

        repository.editar(itemModeloEditado)

        val resultado = repository.buscarPorId(itemModelo.id)

        assertNotNull(resultado)
        assertEquals(5, resultado?.quantidade)

        repository.excluir(itemModelo.id)
    }

    @Test
    fun deveBuscarItensPorModeloId() = runTest {
        val repository = RepositoryProvider.itemModeloRepository(context)

        val item1 = ItemModelo(
            id = "teste-busca-modelo-1",
            modeloId = "modelo-3",
            itemId = "item-1",
            nomeItem = "Notebook",
            quantidade = 1
        )

        val item2 = ItemModelo(
            id = "teste-busca-modelo-2",
            modeloId = "modelo-3",
            itemId = "item-2",
            nomeItem = "Mouse",
            quantidade = 2
        )

        repository.adicionar(item1)
        repository.adicionar(item2)

        val resultado = repository.buscarPorModeloId("modelo-3")

        assertEquals(2, resultado.size)
        assertTrue(resultado.any { it.id == item1.id })
        assertTrue(resultado.any { it.id == item2.id })

        repository.excluirPorModeloId("modelo-3")
    }

    @Test
    fun deveExcluirTodosOsItensDeUmModelo() = runTest {
        val repository = RepositoryProvider.itemModeloRepository(context)

        val item1 = ItemModelo(
            id = "teste-exclusao-modelo-1",
            modeloId = "modelo-4",
            itemId = "item-1",
            nomeItem = "Notebook",
            quantidade = 1
        )

        val item2 = ItemModelo(
            id = "teste-exclusao-modelo-2",
            modeloId = "modelo-4",
            itemId = "item-2",
            nomeItem = "Mouse",
            quantidade = 2
        )

        repository.adicionar(item1)
        repository.adicionar(item2)

        repository.excluirPorModeloId("modelo-4")

        val resultado = repository.buscarPorModeloId("modelo-4")

        assertTrue(resultado.isEmpty())
    }

    @Test
    fun deveManterItemModeloAposAcessarNovamenteOBanco() = runTest {
        val repository = RepositoryProvider.itemModeloRepository(context)

        val itemModelo = ItemModelo(
            id = "teste-persistencia-item-modelo",
            modeloId = "modelo-5",
            itemId = "item-5",
            nomeItem = "Projetor",
            quantidade = 3
        )

        repository.adicionar(itemModelo)

        val novoRepository = RepositoryProvider.itemModeloRepository(context)

        val resultado = novoRepository.buscarPorId(itemModelo.id)

        assertNotNull(resultado)
        assertEquals("Projetor", resultado?.nomeItem)
        assertEquals(3, resultado?.quantidade)

        novoRepository.excluir(itemModelo.id)
    }
}