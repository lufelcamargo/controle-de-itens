package com.example.controleitens.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.controleitens.domain.model.Saida
import com.example.controleitens.domain.model.StatusSaida
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaidasScreen(
    saidas: List<Saida>,
    quantidadeItens: Map<String, Int>,
    onBackClick: () -> Unit,
    onSaidaClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Saídas"
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        if (saidas.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Nenhuma saída cadastrada.",
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    horizontal = 20.dp,
                    vertical = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    items = saidas,
                    key = { it.id }
                ) { saida ->

                    SaidaCard(
                        saida = saida,
                        quantidade = quantidadeItens[saida.id] ?: 0,
                        onClick = {
                            onSaidaClick(saida.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SaidaCard(
    saida: Saida,
    quantidade: Int,
    onClick: () -> Unit
) {
    val statusConcluida = saida.status == StatusSaida.FINALIZADA

    val statusTexto = if (statusConcluida) {
        "Concluída"
    } else {
        "Não concluída"
    }

    val statusCor = if (statusConcluida) {
        androidx.compose.material3.MaterialTheme.colorScheme.tertiary
    } else {
        androidx.compose.material3.MaterialTheme.colorScheme.error
    }

    val data = formatarData(saida.dataCriacao)

    androidx.compose.material3.Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = 16.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = saida.titulo,
                    style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                )

                Spacer(
                    modifier = Modifier.height(2.dp)
                )

                Text(
                    text = "$data • $quantidade ${if (quantidade == 1) "item" else "itens"}",
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = statusTexto,
                style = androidx.compose.material3.MaterialTheme.typography.labelLarge,
                color = statusCor
            )

            Spacer(
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Abrir saída",
                tint = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun formatarData(timestamp: Long): String {
    val hoje = SimpleDateFormat(
        "dd/MM",
        Locale("pt", "BR")
    ).format(Date(timestamp))

    val agora = System.currentTimeMillis()

    val calendarioHoje = java.util.Calendar.getInstance()
    val calendarioData = java.util.Calendar.getInstance().apply {
        timeInMillis = timestamp
    }

    return when {
        calendarioHoje.get(java.util.Calendar.YEAR) == calendarioData.get(java.util.Calendar.YEAR) &&
                calendarioHoje.get(java.util.Calendar.DAY_OF_YEAR) == calendarioData.get(java.util.Calendar.DAY_OF_YEAR) -> {
            "Hoje"
        }

        calendarioHoje.apply {
            add(java.util.Calendar.DAY_OF_YEAR, -1)
        }.get(java.util.Calendar.YEAR) == calendarioData.get(java.util.Calendar.YEAR) &&
                calendarioHoje.get(java.util.Calendar.DAY_OF_YEAR) == calendarioData.get(java.util.Calendar.DAY_OF_YEAR) -> {
            "Ontem"
        }

        else -> {
            hoje
        }
    }
}