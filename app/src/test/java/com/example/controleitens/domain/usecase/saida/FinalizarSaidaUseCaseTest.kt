package com.example.controleitens.domain.usecase.saida

import com.example.controleitens.FakeSaidaRepository
import com.example.controleitens.domain.model.Saida
import com.example.controleitens.domain.model.StatusSaida
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class FinalizarSaidaUseCaseTest {

    @Test
    fun `deve finalizar saida em andamento`() = runTest {

        val repository = FakeSaidaRepository()
        val useCase = FinalizarSaidaUseCase(repository)

        repository.cadastrar(
            Saida(
                id = "1",
                titulo = "Faculdade",
                dataCriacao = System.currentTimeMillis(),
                status = StatusSaida.EM_ANDAMENTO
            )
        )

        useCase("1")

        val saida = repository.buscarPorId("1")

        assertEquals(StatusSaida.FINALIZADA, saida?.status)
    }

    @Test
    fun `nao deve permitir finalizar saida inexistente`() = runTest {

        val repository = FakeSaidaRepository()
        val useCase = FinalizarSaidaUseCase(repository)

        try {
            useCase("999")
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
    fun `nao deve permitir finalizar saida que ja esta finalizada`() = runTest {

        val repository = FakeSaidaRepository()
        val useCase = FinalizarSaidaUseCase(repository)

        repository.cadastrar(
            Saida(
                id = "1",
                titulo = "Faculdade",
                dataCriacao = System.currentTimeMillis(),
                status = StatusSaida.FINALIZADA
            )
        )

        try {
            useCase("1")
        } catch (e: IllegalArgumentException) {
            assertEquals(
                "A saída já está finalizada.",
                e.message
            )
            return@runTest
        }

        throw AssertionError(
            "Era esperado que a saída já finalizada fosse rejeitada."
        )
    }
}