package com.example.controleitens.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.controleitens.domain.model.Item

data class ItemConfiguracao(
    val itemId: String,
    val nome: String,
    val quantidade: Int
)

@Composable
fun ConfigureSaidaScreen(
    titulo: String,
    textoBotao: String,
    itensDisponiveis: List<Item>,
    onBackClick: () -> Unit,
    onConfirmClick: (String, List<ItemConfiguracao>) -> Unit
) {
    var nome by remember {
        mutableStateOf("")
    }

    var mostrarDialogoItens by remember {
        mutableStateOf(false)
    }

    // Começa vazia.
    var itens by remember {
        mutableStateOf(emptyList<ItemConfiguracao>())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        // Cabeçalho
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Voltar",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            Text(
                text = titulo,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        // Nome
        OutlinedTextField(
            value = nome,
            onValueChange = {
                nome = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(
                    if (titulo == "Nova saída") {
                        "Título"
                    } else {
                        "Nome"
                    }
                )
            },
            singleLine = true
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Text(
            text = "Itens",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        // Lista
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(
                items = itens,
                key = { it.itemId }
            ) { item ->

                ItemConfiguracaoRow(
                    item = item,
                    onDiminuir = {
                        if (item.quantidade > 1) {
                            itens = itens.map {
                                if (it.itemId == item.itemId) {
                                    it.copy(
                                        quantidade = it.quantidade - 1
                                    )
                                } else {
                                    it
                                }
                            }
                        }
                    },
                    onAumentar = {
                        itens = itens.map {
                            if (it.itemId == item.itemId) {
                                it.copy(
                                    quantidade = it.quantidade + 1
                                )
                            } else {
                                it
                            }
                        }
                    }
                )
            }

            item {
                Text(
                    text = "+ Adicionar item",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .clickable {
                            mostrarDialogoItens = true
                        },
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        // Botão inferior
        Button(
            onClick = {
                onConfirmClick(
                    nome.trim(),
                    itens
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            enabled = nome.isNotBlank()
        ) {
            Text(textoBotao)
        }
    }

    // Diálogo para adicionar item
    if (mostrarDialogoItens) {
        AlertDialog(
            onDismissRequest = {
                mostrarDialogoItens = false
            },
            title = {
                Text("Adicionar item")
            },
            text = {
                Column {
                    itensDisponiveis.forEach { itemDisponivel ->

                        val itemExistente = itens.firstOrNull {
                            it.itemId == itemDisponivel.id
                        }

                        Text(
                            text = if (itemExistente != null) {
                                "${itemDisponivel.nome} (${itemExistente.quantidade})"
                            } else {
                                itemDisponivel.nome
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {

                                    if (itemExistente != null) {

                                        itens = itens.map {
                                            if (it.itemId == itemDisponivel.id) {
                                                it.copy(
                                                    quantidade = it.quantidade + 1
                                                )
                                            } else {
                                                it
                                            }
                                        }

                                    } else {

                                        itens = itens + ItemConfiguracao(
                                            itemId = itemDisponivel.id,
                                            nome = itemDisponivel.nome,
                                            quantidade = 1
                                        )
                                    }

                                    mostrarDialogoItens = false
                                }
                                .padding(
                                    horizontal = 8.dp,
                                    vertical = 12.dp
                                ),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarDialogoItens = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun ItemConfiguracaoRow(
    item: ItemConfiguracao,
    onDiminuir: () -> Unit,
    onAumentar: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.small
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

            Text(
                text = item.nome,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            IconButton(
                onClick = onDiminuir,
                enabled = item.quantidade > 1
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Diminuir quantidade"
                )
            }

            Text(
                text = item.quantidade.toString(),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            IconButton(
                onClick = onAumentar
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Aumentar quantidade"
                )
            }
        }
    }
}