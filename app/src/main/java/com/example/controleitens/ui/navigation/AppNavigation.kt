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

    // Repositório de itens
    val itemRepository = ItemRepositoryImpl(
        database.itemDao()
    )

    // ViewModel de itens
    val itemsViewModel: ItemsViewModel = viewModel(
        factory = ItemsViewModelFactory(itemRepository)
    )

    val itens by itemsViewModel.itens.collectAsState()

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
                        navController.navigate(route) {
                            popUpTo("inicio") {
                                saveState = true
                            }

                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = "inicio",
            modifier = Modifier
                .padding(innerPadding)
                .statusBarsPadding()
        ) {

            // ---------------------------------------------------------
            // HOME
            // ---------------------------------------------------------

            composable("inicio") {
                HomeScreen(
                    saidas = saidas,
                    quantidadeItens = quantidadeItens,
                    onNovaSaidaClick = {
                        navController.navigate("nova_saida")
                    },
                    onNovoModeloClick = {
                        navController.navigate("novo_modelo")
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
                        onBackClick = {
                            navController.popBackStack()
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
                    onSobreClick = {
                        navController.navigate("sobre")
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
                    onConfirmClick = { nome, itens ->
                        // Por enquanto não faz nada
                    }
                )
            }
        }
    }
}