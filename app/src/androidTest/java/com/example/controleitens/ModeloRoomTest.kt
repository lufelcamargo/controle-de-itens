package com.example.controleitens

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.controleitens.data.local.repository.RepositoryProvider
import com.example.controleitens.domain.model.Modelo
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ModeloRoomTest {

    private val context: Context
        get() = ApplicationProvider.getApplicationContext()

    @Test
    fun deveCadastrarEBuscarModeloNoRoom() = runTest {
        val repository = RepositoryProvider.modeloRepository(context)

        val modelo = Modelo(
            id = "teste-cadastro-modelo",
            titulo = "Modelo de teste"
        )

        repository.cadastrar(modelo)

        val resultado = repository.buscarPorId(modelo.id)

        assertNotNull(resultado)
        assertEquals("Modelo de teste", resultado?.titulo)

        repository.excluir(modelo.id)
    }

    @Test
    fun deveEditarModeloNoRoom() = runTest {
        val repository = RepositoryProvider.modeloRepository(context)

        val modelo = Modelo(
            id = "teste-edicao-modelo",
            titulo = "Modelo original"
        )

        repository.cadastrar(modelo)

        val modeloEditado = modelo.copy(
            titulo = "Modelo editado"
        )

        repository.editar(modeloEditado)

        val resultado = repository.buscarPorId(modelo.id)

        assertNotNull(resultado)
        assertEquals("Modelo editado", resultado?.titulo)

        repository.excluir(modelo.id)
    }

    @Test
    fun deveExcluirModeloNoRoom() = runTest {
        val repository = RepositoryProvider.modeloRepository(context)

        val modelo = Modelo(
            id = "teste-exclusao-modelo",
            titulo = "Modelo para excluir"
        )

        repository.cadastrar(modelo)
        repository.excluir(modelo.id)

        val resultado = repository.buscarPorId(modelo.id)

        assertTrue(resultado == null)
    }

    @Test
    fun deveBuscarTodosOsModelos() = runTest {
        val repository = RepositoryProvider.modeloRepository(context)

        val modelo1 = Modelo(
            id = "teste-lista-modelo-1",
            titulo = "Modelo 1"
        )

        val modelo2 = Modelo(
            id = "teste-lista-modelo-2",
            titulo = "Modelo 2"
        )

        repository.cadastrar(modelo1)
        repository.cadastrar(modelo2)

        val resultado = repository.buscarTodas()

        assertTrue(resultado.any { it.id == modelo1.id })
        assertTrue(resultado.any { it.id == modelo2.id })

        repository.excluir(modelo1.id)
        repository.excluir(modelo2.id)
    }

    @Test
    fun deveManterModeloAposAcessarNovamenteOBanco() = runTest {
        val repository = RepositoryProvider.modeloRepository(context)

        val modelo = Modelo(
            id = "teste-persistencia-modelo",
            titulo = "Modelo persistente"
        )

        repository.cadastrar(modelo)

        val novoRepository = RepositoryProvider.modeloRepository(context)

        val resultado = novoRepository.buscarPorId(modelo.id)

        assertNotNull(resultado)
        assertEquals("Modelo persistente", resultado?.titulo)

        novoRepository.excluir(modelo.id)
    }
}