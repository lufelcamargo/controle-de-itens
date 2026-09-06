package com.example.controleitens.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ExtendedFloatingActionButton

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

import androidx.compose.material3.AlertDialog

@Composable
fun ItemsScreen(

    onCadastrarItemClick: () -> Unit
) {
    var mostrarDialogoCadastro by remember {
        mutableStateOf(false)
    }

    var nomeNovoItem by remember {
        mutableStateOf("")
    }

    val itens = remember {
        listOf(
            "Caderno",
            "Carteira",
            "Carregador",
            "Chaves",
            "Fone de ouvido",
            "Mochila"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        Text(
            text = "Itens",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("Buscar itens...")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar"
                )
            },
            singleLine = true
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Seus itens",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            TextButton(
                onClick = {}
            ) {
                Text("A–Z")

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Ordenação"
                )
            }
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    bottom = 88.dp
                )
            ) {
                items(itens) { item ->
                    ItemRow(
                        nome = item,
                        onClick = {}
                    )
                }
            }

            ExtendedFloatingActionButton(
                onClick = {
                    nomeNovoItem = ""
                    mostrarDialogoCadastro = true
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 8.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Cadastrar item"
                    )
                },
                text = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Cadastrar",
                            style = MaterialTheme.typography.labelLarge
                        )

                        Text(
                            text = "Item",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            )
        }

    }
    if (mostrarDialogoCadastro) {
        AlertDialog(
            onDismissRequest = {
                mostrarDialogoCadastro = false
            },
            title = {
                Text("Cadastrar item")
            },
            text = {
                OutlinedTextField(
                    value = nomeNovoItem,
                    onValueChange = {
                        nomeNovoItem = it
                    },
                    label = {
                        Text("Nome")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarDialogoCadastro = false
                    }
                ) {
                    Text("Cancelar")
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarDialogoCadastro = false
                    },
                    enabled = nomeNovoItem.isNotBlank()
                ) {
                    Text("Cadastrar")
                }
            }
        )
    }
}

@Composable
private fun ItemRow(
    nome: String,
    onClick: () -> Unit
) {
    androidx.compose.material3.Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = nome,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Abrir item",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}