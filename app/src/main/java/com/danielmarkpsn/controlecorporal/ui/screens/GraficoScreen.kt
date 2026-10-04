package com.danielmarkpsn.controlecorporal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.danielmarkpsn.controlecorporal.data.Medicao
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entryModelOf

@Composable
fun GraficoScreen(
    medicoes: List<Medicao>,
    modifier: Modifier = Modifier
) {
    val lista = remember(medicoes) {
        medicoes.sortedBy { it.data }
    }

    if (lista.size < 2) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("São necessárias pelo menos 2 medições para exibir o gráfico.")
        }
        return
    }

    // ✅ Converte os dados para o formato do Vico
    val entradasPeso = remember(lista) {
        lista.mapIndexed { index, medicao ->
            FloatEntry(x = index.toFloat(), y = medicao.peso)
        }
    }

    val entradasCintura = remember(lista) {
        lista.mapIndexed { index, medicao ->
            FloatEntry(x = index.toFloat(), y = medicao.cintura)
        }
    }

    val modeloPeso = remember(entradasPeso) { entryModelOf(entradasPeso) }
    val modeloCintura = remember(entradasCintura) { entryModelOf(entradasCintura) }

    val corPeso = MaterialTheme.colorScheme.primary
    val corCintura = MaterialTheme.colorScheme.secondary

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text("Evolução", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        // Gráfico de Peso
        Text("Peso (kg)", style = MaterialTheme.typography.titleMedium)
        Chart(
            chart = lineChart(
                lines = listOf(
                    LineChart.LineSpec(
                        lineColor = corPeso.value.toInt(),
                        lineThicknessDp = 3f
                    )
                )
            ),
            model = modeloPeso,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Gráfico de Cintura
        Text("Cintura (cm)", style = MaterialTheme.typography.titleMedium)
        Chart(
            chart = lineChart(
                lines = listOf(
                    LineChart.LineSpec(
                        lineColor = corCintura.value.toInt(),
                        lineThicknessDp = 3f
                    )
                )
            ),
            model = modeloCintura,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text("● Peso   ● Cintura", style = MaterialTheme.typography.bodyMedium)
    }
}
