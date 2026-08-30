package com.example.controleitens.domain.usecase.saida

import com.example.controleitens.FakeSaidaRepository
import com.example.controleitens.domain.model.Saida
import com.example.controleitens.domain.model.StatusSaida
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ConsultarHistoricoUseCaseTest {

    @Test
    fun `deve retornar apenas saidas finalizadas`() = runTest {

        val repository = FakeSaidaRepository()
        val useCase = ConsultarHistoricoUseCase(repository)

        repository.cadastrar(
            Saida(
                id = "1",
                titulo = "Faculdade",
                dataCriacao = 1000L,
                status = StatusSaida.FINALIZADA
            )
        )

        repository.cadastrar(
            Saida(
                id = "2",
                titulo = "Trabalho",
                dataCriacao = 2000L,
                status = StatusSaida.EM_ANDAMENTO
            )
        )

        repository.cadastrar(
            Saida(
                id = "3",
                titulo = "Viagem",
                dataCriacao = 3000L,
                status = StatusSaida.FINALIZADA
            )
        )

        val historico = useCase()

        assertEquals(2, historico.size)
        assertEquals("Viagem", historico[0].titulo)
        assertEquals("Faculdade", historico[1].titulo)
    }

    @Test
    fun `deve ordenar historico da mais recente para a mais antiga`() = runTest {

        val repository = FakeSaidaRepository()
        val useCase = ConsultarHistoricoUseCase(repository)

        repository.cadastrar(
            Saida(
                id = "1",
                titulo = "Mais antiga",
                dataCriacao = 1000L,
                status = StatusSaida.FINALIZADA
            )
        )

        repository.cadastrar(
            Saida(
                id = "2",
                titulo = "Mais recente",
                dataCriacao = 3000L,
                status = StatusSaida.FINALIZADA
            )
        )

        repository.cadastrar(
            Saida(
                id = "3",
                titulo = "Intermediária",
                dataCriacao = 2000L,
                status = StatusSaida.FINALIZADA
            )
        )

        val historico = useCase()

        assertEquals("Mais recente", historico[0].titulo)
        assertEquals("Intermediária", historico[1].titulo)
        assertEquals("Mais antiga", historico[2].titulo)
    }

    @Test
    fun `deve retornar lista vazia quando nao existem saidas finalizadas`() = runTest {

        val repository = FakeSaidaRepository()
        val useCase = ConsultarHistoricoUseCase(repository)

        repository.cadastrar(
            Saida(
                id = "1",
                titulo = "Faculdade",
                dataCriacao = 1000L,
                status = StatusSaida.EM_ANDAMENTO
            )
        )

        val historico = useCase()

        assertEquals(0, historico.size)
    }
}