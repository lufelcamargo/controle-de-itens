package com.example.controleitens.domain.usecase.saida

import com.example.controleitens.FakeSaidaRepository
import com.example.controleitens.domain.model.Saida
import com.example.controleitens.domain.model.StatusSaida
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class EditarSaidaUseCaseTest {

    @Test
    fun `deve editar titulo da saida`() = runTest {
        val repository = FakeSaidaRepository()
        val useCase = EditarSaidaUseCase(repository)

        repository.cadastrar(
            Saida(
                id = "1",
                titulo = "Faculdade",
                dataCriacao = System.currentTimeMillis(),
                status = StatusSaida.EM_ANDAMENTO
            )
        )

        useCase("1", "Faculdade e trabalho")

        val saida = repository.buscarPorId("1")

        assertEquals("Faculdade e trabalho", saida?.titulo)
    }

    @Test
    fun `deve remover espacos das extremidades do titulo`() = runTest {
        val repository = FakeSaidaRepository()
        val useCase = EditarSaidaUseCase(repository)

        repository.cadastrar(
            Saida(
                id = "1",
                titulo = "Faculdade",
                dataCriacao = System.currentTimeMillis(),
                status = StatusSaida.EM_ANDAMENTO
            )
        )

        useCase("1", "  Faculdade  ")

        val saida = repository.buscarPorId("1")

        assertEquals("Faculdade", saida?.titulo)
    }

    @Test
    fun `nao deve permitir titulo vazio`() = runTest {
        val repository = FakeSaidaRepository()
        val useCase = EditarSaidaUseCase(repository)

        try {
            useCase("1", "")
        } catch (e: IllegalArgumentException) {
            assertEquals("O título é obrigatório.", e.message)
            return@runTest
        }

        throw AssertionError(
            "Era esperado que o título fosse rejeitado."
        )
    }

    @Test
    fun `nao deve permitir editar saida inexistente`() = runTest {
        val repository = FakeSaidaRepository()
        val useCase = EditarSaidaUseCase(repository)

        try {
            useCase("999", "Novo título")
        } catch (e: IllegalArgumentException) {
            assertEquals(
                "Saída não encontrada.",
                e.message
            )
            return@runTest
        }

        throw AssertionError(
            "Era esperado que a saída inexistente fosse rejeitada."
        )
    }

    @Test
    fun `nao deve permitir editar saida finalizada`() = runTest {
        val repository = FakeSaidaRepository()
        val useCase = EditarSaidaUseCase(repository)

        repository.cadastrar(
            Saida(
                id = "1",
                titulo = "Faculdade",
                dataCriacao = System.currentTimeMillis(),
                status = StatusSaida.FINALIZADA
            )
        )

        try {
            useCase("1", "Novo título")
        } catch (e: IllegalArgumentException) {
            assertEquals(
                "Não é possível editar uma saída finalizada.",
                e.message
            )
            return@runTest
        }

        throw AssertionError(
            "Era esperado que a edição fosse rejeitada."
        )
    }
}