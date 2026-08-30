package com.example.controleitens.data.local.repository

import com.example.controleitens.domain.model.Saida
import com.example.controleitens.domain.model.StatusSaida
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class SaidaRepositoryImplTest {

    @Test
    fun `deve cadastrar e buscar saida`() = runTest {
        val dao = FakeSaidaDao()
        val repository = SaidaRepositoryImpl(dao)

        val saida = Saida(
            id = "1",
            titulo = "Faculdade",
            dataCriacao = 1000L,
            status = StatusSaida.EM_ANDAMENTO
        )

        repository.cadastrar(saida)

        val resultado = repository.buscarPorId("1")

        assertNotNull(resultado)
        assertEquals("Faculdade", resultado?.titulo)
        assertEquals(StatusSaida.EM_ANDAMENTO, resultado?.status)
    }

    @Test
    fun `deve editar saida`() = runTest {
        val dao = FakeSaidaDao()
        val repository = SaidaRepositoryImpl(dao)

        repository.cadastrar(
            Saida(
                id = "1",
                titulo = "Faculdade",
                dataCriacao = 1000L,
                status = StatusSaida.EM_ANDAMENTO
            )
        )

        repository.editar(
            Saida(
                id = "1",
                titulo = "Faculdade - Atualizada",
                dataCriacao = 1000L,
                status = StatusSaida.FINALIZADA
            )
        )

        val resultado = repository.buscarPorId("1")

        assertEquals("Faculdade - Atualizada", resultado?.titulo)
        assertEquals(StatusSaida.FINALIZADA, resultado?.status)
    }

    @Test
    fun `deve excluir saida`() = runTest {
        val dao = FakeSaidaDao()
        val repository = SaidaRepositoryImpl(dao)

        repository.cadastrar(
            Saida(
                id = "1",
                titulo = "Faculdade",
                dataCriacao = 1000L,
                status = StatusSaida.EM_ANDAMENTO
            )
        )

        repository.excluir("1")

        val resultado = repository.buscarPorId("1")

        assertNull(resultado)
    }

    @Test
    fun `deve buscar todas as saidas`() = runTest {
        val dao = FakeSaidaDao()
        val repository = SaidaRepositoryImpl(dao)

        repository.cadastrar(
            Saida(
                id = "1",
                titulo = "Faculdade",
                dataCriacao = 1000L,
                status = StatusSaida.EM_ANDAMENTO
            )
        )

        repository.cadastrar(
            Saida(
                id = "2",
                titulo = "Trabalho",
                dataCriacao = 2000L,
                status = StatusSaida.FINALIZADA
            )
        )

        val resultado = repository.buscarTodas()

        assertEquals(2, resultado.size)
        assertEquals("Faculdade", resultado[0].titulo)
        assertEquals("Trabalho", resultado[1].titulo)
    }

    @Test
    fun `deve retornar null para saida inexistente`() = runTest {
        val dao = FakeSaidaDao()
        val repository = SaidaRepositoryImpl(dao)

        val resultado = repository.buscarPorId("999")

        assertNull(resultado)
    }
}