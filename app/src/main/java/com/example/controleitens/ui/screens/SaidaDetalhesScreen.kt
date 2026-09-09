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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.controleitens.domain.model.ItemSaida
import com.example.controleitens.domain.model.Saida
import java.text.SimpleDateFormat
import java.util.Date
import androidx.compose.ui.platform.LocalLocale
import com.example.controleitens.domain.model.Item
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Button

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaidaDetalhesScreen(
    saida: Saida,
    itens: List<ItemSaida>,
    itensDisponiveis: List<Item>,
    onBackClick: () -> Unit,
    onConferirItem: (String) -> Unit,
    onDesconferirItem: (String) -> Unit,
    onFinalizarSaida: () -> Unit,
    onExcluirItem: (String) -> Unit,
    onAdicionarItem: (String, String, String, Int) -> Unit,
    onCadastrarItem: (String, (Item) -> Unit) -> Unit
) {
    var mostrarAvisoFinalizacao by remember {
        mutableStateOf(false)
    }

    val saidaFinalizada = saida.status.name == "FINALIZADA"

    var mostrarBottomSheetAdicionar by remember {
        mutableStateOf(false)
    }

    var itensSelecionados by remember {
        mutableStateOf(setOf<String>())
    }

    var mostrarCriarItem by remember {
        mutableStateOf(false)
    }

    var nomeNovoItem by remember {
        mutableStateOf("")
    }
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
                        LocalLocale.current.platformLocale
                    ).format(Date(saida.dataCriacao)),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.size(4.dp))

                Text(
                    text = if (saidaFinalizada) {
                        "Concluída"
                    } else {
                        "Não concluída"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (saidaFinalizada) {
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
                        .weight(1f)
                        .padding(horizontal = 20.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

            } else {

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(
                        start = 20.dp,
                        top = 0.dp,
                        end = 20.dp,
                        bottom = 12.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = itens,
                        key = { it.id }
                    ) { item ->

                        ItemSaidaCard(
                            item = item,
                            habilitado = !saidaFinalizada,
                            onConferir = {
                                onConferirItem(item.id)
                            },
                            onDesconferir = {
                                onDesconferirItem(item.id)
                            },
                            onExcluir = {
                                onExcluirItem(item.id)
                            }
                        )
                    }
                }
                if (!saidaFinalizada) {
                    Text(
                        text = "+ Adicionar item",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                itensSelecionados = emptySet()
                                mostrarBottomSheetAdicionar = true
                            }
                            .padding(
                                horizontal = 20.dp,
                                vertical = 16.dp
                            ),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            if (!saidaFinalizada) {
                Button(
                    onClick = {
                        val existemItensNaoConferidos =
                            itens.any { !it.conferido }

                        if (existemItensNaoConferidos) {
                            mostrarAvisoFinalizacao = true
                        } else {
                            onFinalizarSaida()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 20.dp,
                            vertical = 12.dp
                        )
                ) {
                    Text("Finalizar saída")
                }
            }
        }
    }

    if (mostrarAvisoFinalizacao) {
        AlertDialog(
            onDismissRequest = {
                mostrarAvisoFinalizacao = false
            },
            title = {
                Text("Finalizar saída?")
            },
            text = {
                Text(
                    "Existem itens que ainda não foram conferidos. " +
                            "Deseja finalizar mesmo assim?"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarAvisoFinalizacao = false
                        onFinalizarSaida()
                    }
                ) {
                    Text("Finalizar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarAvisoFinalizacao = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
    if (mostrarBottomSheetAdicionar) {
        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = false
        )

        ModalBottomSheet(
            onDismissRequest = {
                mostrarBottomSheetAdicionar = false
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

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Selecione os itens que deseja adicionar.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

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

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = {
                        nomeNovoItem = ""
                        mostrarCriarItem = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Text("+ Criar novo item")
                }

                Button(
                    onClick = {
                        itensSelecionados.forEach { itemId ->

                            val item = itensDisponiveis.firstOrNull {
                                it.id == itemId
                            }

                            if (item != null) {
                                onAdicionarItem(
                                    saida.id,
                                    item.id,
                                    item.nome,
                                    1
                                )
                            }
                        }

                        itensSelecionados = emptySet()
                        mostrarBottomSheetAdicionar = false
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
                        val nome = nomeNovoItem.trim()

                        if (nome.isNotBlank()) {
                            onCadastrarItem(
                                nome
                            ) { novoItem ->
                                mostrarCriarItem = false

                                itensSelecionados =
                                    itensSelecionados + novoItem.id
                            }
                        }
                    },
                    enabled = nomeNovoItem.trim().isNotBlank()
                ) {
                    Text("Criar")
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
private fun ItemSaidaCard(
    item: ItemSaida,
    habilitado: Boolean,
    onConferir: () -> Unit,
    onDesconferir: () -> Unit,
    onExcluir: () -> Unit
) {
    var mostrarDialogoExcluir by remember { mutableStateOf(false) }

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
            if (habilitado) {
                IconButton(
                    onClick = {
                        mostrarDialogoExcluir = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Excluir item"
                    )
                }
            }

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
                    enabled = habilitado,
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
                    enabled = habilitado,
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
        if (mostrarDialogoExcluir) {
            AlertDialog(
                onDismissRequest = {
                    mostrarDialogoExcluir = false
                },
                title = {
                    Text("Excluir item?")
                },
                text = {
                    Text(
                        "O item \"${item.nomeItem}\" será removido desta saída."
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            mostrarDialogoExcluir = false
                            onExcluir()
                        }
                    ) {
                        Text(
                            text = "Excluir",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            mostrarDialogoExcluir = false
                        }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}