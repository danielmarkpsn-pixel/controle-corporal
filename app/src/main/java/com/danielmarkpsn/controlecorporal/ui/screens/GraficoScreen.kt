package com.danielmarkpsn.controlecorporal.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import com.danielmarkpsn.controlecorporal.data.Medicao
import kotlin.math.max

private enum class GraficoTipo(val titulo: String) {
    PESO("Peso (kg)"),
    CINTURA("Cintura (cm)"),
    BRACOS("Braços — direita x esquerda"),
    COXAS("Coxas — direita x esquerda"),
    PANTURRILHAS("Panturrilhas — direita x esquerda")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GraficoScreen(medicoes: List<Medicao>, onVoltar: () -> Unit, modifier: Modifier = Modifier) {
    val lista = medicoes.sortedBy { it.data }
    var tipo by remember { mutableStateOf(GraficoTipo.PESO) }
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Evolução") },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            if (lista.size < 2) {
                Text("Registre pelo menos duas pesagens para visualizar sua evolução.")
                return@Column
            }
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                OutlinedTextField(
                    value = tipo.titulo, onValueChange = {}, readOnly = true,
                    label = { Text("Indicador") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    GraficoTipo.entries.forEach {
                        DropdownMenuItem(text = { Text(it.titulo) }, onClick = { tipo = it; expanded = false })
                    }
                }
            }
            Spacer(Modifier.height(20.dp))
            when (tipo) {
                GraficoTipo.PESO -> MetricChart("Peso", lista.map { it.peso })
                GraficoTipo.CINTURA -> MetricChart("Cintura", lista.map { it.cintura })
                GraficoTipo.BRACOS -> DualChart("Braço direito", lista.map { it.bracoDireito }, "Braço esquerdo", lista.map { it.bracoEsquerdo })
                GraficoTipo.COXAS -> DualChart("Coxa direita", lista.map { it.coxaDireita }, "Coxa esquerda", lista.map { it.coxaEsquerda })
                GraficoTipo.PANTURRILHAS -> DualChart("Panturrilha direita", lista.map { it.panturrilhaDireita }, "Panturrilha esquerda", lista.map { it.panturrilhaEsquerda })
            }
        }
    }
}

@Composable
private fun MetricChart(title: String, values: List<Float>) {
    Text(title, style = MaterialTheme.typography.titleMedium)
    val valid = values.filter { it > 0f }
    if (valid.size >= 2) LineChart(valid) else Text("Ainda não existem dados suficientes.")
}

@Composable
private fun DualChart(firstTitle: String, first: List<Float>, secondTitle: String, second: List<Float>) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        AssistChip(onClick = {}, label = { Text(firstTitle) })
        AssistChip(onClick = {}, label = { Text(secondTitle) })
    }
    Spacer(Modifier.height(8.dp))
    val f = first.filter { it > 0f }
    val s = second.filter { it > 0f }
    if (f.size >= 2 || s.size >= 2) DualLineChart(f, s) else Text("Ainda não existem dados suficientes para comparar os lados.")
}

@Composable
private fun LineChart(values: List<Float>) {
    val primary = MaterialTheme.colorScheme.primary
    Card(Modifier.fillMaxWidth().height(260.dp)) {
        Canvas(Modifier.fillMaxSize().padding(16.dp)) { drawSeries(values, primary) }
    }
}

@Composable
private fun DualLineChart(first: List<Float>, second: List<Float>) {
    val primary = MaterialTheme.colorScheme.primary
    val secondary = MaterialTheme.colorScheme.secondary
    Card(Modifier.fillMaxWidth().height(260.dp)) {
        Canvas(Modifier.fillMaxSize().padding(16.dp)) {
            drawSeries(first, primary)
            drawSeries(second, secondary)
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawSeries(
    values: List<Float>, color: androidx.compose.ui.graphics.Color
) {
    if (values.size < 2) return
    val minValue = values.minOrNull() ?: return
    val maxValue = values.maxOrNull() ?: return
    val range = max(0.001f, maxValue - minValue)
    val xStep = size.width / (values.size - 1).coerceAtLeast(1)
    val path = Path()
    values.forEachIndexed { index, value ->
        val x = index * xStep
        val y = size.height - ((value - minValue) / range) * size.height
        if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
        drawCircle(color, 6f, Offset(x, y))
    }
    drawPath(path, color, style = androidx.compose.ui.graphics.drawscope.Stroke(5f))
}
