package com.danielmarkpsn.controlecorporal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.danielmarkpsn.controlecorporal.viewmodel.ControleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: ControleViewModel,
    onAdicionarPeso: () -> Unit,
    onAdicionarMedida: () -> Unit,
    onVerHistorico: () -> Unit,
    onVerMeta: () -> Unit,
    onVerGrafico: () -> Unit,
    onVerFotos: () -> Unit,
    onVerRelatorio: () -> Unit
) {
    val ultimoPeso by viewModel.ultimoPeso.collectAsStateWithLifecycle()
    val meta by viewModel.meta.collectAsStateWithLifecycle()
    val imc by viewModel.imcAtual.collectAsStateWithLifecycle()
    val classificacao by viewModel.classificacaoImc.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Controle Corporal", fontWeight = FontWeight.Bold)
                        Text(
                            "Acompanhe sua evolução",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onVerMeta) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar altura e meta")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAdicionarPeso) {
                Icon(Icons.Default.Add, contentDescription = "Registrar peso")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text(
                        "Seu peso atual",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        ultimoPeso?.let { "%.1f kg".format(it.pesoKg) } ?: "Nenhum registro",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        if (ultimoPeso != null) "Última pesagem registrada"
                        else "Registre seu primeiro peso",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InfoCard(
                    modifier = Modifier.weight(1f),
                    title = "IMC",
                    value = if (imc > 0f) "%.1f".format(imc) else "—",
                    subtitle = if (imc > 0f) classificacao else "Cadastre sua altura",
                    icon = { Icon(Icons.Default.FitnessCenter, contentDescription = null) }
                )
                InfoCard(
                    modifier = Modifier.weight(1f),
                    title = "Altura",
                    value = meta?.alturaCm?.takeIf { it > 0f }?.let { "%.0f cm".format(it) } ?: "—",
                    subtitle = if (meta?.alturaCm?.let { it > 0f } == true) "Altura cadastrada" else "Toque em editar",
                    icon = { Icon(Icons.Default.Height, contentDescription = null) }
                )
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Meta de peso", style = MaterialTheme.typography.labelLarge)
                            Text(
                                meta?.pesoAlvoKg?.takeIf { it > 0f }?.let { "%.1f kg".format(it) }
                                    ?: "Nenhuma meta definida",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Icon(
                            Icons.Default.Flag,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    val alvo = meta?.pesoAlvoKg ?: 0f
                    val atual = ultimoPeso?.pesoKg
                    if (alvo > 0f && atual != null) {
                        Spacer(Modifier.height(12.dp))
                        val progresso = (atual / alvo).coerceIn(0f, 1f)
                        LinearProgressIndicator(
                            progress = { progresso },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            if (atual <= alvo) "Meta atingida!" else "Faltam %.1f kg".format(atual - alvo),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Text(
                "Ações rápidas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FilledTonalButton(
                    onClick = onAdicionarMedida,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.FitnessCenter, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Medidas")
                }
                FilledTonalButton(
                    onClick = onVerGrafico,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.BarChart, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Evolução")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onVerFotos,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.AddAPhoto, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Fotos")
                }
                OutlinedButton(
                    onClick = onVerRelatorio,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.PictureAsPdf, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("PDF")
                }
            }

            OutlinedButton(
                onClick = onVerHistorico,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.History, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Ver histórico completo")
            }

            Spacer(Modifier.height(72.dp))
        }
    }
}

@Composable
private fun InfoCard(
    modifier: Modifier,
    title: String,
    value: String,
    subtitle: String,
    icon: @Composable () -> Unit
) {
    Card(modifier = modifier) {
        Column(Modifier.padding(16.dp)) {
            icon()
            Spacer(Modifier.height(8.dp))
            Text(title, style = MaterialTheme.typography.labelLarge)
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(
                subtitle,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2
            )
        }
    }
}
