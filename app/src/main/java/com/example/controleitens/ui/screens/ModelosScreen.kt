package com.example.controleitens.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.controleitens.domain.model.Modelo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModelosScreen(
    modelos: List<Modelo>,
    onBackClick: () -> Unit,
    onModeloClick: (String) -> Unit,
    onNovoModeloClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Modelos")
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNovoModeloClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                },
                text = {
                    Text("Criar modelo")
                }
            )
        }
    ) { innerPadding ->

        if (modelos.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Nenhum modelo cadastrado.",
                    color = androidx.compose.material3.MaterialTheme
                        .colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = modelos,
                    key = { it.id }
                ) { modelo ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onModeloClick(modelo.id)
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = androidx.compose.material3.MaterialTheme
                                .colorScheme.surfaceVariant
                        )
                    ) {
                        androidx.compose.foundation.layout.Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 16.dp,
                                    vertical = 18.dp
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = modelo.titulo,
                                modifier = Modifier.weight(1f),
                                color = androidx.compose.material3.MaterialTheme
                                    .colorScheme.onSurface,
                                style = androidx.compose.material3.MaterialTheme
                                    .typography.titleMedium
                            )

                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = "Abrir modelo",
                                tint = androidx.compose.material3.MaterialTheme
                                    .colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}