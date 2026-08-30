package com.example.controleitens.data.local.repository

import com.example.controleitens.domain.model.Modelo
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ModeloRepositoryImplTest {

    @Test
    fun `deve cadastrar e buscar modelo`() = runTest {
        val dao = FakeModeloDao()
        val repository = ModeloRepositoryImpl(dao)

        repository.cadastrar(
            Modelo(
                id = "1",
                titulo = "Faculdade"
            )
        )

        val resultado = repository.buscarPorId("1")

        assertNotNull(resultado)
        assertEquals("Faculdade", resultado?.titulo)
    }

    @Test
    fun `deve editar modelo`() = runTest {
        val dao = FakeModeloDao()
        val repository = ModeloRepositoryImpl(dao)

        repository.cadastrar(
            Modelo(
                id = "1",
                titulo = "Faculdade"
            )
        )

        repository.editar(
            Modelo(
                id = "1",
                titulo = "Faculdade Atualizada"
            )
        )

        val resultado = repository.buscarPorId("1")

        assertEquals("Faculdade Atualizada", resultado?.titulo)
    }

    @Test
    fun `deve excluir modelo`() = runTest {
        val dao = FakeModeloDao()
        val repository = ModeloRepositoryImpl(dao)

        repository.cadastrar(
            Modelo(
                id = "1",
                titulo = "Faculdade"
            )
        )

        repository.excluir("1")

        assertNull(repository.buscarPorId("1"))
    }

    @Test
    fun `deve buscar todos os modelos`() = runTest {
        val dao = FakeModeloDao()
        val repository = ModeloRepositoryImpl(dao)

        repository.cadastrar(
            Modelo(
                id = "1",
                titulo = "Faculdade"
            )
        )

        repository.cadastrar(
            Modelo(
                id = "2",
                titulo = "Trabalho"
            )
        )

        val resultado = repository.buscarTodas()

        assertEquals(2, resultado.size)
        assertEquals("Faculdade", resultado[0].titulo)
        assertEquals("Trabalho", resultado[1].titulo)
    }

    @Test
    fun `deve retornar null para modelo inexistente`() = runTest {
        val dao = FakeModeloDao()
        val repository = ModeloRepositoryImpl(dao)

        val resultado = repository.buscarPorId("999")

        assertNull(resultado)
    }
}