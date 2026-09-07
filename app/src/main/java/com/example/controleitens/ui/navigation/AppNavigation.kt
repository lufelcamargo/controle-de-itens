package com.example.controleitens.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.controleitens.ui.components.BottomBar
import com.example.controleitens.ui.screens.AboutScreen
import com.example.controleitens.ui.screens.ConfigureSaidaScreen
import com.example.controleitens.ui.screens.HomeScreen
import com.example.controleitens.ui.screens.ItemsScreen
import com.example.controleitens.ui.screens.SettingsScreen

import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.controleitens.data.local.database.DatabaseProvider
import com.example.controleitens.data.local.repository.ItemRepositoryImpl
import com.example.controleitens.ui.viewmodel.ItemsViewModel
import com.example.controleitens.ui.viewmodel.ItemsViewModelFactory

import com.example.controleitens.data.local.repository.SaidaRepositoryImpl
import com.example.controleitens.data.local.repository.ItemSaidaRepositoryImpl
import com.example.controleitens.ui.screens.SaidasScreen
import com.example.controleitens.ui.viewmodel.SaidasViewModel
import com.example.controleitens.ui.viewmodel.SaidasViewModelFactory


@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val context = LocalContext.current

    val database = DatabaseProvider.getDatabase(context)

    val saidaRepository = SaidaRepositoryImpl(database.saidaDao())
    val itemSaidaRepository = ItemSaidaRepositoryImpl(database.itemSaidaDao())

    val saidasViewModel: SaidasViewModel = viewModel(
        factory = SaidasViewModelFactory(
            saidaRepository,
            itemSaidaRepository
        )
    )

    val saidas by saidasViewModel.saidas.collectAsState()

    val quantidadeItens by saidasViewModel.quantidadeItens.collectAsState()

    val itemRepository = ItemRepositoryImpl(
        database.itemDao()
    )

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

            composable("inicio") {
                HomeScreen(
                    onCadastrarItemClick = {
                        // Por enquanto não faz nada
                    },
                    onNovaSaidaClick = {
                        navController.navigate("nova_saida")
                    },
                    onNovoModeloClick = {
                        navController.navigate("novo_modelo")
                    },
                    onVerTudoClick = {
                        navController.navigate("saidas")
                    }
                )
            }
            composable("saidas") {
                SaidasScreen(
                    saidas = saidas,
                    quantidadeItens = quantidadeItens,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onSaidaClick = { id ->
                        // Vamos implementar a tela de detalhes depois
                    }
                )
            }
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

            composable("ajustes") {
                SettingsScreen(
                    onSobreClick = {
                        navController.navigate("sobre")
                    }
                )
            }

            composable("sobre") {
                AboutScreen()
            }

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