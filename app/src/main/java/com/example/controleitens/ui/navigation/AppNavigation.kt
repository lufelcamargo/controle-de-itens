package com.example.controleitens.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "inicio"
    ) {

        composable("inicio") {
            TelaInicial(
                onItensClick = {
                    navController.navigate("itens")
                },
                onSaidasClick = {
                    navController.navigate("saidas")
                },
                onModelosClick = {
                    navController.navigate("modelos")
                }
            )
        }

        composable("itens") {
            Text("Tela de Itens")
        }

        composable("saidas") {
            Text("Tela de Saídas")
        }

        composable("modelos") {
            Text("Tela de Modelos")
        }
    }
}

@Composable
private fun TelaInicial(
    onItensClick: () -> Unit,
    onSaidasClick: () -> Unit,
    onModelosClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Controle de Itens")

        Button(onClick = onItensClick) {
            Text("Itens")
        }

        Button(onClick = onSaidasClick) {
            Text("Saídas")
        }

        Button(onClick = onModelosClick) {
            Text("Modelos")
        }
    }
}