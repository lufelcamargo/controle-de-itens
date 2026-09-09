package com.example.controleitens.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.controleitens.data.local.database.DatabaseProvider
import com.example.controleitens.data.local.repository.ItemRepositoryImpl
import com.example.controleitens.data.local.repository.ItemSaidaRepositoryImpl
import com.example.controleitens.data.local.repository.SaidaRepositoryImpl
import com.example.controleitens.ui.components.BottomBar
import com.example.controleitens.ui.screens.AboutScreen
import com.example.controleitens.ui.screens.ConfigureSaidaScreen
import com.example.controleitens.ui.screens.HomeScreen
import com.example.controleitens.ui.screens.ItemsScreen
import com.example.controleitens.ui.screens.SaidaDetalhesScreen
import com.example.controleitens.ui.screens.SaidasScreen
import com.example.controleitens.ui.screens.SettingsScreen
import com.example.controleitens.ui.viewmodel.ItemsViewModel
import com.example.controleitens.ui.viewmodel.ItemsViewModelFactory
import com.example.controleitens.ui.viewmodel.SaidasViewModel
import com.example.controleitens.ui.viewmodel.SaidasViewModelFactory
import com.example.controleitens.data.local.repository.ItemModeloRepositoryImpl
import com.example.controleitens.data.local.repository.ModeloRepositoryImpl
import com.example.controleitens.data.preferences.UserPreferences
import com.example.controleitens.domain.usecase.saida.CriarSaidaModeloUseCase
import com.example.controleitens.ui.screens.EditarNomeScreen
import com.example.controleitens.ui.screens.ItemConfiguracao
import com.example.controleitens.ui.viewmodel.ModelosViewModel
import com.example.controleitens.ui.screens.ModelosScreen
import com.example.controleitens.ui.screens.ModeloDetalhesScreen
import com.example.controleitens.ui.screens.NomeUsuarioScreen
import com.example.controleitens.ui.viewmodel.UserPreferencesViewModel
import com.example.controleitens.ui.viewmodel.UserPreferencesViewModelFactory

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val context = LocalContext.current

    val database = DatabaseProvider.getDatabase(context)

    // Repositórios de saídas
    val saidaRepository = SaidaRepositoryImpl(
        database.saidaDao()
    )

    val itemSaidaRepository = ItemSaidaRepositoryImpl(
        database.itemSaidaDao()
    )

    // ViewModel de saídas
    val saidasViewModel: SaidasViewModel = viewModel(
        factory = SaidasViewModelFactory(
            saidaRepository,
            itemSaidaRepository
        )
    )

    val saidas by saidasViewModel.saidas.collectAsState()

    val quantidadeItens by saidasViewModel.quantidadeItens.collectAsState()

    val saidasPendentes by saidasViewModel.saidasPendentes.collectAsState()
    val itensAConferir by saidasViewModel.itensAConferir.collectAsState()

    // ViewModel de modelos de saídas
    val modelosViewModel = remember {
        val modeloRepository = ModeloRepositoryImpl(database.modeloDao())
        val itemModeloRepository = ItemModeloRepositoryImpl(database.itemModeloDao())

        ModelosViewModel(
            modeloRepository = modeloRepository,
            itemModeloRepository = itemModeloRepository,
            criarSaidaModeloUseCase = CriarSaidaModeloUseCase(
                modeloRepository = modeloRepository,
                itemModeloRepository = itemModeloRepository,
                saidaRepository = saidaRepository,
                itemSaidaRepository = itemSaidaRepository
            )
        )
    }

    // Repositório de itens
    val itemRepository = ItemRepositoryImpl(
        database.itemDao()
    )

    // ViewModel de itens
    val itemsViewModel: ItemsViewModel = viewModel(
        factory = ItemsViewModelFactory(itemRepository)
    )

    val itens by itemsViewModel.itens.collectAsState()
    val modelos by modelosViewModel.modelos.collectAsState()

    val userPreferences = remember {
        UserPreferences(context)
    }

    val userPreferencesViewModel: UserPreferencesViewModel = viewModel(
        factory = UserPreferencesViewModelFactory(userPreferences)
    )

    val nomeUsuario by userPreferencesViewModel.nomeUsuario.collectAsState()
    val carregandoNome by userPreferencesViewModel.carregando.collectAsState()

    Scaffold(
        bottomBar = {
            if (
                currentRoute == "inicio" ||
                currentRoute == "itens" ||
                currentRoute == "ajustes"
            ) {
                BottomBar(
                    currentRoute = currentRoute ?: "inicio",
                    onNavigate = { route ->
                        if (route != currentRoute) {
                            navController.navigate(route) {
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->

        if (!carregandoNome) {
            NavHost(
                navController = navController,
                startDestination = if (nomeUsuario == null) {
                    "configurar_nome"
                } else {
                    "inicio"
                },
                modifier = Modifier
                    .padding(innerPadding)
                    .statusBarsPadding()
            ) {

                // ---------------------------------------------------------
                // HOME
                // ---------------------------------------------------------
                composable("configurar_nome") {
                    NomeUsuarioScreen(
                        onContinuar = { nome ->
                            userPreferencesViewModel.salvarNome(nome)

                            navController.navigate("inicio") {
                                popUpTo("configurar_nome") {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }
                composable("inicio") {
                    HomeScreen(
                        saidas = saidas,
                        quantidadeItens = quantidadeItens,
                        saidasPendentes = saidasPendentes,
                        itensAConferir = itensAConferir,
                        nomeUsuario = nomeUsuario ?: "",
                        onNovaSaidaClick = {
                            navController.navigate("nova_saida")
                        },
                        onNovoModeloClick = {
                            navController.navigate("modelos")
                        },
                        onCadastrarItemClick = {
                            navController.navigate("itens")
                        },
                        onVerTudoClick = {
                            navController.navigate("saidas")
                        },
                        onSaidaClick = { id ->
                            navController.navigate("saida/$id")
                        }
                    )
                }

                // ---------------------------------------------------------
                // TODAS AS SAÍDAS
                // ---------------------------------------------------------

                composable("saidas") {
                    SaidasScreen(
                        saidas = saidas,
                        quantidadeItens = quantidadeItens,
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onSaidaClick = { id ->
                            navController.navigate("saida/$id")
                        },
                        onExcluirSaidas = { ids: List<String> ->
                            ids.forEach { id ->
                                saidasViewModel.excluirSaida(id)
                            }
                        }
                    )
                }

                // ---------------------------------------------------------
                // DETALHES DA SAÍDA
                // ---------------------------------------------------------

                composable(
                    route = "saida/{saidaId}",
                    arguments = listOf(
                        navArgument("saidaId") {
                            type = NavType.StringType
                        }
                    )
                ) { backStackEntry ->

                    val saidaId = backStackEntry.arguments
                        ?.getString("saidaId")

                    val saida = saidas.firstOrNull {
                        it.id == saidaId
                    }

                    var itensDaSaida by remember {
                        mutableStateOf(emptyList<com.example.controleitens.domain.model.ItemSaida>())
                    }

                    LaunchedEffect(saidaId) {
                        if (saidaId != null) {
                            saidasViewModel.buscarItensDaSaida(
                                saidaId = saidaId
                            ) { itens ->
                                itensDaSaida = itens
                            }
                        }
                    }

                    if (saida != null) {
                        SaidaDetalhesScreen(
                            saida = saida,
                            itens = itensDaSaida,
                            itensDisponiveis = itens,
                            onBackClick = {
                                navController.popBackStack()
                            },
                            onConferirItem = { itemId ->
                                saidasViewModel.conferirItem(itemId) { saidaId ->
                                    saidasViewModel.buscarItensDaSaida(saidaId) { itens ->
                                        itensDaSaida = itens
                                    }
                                }
                            },

                            onDesconferirItem = { itemId ->
                                saidasViewModel.desconferirItem(itemId) { saidaId ->
                                    saidasViewModel.buscarItensDaSaida(saidaId) { itens ->
                                        itensDaSaida = itens
                                    }
                                }
                            },
                            onExcluirItem = { itemId ->
                                saidasViewModel.excluirItemDaSaida(itemId) { saidaId ->
                                    saidasViewModel.buscarItensDaSaida(saidaId) { itens ->
                                        itensDaSaida = itens
                                    }
                                }
                            },
                            onAdicionarItem = { saidaId, itemId, nomeItem, quantidade ->
                                saidasViewModel.adicionarItemASaida(
                                    saidaId = saidaId,
                                    itemId = itemId,
                                    nomeItem = nomeItem,
                                    quantidade = quantidade
                                ) {
                                    saidasViewModel.buscarItensDaSaida(saidaId) { itens ->
                                        itensDaSaida = itens
                                    }
                                }
                            },
                            onFinalizarSaida = {
                                saidasViewModel.finalizarSaida(saida.id)
                            }
                        )
                    }
                }

                // ---------------------------------------------------------
                // ITENS
                // ---------------------------------------------------------

                composable("itens") {
                    ItemsScreen(
                        itens = itens,
                        onCadastrarItemClick = {
                            // ...
                        },
                        onCadastrarItem = { nome ->
                            itemsViewModel.cadastrarItem(nome)
                        },
                        onEditarItem = { id, novoNome ->
                            itemsViewModel.editarItem(id, novoNome)
                        },
                        onExcluirItem = { id ->
                            itemsViewModel.excluirItem(id)
                        }
                    )
                }

                // ---------------------------------------------------------
                // AJUSTES
                // ---------------------------------------------------------

                composable("ajustes") {
                    SettingsScreen(
                        nomeUsuario = nomeUsuario ?: "",
                        onNomeClick = {
                            navController.navigate("editar_nome")
                        },
                        onSobreClick = {
                            navController.navigate("sobre")
                        }
                    )
                }
                // ---------------------------------------------------------
                // EDIÇÃO DO NOME DE USUÁRIO
                // ---------------------------------------------------------

                composable("editar_nome") {
                    EditarNomeScreen(
                        nomeAtual = nomeUsuario ?: "",
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onSalvar = { novoNome ->
                            userPreferencesViewModel.salvarNome(novoNome)
                            navController.popBackStack()
                        }
                    )
                }

                // ---------------------------------------------------------
                // SOBRE
                // ---------------------------------------------------------

                composable("sobre") {
                    AboutScreen()
                }

                // ---------------------------------------------------------
                // NOVA SAÍDA
                // ---------------------------------------------------------

                composable("nova_saida") {
                    ConfigureSaidaScreen(
                        titulo = "Nova saída",
                        textoBotao = "Criar saída",
                        itensDisponiveis = itens,
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onCadastrarItem = { nome, onSuccess ->
                            itemsViewModel.cadastrarItem(nome, onSuccess)
                        },
                        onConfirmClick = { nome, itens ->
                            saidasViewModel.criarSaida(
                                titulo = nome,
                                itens = itens,
                                onSuccess = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    )
                }
                // ---------------------------------------------------------
                // MODELOS
                // ---------------------------------------------------------

                composable("modelos") {
                    ModelosScreen(
                        modelos = modelos,
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onModeloClick = { id ->
                            navController.navigate("modelo/$id")
                        },
                        onNovoModeloClick = {
                            navController.navigate("novo_modelo")
                        }
                    )
                }

                composable(
                    route = "modelo/{modeloId}",
                    arguments = listOf(
                        navArgument("modeloId") {
                            type = NavType.StringType
                        }
                    )
                ) { backStackEntry ->

                    val modeloId = backStackEntry.arguments
                        ?.getString("modeloId")

                    val modelo = modelos.firstOrNull {
                        it.id == modeloId
                    }

                    var itensDoModelo by remember {
                        mutableStateOf(emptyList<com.example.controleitens.domain.model.ItemModelo>())
                    }

                    LaunchedEffect(modeloId) {
                        if (modeloId != null) {
                            modelosViewModel.buscarItensDoModelo(
                                modeloId = modeloId
                            ) { itens ->
                                itensDoModelo = itens
                            }
                        }
                    }

                    if (modelo != null) {
                        ModeloDetalhesScreen(
                            modelo = modelo,
                            itens = itensDoModelo,

                            onBackClick = {
                                navController.popBackStack()
                            },

                            onEditarClick = {
                                navController.navigate("editar_modelo/$modeloId")
                            },

                            onExcluirClick = {
                                modelosViewModel.excluirModelo(
                                    modeloId = modelo.id,
                                    onSuccess = {
                                        navController.popBackStack()
                                    }
                                )
                            },

                            onCriarSaidaClick = {
                                modelosViewModel.criarSaidaAPartirDoModelo(
                                    modeloId = modelo.id
                                ) { saidaId ->
                                    saidasViewModel.carregarSaidas {
                                        navController.navigate("saidas") {
                                            popUpTo("inicio") {
                                                inclusive = false
                                            }
                                        }

                                        navController.navigate("saida/$saidaId")
                                    }
                                }
                            }
                        )
                    }
                }
                // ---------------------------------------------------------
                // NOVO MODELO
                // ---------------------------------------------------------

                composable("novo_modelo") {
                    ConfigureSaidaScreen(
                        titulo = "Novo modelo",
                        textoBotao = "Salvar modelo",
                        itensDisponiveis = itens,
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onCadastrarItem = { nome, onSuccess ->
                            itemsViewModel.cadastrarItem(nome, onSuccess)
                        },
                        onConfirmClick = { nome, itens ->
                            modelosViewModel.criarModelo(
                                titulo = nome,
                                itens = itens,
                                onSuccess = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    )
                }
                composable(
                    route = "editar_modelo/{modeloId}",
                    arguments = listOf(
                        navArgument("modeloId") {
                            type = NavType.StringType
                        }
                    )
                ) { backStackEntry ->

                    val modeloId = backStackEntry.arguments?.getString("modeloId")

                    val modelo = modelos.firstOrNull {
                        it.id == modeloId
                    }

                    var itensDoModelo by remember {
                        mutableStateOf(
                            emptyList<com.example.controleitens.domain.model.ItemModelo>()
                        )
                    }

                    LaunchedEffect(modeloId) {
                        if (modeloId != null) {
                            modelosViewModel.buscarItensDoModelo(
                                modeloId = modeloId
                            ) { itens ->
                                itensDoModelo = itens
                            }
                        }
                    }

                    if (modelo != null) {
                        ConfigureSaidaScreen(
                            titulo = "Editar modelo",
                            textoBotao = "Salvar alterações",
                            nomeInicial = modelo.titulo,
                            itensIniciais = itensDoModelo.map {
                                ItemConfiguracao(
                                    itemId = it.itemId,
                                    nome = it.nomeItem,
                                    quantidade = it.quantidade
                                )
                            },
                            itensDisponiveis = itens,
                            onBackClick = {
                                navController.popBackStack()
                            },
                            onCadastrarItem = { nome, onSuccess ->
                                itemsViewModel.cadastrarItem(nome, onSuccess)
                            },
                            onConfirmClick = { nome, itens ->
                                modelosViewModel.editarModelo(
                                    modeloId = modelo.id,
                                    titulo = nome,
                                    itens = itens,
                                    onSuccess = {
                                        navController.popBackStack()
                                    }
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
