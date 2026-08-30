package com.example.controleitens.domain.usecase.saida

import com.example.controleitens.FakeSaidaRepository
import com.example.controleitens.domain.model.Saida
import com.example.controleitens.domain.model.StatusSaida
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ListarSaidasUseCaseTest {

    @Test
    fun `deve retornar todas as saidas`() = runTest {

        val repository = FakeSaidaRepository()
        val useCase = ListarSaidasUseCase(repository)

        repository.cadastrar(
            Saida(
                id = "1",
                titulo = "Faculdade",
                dataCriacao = 1L,
                status = StatusSaida.EM_ANDAMENTO
            )
        )

        repository.cadastrar(
            Saida(
                id = "2",
                titulo = "Trabalho",
                dataCriacao = 2L,
                status = StatusSaida.FINALIZADA
            )
        )

        val saidas = useCase()

        assertEquals(2, saidas.size)
        assertEquals("Faculdade", saidas[0].titulo)
        assertEquals("Trabalho", saidas[1].titulo)
    }

    @Test
    fun `deve retornar lista vazia quando nao existem saidas`() = runTest {

        val repository = FakeSaidaRepository()
        val useCase = ListarSaidasUseCase(repository)

        val saidas = useCase()

        assertEquals(0, saidas.size)
    }
}