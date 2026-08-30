package com.example.controleitens.domain.usecase.modelo

import com.example.controleitens.FakeModeloRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CriarModeloUseCaseTest {

    @Test
    fun `deve criar modelo`() = runTest {

        val repository = FakeModeloRepository()
        val useCase = CriarModeloUseCase(repository)

        useCase("Faculdade")

        val modelos = repository.buscarTodas()

        assertEquals(1, modelos.size)
        assertEquals("Faculdade", modelos[0].titulo)
    }

    @Test
    fun `deve remover espacos das extremidades do titulo`() = runTest {

        val repository = FakeModeloRepository()
        val useCase = CriarModeloUseCase(repository)

        useCase("  Faculdade  ")

        val modelo = repository.buscarTodas()[0]

        assertEquals("Faculdade", modelo.titulo)
    }

    @Test
    fun `nao deve permitir titulo vazio`() = runTest {

        val repository = FakeModeloRepository()
        val useCase = CriarModeloUseCase(repository)

        try {
            useCase("")
        } catch (e: IllegalArgumentException) {
            assertEquals("O título é obrigatório.", e.message)
            return@runTest
        }

        throw AssertionError(
            "Era esperado que o título fosse rejeitado."
        )
    }

    @Test
    fun `nao deve permitir titulo contendo apenas espacos`() = runTest {

        val repository = FakeModeloRepository()
        val useCase = CriarModeloUseCase(repository)

        try {
            useCase("   ")
        } catch (e: IllegalArgumentException) {
            assertEquals("O título é obrigatório.", e.message)
            return@runTest
        }

        throw AssertionError(
            "Era esperado que o título fosse rejeitado."
        )
    }

    @Test
    fun `deve gerar id para o modelo`() = runTest {

        val repository = FakeModeloRepository()
        val useCase = CriarModeloUseCase(repository)

        useCase("Faculdade")

        val modelo = repository.buscarTodas()[0]

        assertNotNull(modelo.id)
        assertTrue(modelo.id.isNotBlank())
    }
}