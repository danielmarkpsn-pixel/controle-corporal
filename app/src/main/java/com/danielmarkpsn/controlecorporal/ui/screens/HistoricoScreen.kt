package com.danielmarkpsn.controlecorporal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.danielmarkpsn.controlecorporal.viewmodel.ControleViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoricoScreen(
    viewModel: ControleViewModel,
    onVoltar: () -> Unit
) {
    val pesos by viewModel.pesos.collectAsStateWithLifecycle()
    val formatador = remember { SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Histórico") },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        if (pesos.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Nenhum registro ainda")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(pesos, key = { it.id }) { peso ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "%.1f kg".format(peso.pesoKg),
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Text(
                                    text = formatador.format(Date(peso.data)),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                peso.observacao?.let {
                                    Text(text = it, style = MaterialTheme.typography.labelSmall)
                                }
                            }
                            IconButton(onClick = { viewModel.removerPeso(peso) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Remover")
                            }
                        }
                    }
                }
            }
        }
    }
}
