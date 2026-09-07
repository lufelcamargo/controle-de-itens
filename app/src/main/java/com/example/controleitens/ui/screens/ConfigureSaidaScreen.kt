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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.controleitens.domain.model.Item

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState

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
    onConfirmClick: (String, List<ItemConfiguracao>) -> Unit
) {
    var nome by remember {
        mutableStateOf("")
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
                            itensSelecionados = emptySet()
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
                mostrarCriarItem = false
                nomeNovoItem = ""
                itensSelecionados = emptySet()
            },
            sheetState = sheetState
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {

                Text(
                    text = "Adicionar itens",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                if (!mostrarCriarItem) {

                    Text(
                        text = "Selecione os itens que deseja adicionar.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = false)
                    ) {

                        items(
                            items = itensDisponiveis,
                            key = { it.id }
                        ) { itemDisponivel ->

                            val selecionado =
                                itemDisponivel.id in itensSelecionados

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        itensSelecionados =
                                            if (selecionado) {
                                                itensSelecionados - itemDisponivel.id
                                            } else {
                                                itensSelecionados + itemDisponivel.id
                                            }
                                    }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Checkbox(
                                    checked = selecionado,
                                    onCheckedChange = {
                                        itensSelecionados =
                                            if (selecionado) {
                                                itensSelecionados - itemDisponivel.id
                                            } else {
                                                itensSelecionados + itemDisponivel.id
                                            }
                                    }
                                )

                                Text(
                                    text = itemDisponivel.nome,
                                    modifier = Modifier.weight(1f),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    HorizontalDivider()

                    Spacer(
                        modifier = Modifier.height(8.dp)
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

                                    val itemExistente =
                                        itens.firstOrNull {
                                            it.itemId == itemDisponivel.id
                                        }

                                    if (itemExistente != null) {

                                        itens = itens.map {
                                            if (it.itemId == itemDisponivel.id) {
                                                it.copy(
                                                    quantidade =
                                                        it.quantidade + 1
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
                                }
                            }

                            itensSelecionados = emptySet()
                            mostrarBottomSheet = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        enabled = itensSelecionados.isNotEmpty()
                    ) {
                        Text("Adicionar")
                    }

                } else {

                    Text(
                        text = "Criar novo item",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    OutlinedTextField(
                        value = nomeNovoItem,
                        onValueChange = {
                            nomeNovoItem = it
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text("Nome do item")
                        },
                        singleLine = true
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        OutlinedButton(
                            onClick = {
                                mostrarCriarItem = false
                                nomeNovoItem = ""
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancelar")
                        }

                        Button(
                            onClick = {

                                onCadastrarItem(
                                    nomeNovoItem.trim()
                                ) { novoItem ->

                                    itensSelecionados =
                                        itensSelecionados + novoItem.id

                                    mostrarCriarItem = false
                                    nomeNovoItem = ""
                                }
                            },
                            modifier = Modifier.weight(1f),
                            enabled = nomeNovoItem.isNotBlank()
                        ) {
                            Text("Adicionar")
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )
                }
            }
        }
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