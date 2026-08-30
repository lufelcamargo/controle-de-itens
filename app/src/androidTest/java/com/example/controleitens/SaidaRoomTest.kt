package com.example.controleitens

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.controleitens.data.local.repository.RepositoryProvider
import com.example.controleitens.domain.model.Saida
import com.example.controleitens.domain.model.StatusSaida
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SaidaRoomTest {

    private val context: Context
        get() = ApplicationProvider.getApplicationContext()

    @Test
    fun deveCadastrarEBuscarSaidaNoRoom() = runTest {
        val repository = RepositoryProvider.saidaRepository(context)

        val saida = Saida(
            id = "teste-cadastro-saida",
            titulo = "Saída de teste",
            dataCriacao = 123456789L,
            status = StatusSaida.EM_ANDAMENTO
        )

        repository.cadastrar(saida)

        val resultado = repository.buscarPorId(saida.id)

        assertNotNull(resultado)
        assertEquals("Saída de teste", resultado?.titulo)
        assertEquals(123456789L, resultado?.dataCriacao)
        assertEquals(StatusSaida.EM_ANDAMENTO, resultado?.status)

        repository.excluir(saida.id)
    }

    @Test
    fun deveEditarSaidaNoRoom() = runTest {
        val repository = RepositoryProvider.saidaRepository(context)

        val saida = Saida(
            id = "teste-edicao-saida",
            titulo = "Saída original",
            dataCriacao = 123456789L,
            status = StatusSaida.EM_ANDAMENTO
        )

        repository.cadastrar(saida)

        val saidaEditada = saida.copy(
            titulo = "Saída editada",
            status = StatusSaida.FINALIZADA
        )

        repository.editar(saidaEditada)

        val resultado = repository.buscarPorId(saida.id)

        assertNotNull(resultado)
        assertEquals("Saída editada", resultado?.titulo)
        assertEquals(StatusSaida.FINALIZADA, resultado?.status)

        repository.excluir(saida.id)
    }

    @Test
    fun deveExcluirSaidaNoRoom() = runTest {
        val repository = RepositoryProvider.saidaRepository(context)

        val saida = Saida(
            id = "teste-exclusao-saida",
            titulo = "Saída para excluir",
            dataCriacao = 123456789L,
            status = StatusSaida.EM_ANDAMENTO
        )

        repository.cadastrar(saida)
        repository.excluir(saida.id)

        val resultado = repository.buscarPorId(saida.id)

        assertTrue(resultado == null)
    }

    @Test
    fun deveBuscarTodasAsSaidas() = runTest {
        val repository = RepositoryProvider.saidaRepository(context)

        val saida1 = Saida(
            id = "teste-lista-saida-1",
            titulo = "Saída 1",
            dataCriacao = 123456789L,
            status = StatusSaida.EM_ANDAMENTO
        )

        val saida2 = Saida(
            id = "teste-lista-saida-2",
            titulo = "Saída 2",
            dataCriacao = 987654321L,
            status = StatusSaida.FINALIZADA
        )

        repository.cadastrar(saida1)
        repository.cadastrar(saida2)

        val resultado = repository.buscarTodas()

        assertTrue(resultado.any { it.id == saida1.id })
        assertTrue(resultado.any { it.id == saida2.id })

        repository.excluir(saida1.id)
        repository.excluir(saida2.id)
    }

    @Test
    fun deveManterSaidaAposAcessarNovamenteOBanco() = runTest {
        val repository = RepositoryProvider.saidaRepository(context)

        val saida = Saida(
            id = "teste-persistencia-saida",
            titulo = "Saída persistente",
            dataCriacao = 111111111L,
            status = StatusSaida.EM_ANDAMENTO
        )

        repository.cadastrar(saida)

        val novoRepository = RepositoryProvider.saidaRepository(context)

        val resultado = novoRepository.buscarPorId(saida.id)

        assertNotNull(resultado)
        assertEquals("Saída persistente", resultado?.titulo)
        assertEquals(StatusSaida.EM_ANDAMENTO, resultado?.status)

        novoRepository.excluir(saida.id)
    }
}