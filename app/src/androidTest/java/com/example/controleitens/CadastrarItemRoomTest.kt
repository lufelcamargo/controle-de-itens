package com.example.controleitens

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.controleitens.data.local.repository.RepositoryProvider
import com.example.controleitens.domain.model.Item
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CadastrarItemRoomTest {

    private val context: Context
        get() = ApplicationProvider.getApplicationContext()

    @Test
    fun deveCadastrarEBuscarItemNoRoom() = runTest {
        val repository = RepositoryProvider.itemRepository(context)

        val item = Item(
            id = "teste-cadastro",
            nome = "Notebook",
            ativo = true
        )

        repository.cadastrar(item)

        val resultado = repository.buscarPorId(item.id)

        assertNotNull(resultado)
        assertEquals("Notebook", resultado?.nome)
        assertTrue(resultado?.ativo == true)

        repository.excluir(item.id)
    }

    @Test
    fun deveEditarItemNoRoom() = runTest {
        val repository = RepositoryProvider.itemRepository(context)

        val item = Item(
            id = "teste-edicao",
            nome = "Notebook",
            ativo = true
        )

        repository.cadastrar(item)

        val itemEditado = item.copy(
            nome = "Notebook Samsung"
        )

        repository.editar(itemEditado)

        val resultado = repository.buscarPorId(item.id)

        assertNotNull(resultado)
        assertEquals("Notebook Samsung", resultado?.nome)

        repository.excluir(item.id)
    }

    @Test
    fun deveExcluirItemLogicamenteNoRoom() = runTest {
        val repository = RepositoryProvider.itemRepository(context)

        val item = Item(
            id = "teste-exclusao",
            nome = "Mouse",
            ativo = true
        )

        repository.cadastrar(item)
        repository.excluir(item.id)

        val resultado = repository.buscarPorId(item.id)
        val ativos = repository.buscarAtivos()

        assertNotNull(resultado)
        assertFalse(resultado!!.ativo)
        assertTrue(ativos.none { it.id == item.id })
    }

    @Test
    fun deveManterDadosAposReabrirBanco() = runTest {
        val repository = RepositoryProvider.itemRepository(context)

        val item = Item(
            id = "teste-persistencia",
            nome = "Teclado",
            ativo = true
        )

        repository.cadastrar(item)

        // Obtém outro repository usando novamente o DatabaseProvider.
        // Ele deve acessar a mesma base persistida.
        val novoRepository = RepositoryProvider.itemRepository(context)

        val resultado = novoRepository.buscarPorId(item.id)

        assertNotNull(resultado)
        assertEquals("Teclado", resultado?.nome)
        assertTrue(resultado?.ativo == true)

        novoRepository.excluir(item.id)
    }

    @Test
    fun deveRetornarApenasItensAtivos() = runTest {
        val repository = RepositoryProvider.itemRepository(context)

        val itemAtivo = Item(
            id = "teste-ativo",
            nome = "Monitor",
            ativo = true
        )

        val itemInativo = Item(
            id = "teste-inativo",
            nome = "Impressora",
            ativo = false
        )

        repository.cadastrar(itemAtivo)
        repository.cadastrar(itemInativo)

        val ativos = repository.buscarAtivos()

        assertEquals(1, ativos.count { it.id == itemAtivo.id })
        assertEquals(0, ativos.count { it.id == itemInativo.id })

        repository.excluir(itemAtivo.id)
        repository.excluir(itemInativo.id)
    }
}