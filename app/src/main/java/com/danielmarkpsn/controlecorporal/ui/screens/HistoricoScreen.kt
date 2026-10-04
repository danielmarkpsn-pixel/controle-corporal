package com.danielmarkpsn.controlecorporal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.danielmarkpsn.controlecorporal.data.Medicao
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoricoScreen(
    medicoes: List<Medicao>,
    onVoltar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lista = medicoes.sortedByDescending { it.data }
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Histórico") },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        if (lista.isEmpty()) {
            Box(
                Modifier.fillMaxSize().padding(padding),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text("Nenhuma medição registrada ainda.")
            }
        } else {
            LazyColumn(
                Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(lista) { medicao ->
                    Card(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(18.dp)) {
                            Row {
                                Icon(Icons.Default.FitnessCenter, contentDescription = null)
                                Spacer(Modifier.width(10.dp))
                                Column {
                                    Text(
                                        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                            .format(Date(medicao.data)),
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        "%.1f kg".format(medicao.peso),
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                }
                            }
                            Spacer(Modifier.height(14.dp))
                            MeasureLine("Cintura", medicao.cintura)
                            MeasureLine("Abdômen", medicao.abdomen)
                            MeasureLine("Quadril", medicao.quadril)
                            MeasureLine("Peito", medicao.peito)
                            MeasureLine("Braço direito", medicao.bracoDireito)
                            MeasureLine("Braço esquerdo", medicao.bracoEsquerdo)
                            MeasureLine("Coxa direita", medicao.coxaDireita)
                            MeasureLine("Coxa esquerda", medicao.coxaEsquerda)
                            MeasureLine("Panturrilha direita", medicao.panturrilhaDireita)
                            MeasureLine("Panturrilha esquerda", medicao.panturrilhaEsquerda)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MeasureLine(label: String, value: Float) {
    if (value <= 0f) return
    Row(
        Modifier.fillMaxWidth().padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
        Text("%.1f cm".format(value), fontWeight = FontWeight.Medium)
    }
}
