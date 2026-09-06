package com.example.controleitens.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.controleitens.ui.components.BottomBar
import com.example.controleitens.ui.screens.AboutScreen
import com.example.controleitens.ui.screens.HomeScreen
import com.example.controleitens.ui.screens.ItemsScreen
import com.example.controleitens.ui.screens.SettingsScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

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
                    }
                )
            }

            composable("itens") {
                ItemsScreen(
                    onCadastrarItemClick = {
                        // Por enquanto não faz nada
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
                Text("Nova saída")
            }

            composable("novo_modelo") {
                Text("Novo modelo")
            }
        }
    }
}