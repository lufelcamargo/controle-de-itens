package com.example.controleitens.domain.usecase.saida

import com.example.controleitens.FakeItemModeloRepository
import com.example.controleitens.FakeItemSaidaRepository
import com.example.controleitens.FakeModeloRepository
import com.example.controleitens.FakeSaidaRepository
import com.example.controleitens.domain.model.ItemModelo
import com.example.controleitens.domain.model.Modelo
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CriarSaidaModeloUseCaseTest {

    @Test
    fun `deve criar saida a partir do modelo`() = runTest {

        val modeloRepository = FakeModeloRepository()
        val itemModeloRepository = FakeItemModeloRepository()
        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = CriarSaidaModeloUseCase(
            modeloRepository,
            itemModeloRepository,
            saidaRepository,
            itemSaidaRepository
        )

        modeloRepository.cadastrar(
            Modelo(
                id = "1",
                titulo = "Faculdade"
            )
        )

        itemModeloRepository.adicionar(
            ItemModelo(
                id = "1",
                modeloId = "1",
                itemId = "10",
                nomeItem = "Notebook",
                quantidade = 1
            )
        )

        itemModeloRepository.adicionar(
            ItemModelo(
                id = "2",
                modeloId = "1",
                itemId = "20",
                nomeItem = "Caderno",
                quantidade = 2
            )
        )

        val saidaId = useCase("1")

        val saida = saidaRepository.buscarPorId(saidaId)
        val itens = itemSaidaRepository.buscarPorSaidaId(saidaId)

        assertNotNull(saida)
        assertEquals("Faculdade", saida?.titulo)
        assertEquals(2, itens.size)
    }

    @Test
    fun `deve criar saida com status em andamento`() = runTest {

        val modeloRepository = FakeModeloRepository()
        val itemModeloRepository = FakeItemModeloRepository()
        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = CriarSaidaModeloUseCase(
            modeloRepository,
            itemModeloRepository,
            saidaRepository,
            itemSaidaRepository
        )

        modeloRepository.cadastrar(
            Modelo(
                id = "1",
                titulo = "Viagem"
            )
        )

        val saidaId = useCase("1")

        val saida = saidaRepository.buscarPorId(saidaId)

        assertEquals(
            com.example.controleitens.domain.model.StatusSaida.EM_ANDAMENTO,
            saida?.status
        )
    }

    @Test
    fun `deve copiar quantidades dos itens do modelo`() = runTest {

        val modeloRepository = FakeModeloRepository()
        val itemModeloRepository = FakeItemModeloRepository()
        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = CriarSaidaModeloUseCase(
            modeloRepository,
            itemModeloRepository,
            saidaRepository,
            itemSaidaRepository
        )

        modeloRepository.cadastrar(
            Modelo(
                id = "1",
                titulo = "Viagem"
            )
        )

        itemModeloRepository.adicionar(
            ItemModelo(
                id = "1",
                modeloId = "1",
                itemId = "10",
                nomeItem = "Camiseta",
                quantidade = 5
            )
        )

        val saidaId = useCase("1")

        val itens = itemSaidaRepository.buscarPorSaidaId(saidaId)

        assertEquals(1, itens.size)
        assertEquals(5, itens[0].quantidade)
        assertEquals("Camiseta", itens[0].nomeItem)
    }

    @Test
    fun `nao deve alterar o modelo ao criar a saida`() = runTest {

        val modeloRepository = FakeModeloRepository()
        val itemModeloRepository = FakeItemModeloRepository()
        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = CriarSaidaModeloUseCase(
            modeloRepository,
            itemModeloRepository,
            saidaRepository,
            itemSaidaRepository
        )

        modeloRepository.cadastrar(
            Modelo(
                id = "1",
                titulo = "Viagem"
            )
        )

        itemModeloRepository.adicionar(
            ItemModelo(
                id = "1",
                modeloId = "1",
                itemId = "10",
                nomeItem = "Camiseta",
                quantidade = 5
            )
        )

        useCase("1")

        val modelo = modeloRepository.buscarPorId("1")
        val itensModelo = itemModeloRepository.buscarPorModeloId("1")

        assertEquals("Viagem", modelo?.titulo)
        assertEquals(1, itensModelo.size)
        assertEquals(5, itensModelo[0].quantidade)
    }

    @Test
    fun `deve gerar novo id para a saida`() = runTest {

        val modeloRepository = FakeModeloRepository()
        val itemModeloRepository = FakeItemModeloRepository()
        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = CriarSaidaModeloUseCase(
            modeloRepository,
            itemModeloRepository,
            saidaRepository,
            itemSaidaRepository
        )

        modeloRepository.cadastrar(
            Modelo(
                id = "1",
                titulo = "Viagem"
            )
        )

        val saidaId = useCase("1")

        assertTrue(saidaId.isNotBlank())
        assertNotNull(saidaRepository.buscarPorId(saidaId))
    }

    @Test
    fun `nao deve permitir criar saida de modelo inexistente`() = runTest {

        val modeloRepository = FakeModeloRepository()
        val itemModeloRepository = FakeItemModeloRepository()
        val saidaRepository = FakeSaidaRepository()
        val itemSaidaRepository = FakeItemSaidaRepository()

        val useCase = CriarSaidaModeloUseCase(
            modeloRepository,
            itemModeloRepository,
            saidaRepository,
            itemSaidaRepository
        )

        try {
            useCase("999")
        } catch (e: IllegalArgumentException) {
            assertEquals(
                "Modelo não encontrado.",
                e.message
            )
            return@runTest
        }

        throw AssertionError(
            "Era esperado que o modelo inexistente fosse rejeitado."
        )
    }
}