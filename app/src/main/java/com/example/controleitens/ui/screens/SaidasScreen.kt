package com.example.controleitens.ui.screens

import androidx.compose.foundation.combinedClickable
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.controleitens.domain.model.Saida
import com.example.controleitens.domain.model.StatusSaida
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaidasScreen(
    saidas: List<Saida>,
    quantidadeItens: Map<String, Int>,
    onBackClick: () -> Unit,
    onSaidaClick: (String) -> Unit,
    onExcluirSaidas: (List<String>) -> Unit
) {
    var modoSelecao by remember {
        mutableStateOf(false)
    }

    var selecionadas by remember {
        mutableStateOf(setOf<String>())
    }

    var mostrarDialogoExclusao by remember {
        mutableStateOf(false)
    }

    fun alternarSelecao(id: String) {
        selecionadas = if (id in selecionadas) {
            selecionadas - id
        } else {
            selecionadas + id
        }

        if (selecionadas.isEmpty()) {
            modoSelecao = false
        }
    }

    fun sairDoModoSelecao() {
        modoSelecao = false
        selecionadas = emptySet()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (modoSelecao) {
                        Text(
                            text = "${selecionadas.size} selecionada" +
                                    if (selecionadas.size == 1) {
                                        ""
                                    } else {
                                        "s"
                                    }
                        )
                    } else {
                        Text("Saídas")
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (modoSelecao) {
                                sairDoModoSelecao()
                            } else {
                                onBackClick()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = if (modoSelecao) {
                                "Cancelar seleção"
                            } else {
                                "Voltar"
                            }
                        )
                    }
                },
                actions = {
                    if (modoSelecao && selecionadas.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                mostrarDialogoExclusao = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Excluir selecionadas"
                            )
                        }
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
                    color = MaterialTheme.colorScheme.onSurfaceVariant
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
                        selecionada = saida.id in selecionadas,
                        modoSelecao = modoSelecao,
                        onClick = {
                            if (modoSelecao) {
                                alternarSelecao(saida.id)
                            } else {
                                onSaidaClick(saida.id)
                            }
                        },
                        onLongClick = {
                            if (!modoSelecao) {
                                modoSelecao = true
                            }

                            alternarSelecao(saida.id)
                        }
                    )
                }
            }
        }
    }

    if (mostrarDialogoExclusao) {
        AlertDialog(
            onDismissRequest = {
                mostrarDialogoExclusao = false
            },
            title = {
                Text(
                    text = if (selecionadas.size == 1) {
                        "Excluir saída?"
                    } else {
                        "Excluir saídas?"
                    }
                )
            },
            text = {
                Text(
                    text = if (selecionadas.size == 1) {
                        "A saída selecionada será excluída permanentemente."
                    } else {
                        "As ${selecionadas.size} saídas selecionadas serão " +
                                "excluídas permanentemente."
                    }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarDialogoExclusao = false

                        onExcluirSaidas(
                            selecionadas.toList()
                        )

                        sairDoModoSelecao()
                    }
                ) {
                    Text("Excluir")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarDialogoExclusao = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun SaidaCard(
    saida: Saida,
    quantidade: Int,
    selecionada: Boolean,
    modoSelecao: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val statusConcluida = saida.status == StatusSaida.FINALIZADA

    val statusTexto = if (statusConcluida) {
        "Concluída"
    } else {
        "Não concluída"
    }

    val statusCor = if (statusConcluida) {
        MaterialTheme.colorScheme.tertiary
    } else {
        MaterialTheme.colorScheme.error
    }

    val data = formatarData(saida.dataCriacao)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        color = if (selecionada) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        },
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

            if (selecionada) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selecionada",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = 12.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = saida.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(
                    modifier = Modifier.height(2.dp)
                )

                Text(
                    text = "$data • $quantidade ${
                        if (quantidade == 1) "item" else "itens"
                    }",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (!modoSelecao) {
                Text(
                    text = statusTexto,
                    style = MaterialTheme.typography.labelLarge,
                    color = statusCor
                )

                Spacer(
                    modifier = Modifier.padding(horizontal = 4.dp)
                )

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Abrir saída",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun formatarData(timestamp: Long): String {
    val hoje = SimpleDateFormat(
        "dd/MM",
        Locale("pt", "BR")
    ).format(Date(timestamp))

    val calendarioHoje = java.util.Calendar.getInstance()
    val calendarioData = java.util.Calendar.getInstance().apply {
        timeInMillis = timestamp
    }

    return when {
        calendarioHoje.get(java.util.Calendar.YEAR) ==
                calendarioData.get(java.util.Calendar.YEAR) &&
                calendarioHoje.get(java.util.Calendar.DAY_OF_YEAR) ==
                calendarioData.get(java.util.Calendar.DAY_OF_YEAR) -> {
            "Hoje"
        }

        calendarioHoje.apply {
            add(java.util.Calendar.DAY_OF_YEAR, -1)
        }.get(java.util.Calendar.YEAR) ==
                calendarioData.get(java.util.Calendar.YEAR) &&
                calendarioHoje.get(java.util.Calendar.DAY_OF_YEAR) ==
                calendarioData.get(java.util.Calendar.DAY_OF_YEAR) -> {
            "Ontem"
        }

        else -> {
            hoje
        }
    }
}