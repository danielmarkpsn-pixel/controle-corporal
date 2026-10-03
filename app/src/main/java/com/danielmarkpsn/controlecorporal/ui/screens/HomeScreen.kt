package com.danielmarkpsn.controlecorporal.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
    onVerGrafico: () -> Unit
) {
    val ultimoPeso by viewModel.ultimoPeso.collectAsStateWithLifecycle()
    val meta by viewModel.meta.collectAsStateWithLifecycle()
    val imc by viewModel.imcAtual.collectAsStateWithLifecycle()
    val classificacao by viewModel.classificacaoImc.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Controle Corporal") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Cartão do último peso
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Último peso", style = MaterialTheme.typography.labelSmall)
                    Text(
                        text = ultimoPeso?.let { "%.1f kg".format(it.pesoKg) } ?: "—",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Cartão do IMC
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("IMC", style = MaterialTheme.typography.labelSmall)
                    Text(
                        text = if (imc > 0f) "%.1f".format(imc) else "—",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(classificacao, style = MaterialTheme.typography.bodyMedium)
                }
            }

            // Cartão da meta
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Meta", style = MaterialTheme.typography.labelSmall)
                    if (meta != null) {
                        Text(
                            text = "%.1f kg".format(meta!!.pesoAlvoKg),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        val diferenca = (ultimoPeso?.pesoKg ?: 0f) - meta!!.pesoAlvoKg
                        if (ultimoPeso != null) {
                            Text(
                                text = if (diferenca > 0) "Faltam %.1f kg".format(diferenca)
                                else "Meta atingida!",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    } else {
                        Text("Nenhuma meta definida", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            // Botões de ação
            Button(onClick = onAdicionarPeso, modifier = Modifier.fillMaxWidth()) {
                Text("Registrar peso")
            }

            Button(onClick = onAdicionarMedida, modifier = Modifier.fillMaxWidth()) {
                Text("Registrar medida")
            }

            OutlinedButton(onClick = onVerGrafico, modifier = Modifier.fillMaxWidth()) {
                Text("Ver gráfico de evolução")
            }

            OutlinedButton(onClick = onVerMeta, modifier = Modifier.fillMaxWidth()) {
                Text("Definir meta")
            }

            OutlinedButton(onClick = onVerHistorico, modifier = Modifier.fillMaxWidth()) {
                Text("Ver histórico")
            }
        }
    }
}
