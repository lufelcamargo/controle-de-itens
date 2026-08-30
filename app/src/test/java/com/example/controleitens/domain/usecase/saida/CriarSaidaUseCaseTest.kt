package com.example.controleitens.domain.usecase.saida

import com.example.controleitens.FakeSaidaRepository
import com.example.controleitens.domain.model.StatusSaida
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CriarSaidaUseCaseTest {

    @Test
    fun `deve criar saida com titulo valido`() = runTest {

        val repository = FakeSaidaRepository()
        val useCase = CriarSaidaUseCase(repository)

        useCase("Faculdade")

        val saidas = repository.buscarTodas()

        assertEquals(1, saidas.size)
        assertEquals("Faculdade", saidas[0].titulo)
    }

    @Test
    fun `deve criar saida com status em andamento`() = runTest {

        val repository = FakeSaidaRepository()
        val useCase = CriarSaidaUseCase(repository)

        useCase("Faculdade")

        val saida = repository.buscarTodas()[0]

        assertEquals(StatusSaida.EM_ANDAMENTO, saida.status)
    }

    @Test
    fun `deve gerar id para nova saida`() = runTest {

        val repository = FakeSaidaRepository()
        val useCase = CriarSaidaUseCase(repository)

        useCase("Faculdade")

        val saida = repository.buscarTodas()[0]

        assertNotNull(saida.id)
        assertTrue(saida.id.isNotBlank())
    }

    @Test
    fun `deve gerar data de criacao`() = runTest {

        val repository = FakeSaidaRepository()
        val useCase = CriarSaidaUseCase(repository)

        useCase("Faculdade")

        val saida = repository.buscarTodas()[0]

        assertTrue(saida.dataCriacao > 0)
    }

    @Test
    fun `nao deve permitir titulo vazio`() = runTest {

        val repository = FakeSaidaRepository()
        val useCase = CriarSaidaUseCase(repository)

        try {
            useCase("")
        } catch (e: IllegalArgumentException) {
            return@runTest
        }

        throw AssertionError(
            "Era esperado que a criação fosse rejeitada."
        )
    }

    @Test
    fun `nao deve permitir titulo contendo apenas espacos`() = runTest {

        val repository = FakeSaidaRepository()
        val useCase = CriarSaidaUseCase(repository)

        try {
            useCase("   ")
        } catch (e: IllegalArgumentException) {
            return@runTest
        }

        throw AssertionError(
            "Era esperado que a criação fosse rejeitada."
        )
    }

    @Test
    fun `deve remover espacos das extremidades do titulo`() = runTest {

        val repository = FakeSaidaRepository()
        val useCase = CriarSaidaUseCase(repository)

        useCase("  Faculdade  ")

        val saida = repository.buscarTodas()[0]

        assertEquals("Faculdade", saida.titulo)
    }

    @Test
    fun `deve permitir criar duas saidas com titulos iguais`() = runTest {

        val repository = FakeSaidaRepository()
        val useCase = CriarSaidaUseCase(repository)

        useCase("Faculdade")
        useCase("Faculdade")

        val saidas = repository.buscarTodas()

        assertEquals(2, saidas.size)
        assertFalse(saidas[0].id == saidas[1].id)
    }
}