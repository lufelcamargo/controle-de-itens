package com.example.controleitens.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.controleitens.domain.model.ItemSaida
import com.example.controleitens.domain.model.Saida
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaidaDetalhesScreen(
    saida: Saida,
    itens: List<ItemSaida>,
    onBackClick: () -> Unit,
    onConferirItem: (String) -> Unit,
    onDesconferirItem: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(saida.titulo)
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Text(
                    text = SimpleDateFormat(
                        "dd/MM/yyyy HH:mm",
                        Locale.getDefault()
                    ).format(Date(saida.dataCriacao)),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.size(4.dp))

                Text(
                    text = if (saida.status.name == "FINALIZADA") {
                        "Concluída"
                    } else {
                        "Não concluída"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (saida.status.name == "FINALIZADA") {
                        MaterialTheme.colorScheme.tertiary
                    } else {
                        MaterialTheme.colorScheme.error
                    }
                )

                Spacer(modifier = Modifier.size(20.dp))

                Text(
                    text = "Itens",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            if (itens.isEmpty()) {

                Text(
                    text = "Nenhum item nesta saída.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

            } else {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 20.dp,
                        top = 0.dp,
                        end = 20.dp,
                        bottom = 20.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = itens,
                        key = { it.id }
                    ) { item ->

                        ItemSaidaCard(
                            item = item,
                            onConferir = {
                                onConferirItem(item.id)
                            },
                            onDesconferir = {
                                onDesconferirItem(item.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ItemSaidaCard(
    item: ItemSaida,
    onConferir: () -> Unit,
    onDesconferir: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 12.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.nomeItem,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Quantidade: ${item.quantidade}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = " • ",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = if (item.conferido) {
                            "Conferido"
                        } else {
                            "Não conferido"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = if (item.conferido) {
                            MaterialTheme.colorScheme.tertiary
                        } else {
                            MaterialTheme.colorScheme.error
                        }
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Surface(
                    onClick = onConferir,
                    modifier = Modifier.size(40.dp),
                    shape = MaterialTheme.shapes.small,
                    color = if (item.conferido) {
                        MaterialTheme.colorScheme.tertiary.copy(alpha = 0.20f)
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0f)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Confirmar item",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        tint = if (item.conferido) {
                            MaterialTheme.colorScheme.tertiary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }

                Surface(
                    onClick = onDesconferir,
                    modifier = Modifier.size(40.dp),
                    shape = MaterialTheme.shapes.small,
                    color = if (!item.conferido) {
                        MaterialTheme.colorScheme.error.copy(alpha = 0.20f)
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0f)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Não confirmar item",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        tint = if (!item.conferido) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            }
        }
    }
}