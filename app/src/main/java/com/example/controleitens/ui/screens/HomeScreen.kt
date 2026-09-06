package com.example.controleitens.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onNovaSaidaClick: () -> Unit,
    onNovoModeloClick: () -> Unit,
    onCadastrarItemClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Top
    ) {

        Text(
            text = "Controle de itens"
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Text(
            text = "Olá, Daniela! 👋",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Confira o que você precisa levar hoje.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        // Botões principais
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            HomeActionButton(
                icon = Icons.Default.PlaylistAdd,
                text = "Cadastrar Item",
                onClick = onCadastrarItemClick
            )

            HomeActionButton(
                icon = Icons.Default.ExitToApp,
                text = "Nova Saída",
                onClick = onNovaSaidaClick
            )

            HomeActionButton(
                icon = Icons.Default.ListAlt,
                text = "Novo Modelo",
                onClick = onNovoModeloClick
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        // Cards de estatísticas
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            StatCard(
                value = "2",
                line1 = "saídas",
                line2 = "pendentes",
                modifier = Modifier.weight(1f)
            )

            StatCard(
                value = "10",
                line1 = "itens",
                line2 = "conferidos",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        // Título da seção
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Saídas recentes",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "Ver tudo",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

// Dados provisórios
        SaidaCard(
            titulo = "Faculdade",
            data = "Hoje",
            quantidadeItens = 8,
            concluida = true
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        SaidaCard(
            titulo = "Academia",
            data = "Ontem",
            quantidadeItens = 5,
            concluida = true
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        SaidaCard(
            titulo = "Faculdade",
            data = "29/08",
            quantidadeItens = 7,
            concluida = false
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        SaidaCard(
            titulo = "Mercado",
            data = "28/08",
            quantidadeItens = 6,
            concluida = true
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        SaidaCard(
            titulo = "Academia",
            data = "27/08",
            quantidadeItens = 4,
            concluida = false
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        SaidaCard(
            titulo = "Faculdade",
            data = "26/08",
            quantidadeItens = 9,
            concluida = true
        )
    }
}


@Composable
private fun HomeActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .width(120.dp)
            .height(80.dp),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.primary
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = text,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}


@Composable
private fun StatCard(
    value: String,
    line1: String,
    line2: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(68.dp),
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = line1,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = line2,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}


@Composable
private fun SaidaCard(
    titulo: String,
    data: String,
    quantidadeItens: Int,
    concluida: Boolean
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp),
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "$data • $quantidadeItens itens",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = if (concluida) "Concluída" else "Não concluída",
                style = MaterialTheme.typography.labelMedium,
                color = if (concluida) {
                    MaterialTheme.colorScheme.tertiary
                } else {
                    MaterialTheme.colorScheme.error
                }
            )

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Abrir saída",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}