package com.example.controleitens.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.controleitens.domain.model.Saida
import com.example.controleitens.domain.model.StatusSaida
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    saidas: List<Saida>,
    quantidadeItens: Map<String, Int>,
    onNovaSaidaClick: () -> Unit,
    onNovoModeloClick: () -> Unit,
    onCadastrarItemClick: () -> Unit,
    onVerTudoClick: () -> Unit,
    onSaidaClick: (String) -> Unit
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
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    onVerTudoClick()
                }
            )
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        // Saídas recentes
        saidas.take(6).forEach { saida ->

            SaidaCard(
                titulo = saida.titulo,
                data = formatarData(saida.dataCriacao),
                quantidadeItens = quantidadeItens[saida.id] ?: 0,
                concluida = saida.status == StatusSaida.FINALIZADA,
                onClick = {
                    onSaidaClick(saida.id)
                }
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )
        }
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
            .fillMaxWidth()
            .height(72.dp),
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.surfaceVariant
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
    concluida: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
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
                    text = "$data • $quantidadeItens ${
                        if (quantidadeItens == 1) "item" else "itens"
                    }",
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

private fun formatarData(timestamp: Long): String {
    val hoje = Calendar.getInstance()

    val data = Calendar.getInstance().apply {
        timeInMillis = timestamp
    }

    return when {
        hoje.get(Calendar.YEAR) == data.get(Calendar.YEAR) &&
                hoje.get(Calendar.DAY_OF_YEAR) == data.get(Calendar.DAY_OF_YEAR) -> {
            "Hoje"
        }

        hoje.get(Calendar.YEAR) == data.get(Calendar.YEAR) &&
                hoje.get(Calendar.DAY_OF_YEAR) - 1 == data.get(Calendar.DAY_OF_YEAR) -> {
            "Ontem"
        }

        else -> {
            SimpleDateFormat(
                "dd/MM",
                Locale("pt", "BR")
            ).format(Date(timestamp))
        }
    }
}