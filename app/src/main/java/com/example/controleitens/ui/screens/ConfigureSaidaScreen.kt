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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.controleitens.domain.model.Item
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.TextButton
import androidx.compose.material3.AlertDialog

data class ItemConfiguracao(
    val itemId: String,
    val nome: String,
    val quantidade: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigureSaidaScreen(
    titulo: String,
    textoBotao: String,
    itensDisponiveis: List<Item>,
    onBackClick: () -> Unit,
    onCadastrarItem: (String, (Item) -> Unit) -> Unit,
    onConfirmClick: (String, List<ItemConfiguracao>) -> Unit,
    nomeInicial: String = "",
    itensIniciais: List<ItemConfiguracao> = emptyList()
) {
    var nome by remember(nomeInicial) {
        mutableStateOf(nomeInicial)
    }

    var itens by remember(itensIniciais) {
        mutableStateOf(itensIniciais)
    }

    var mostrarBottomSheet by remember {
        mutableStateOf(false)
    }

    var mostrarCriarItem by remember {
        mutableStateOf(false)
    }

    var nomeNovoItem by remember {
        mutableStateOf("")
    }

    var itensSelecionados by remember {
        mutableStateOf(setOf<String>())
    }

    var quantidadesSelecionadas by remember {
        mutableStateOf(emptyMap<String, Int>())
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
                                    it.copy(quantidade = it.quantidade - 1)
                                } else {
                                    it
                                }
                            }
                        }
                    },
                    onAumentar = {
                        itens = itens.map {
                            if (it.itemId == item.itemId) {
                                it.copy(quantidade = it.quantidade + 1)
                            } else {
                                it
                            }
                        }
                    },
                    onExcluir = {
                        itens = itens.filter {
                            it.itemId != item.itemId
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
                            itensSelecionados = emptySet()
                            quantidadesSelecionadas = emptyMap()
                            mostrarBottomSheet = true
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
    if (mostrarBottomSheet) {
        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = false
        )

        ModalBottomSheet(
            onDismissRequest = {
                mostrarBottomSheet = false
                itensSelecionados = emptySet()
                quantidadesSelecionadas = emptyMap()
            },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {

                Text(
                    text = "Adicionar item",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                itensDisponiveis.forEach { itemDisponivel ->

                    val selecionado =
                        itemDisponivel.id in itensSelecionados

                    val quantidade =
                        quantidadesSelecionadas[itemDisponivel.id] ?: 1

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = if (selecionado) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        },
                        shape = MaterialTheme.shapes.small
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 8.dp,
                                    vertical = 4.dp
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Checkbox(
                                checked = selecionado,
                                onCheckedChange = { marcado ->

                                    if (marcado) {
                                        itensSelecionados =
                                            itensSelecionados + itemDisponivel.id

                                        quantidadesSelecionadas =
                                            quantidadesSelecionadas +
                                                    (itemDisponivel.id to 1)

                                    } else {
                                        itensSelecionados =
                                            itensSelecionados - itemDisponivel.id

                                        quantidadesSelecionadas =
                                            quantidadesSelecionadas -
                                                    itemDisponivel.id
                                    }
                                }
                            )

                            Text(
                                text = itemDisponivel.nome,
                                modifier = Modifier.weight(1f),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            if (selecionado) {

                                IconButton(
                                    onClick = {
                                        if (quantidade > 1) {
                                            quantidadesSelecionadas =
                                                quantidadesSelecionadas +
                                                        (
                                                                itemDisponivel.id to
                                                                        (quantidade - 1)
                                                                )
                                        }
                                    },
                                    enabled = quantidade > 1
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Remove,
                                        contentDescription = "Diminuir quantidade"
                                    )
                                }

                                Text(
                                    text = quantidade.toString(),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                IconButton(
                                    onClick = {
                                        quantidadesSelecionadas =
                                            quantidadesSelecionadas +
                                                    (
                                                            itemDisponivel.id to
                                                                    (quantidade + 1)
                                                            )
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Aumentar quantidade"
                                    )
                                }
                            }
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                OutlinedButton(
                    onClick = {
                        nomeNovoItem = ""
                        mostrarCriarItem = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("+ Criar novo item")
                }

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Button(
                    onClick = {

                        itensSelecionados.forEach { itemId ->

                            val itemDisponivel =
                                itensDisponiveis.firstOrNull {
                                    it.id == itemId
                                }

                            if (itemDisponivel != null) {

                                val quantidadeSelecionada =
                                    quantidadesSelecionadas[itemId] ?: 1

                                val itemExistente =
                                    itens.firstOrNull {
                                        it.itemId == itemId
                                    }

                                if (itemExistente != null) {

                                    itens = itens.map {
                                        if (it.itemId == itemId) {
                                            it.copy(
                                                quantidade =
                                                    it.quantidade +
                                                            quantidadeSelecionada
                                            )
                                        } else {
                                            it
                                        }
                                    }

                                } else {

                                    itens = itens + ItemConfiguracao(
                                        itemId = itemDisponivel.id,
                                        nome = itemDisponivel.nome,
                                        quantidade = quantidadeSelecionada
                                    )
                                }
                            }
                        }

                        itensSelecionados = emptySet()
                        quantidadesSelecionadas = emptyMap()
                        mostrarBottomSheet = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    enabled = itensSelecionados.isNotEmpty()
                ) {
                    Text("Adicionar")
                }
            }
        }
    }

    // Diálogo para criar novo item
    if (mostrarCriarItem) {
        AlertDialog(
            onDismissRequest = {
                mostrarCriarItem = false
            },
            title = {
                Text("Criar novo item")
            },
            text = {
                OutlinedTextField(
                    value = nomeNovoItem,
                    onValueChange = {
                        nomeNovoItem = it
                    },
                    label = {
                        Text("Nome do item")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val nomeItem = nomeNovoItem.trim()

                        if (nomeItem.isNotEmpty()) {
                            onCadastrarItem(nomeItem) { novoItem ->

                                itensSelecionados =
                                    itensSelecionados + novoItem.id

                                quantidadesSelecionadas =
                                    quantidadesSelecionadas +
                                            (novoItem.id to 1)

                                mostrarCriarItem = false
                                nomeNovoItem = ""
                            }
                        }
                    }
                ) {
                    Text("Adicionar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarCriarItem = false
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
    onAumentar: () -> Unit,
    onExcluir: () -> Unit
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
            IconButton(
                onClick = onExcluir
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Excluir item",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}