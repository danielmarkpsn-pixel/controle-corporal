package com.danielmarkpsn.controlecorporal.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import com.danielmarkpsn.controlecorporal.data.Medicao
import kotlin.math.max

@Composable
fun GraficoScreen(
    medicoes: List<Medicao>,
    modifier: Modifier = Modifier
) {
    val lista = medicoes.sortedBy { it.data }

    if (lista.size < 2) {
        Box(modifier = modifier.fillMaxSize()) {
            Text(
                "São necessárias pelo menos 2 medições para exibir o gráfico.",
                modifier = Modifier.padding(16.dp)
            )
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Evolução", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))
        Text("Peso (kg)", style = MaterialTheme.typography.titleMedium)
        LineChart(
            values = lista.map { it.peso },
            modifier = Modifier.fillMaxWidth().height(200.dp)
        )
        Spacer(Modifier.height(24.dp))
        Text("Cintura (cm)", style = MaterialTheme.typography.titleMedium)
        val cintura = lista.map { it.cintura }.filter { it > 0f }
        if (cintura.size >= 2) {
            LineChart(
                values = cintura,
                modifier = Modifier.fillMaxWidth().height(200.dp)
            )
        } else {
            Text("Ainda não há pelo menos duas medidas de cintura.", modifier = Modifier.padding(top = 8.dp))
        }
    }
}

@Composable
private fun LineChart(
    values: List<Float>,
    modifier: Modifier = Modifier
) {
    if (values.size < 2) return

    val primaryColor = MaterialTheme.colorScheme.primary

    Canvas(modifier = modifier.padding(vertical = 12.dp)) {
        val minValue = values.minOrNull() ?: return@Canvas
        val maxValue = values.maxOrNull() ?: return@Canvas
        val range = max(0.001f, maxValue - minValue)
        val xStep = size.width / (values.size - 1).coerceAtLeast(1)

        val path = Path()
        values.forEachIndexed { index, value ->
            val x = index * xStep
            val normalized = (value - minValue) / range
            val y = size.height - normalized * size.height
            if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }

        drawPath(
            path = path,
            color = primaryColor,
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 6f)
        )

        values.forEachIndexed { index, value ->
            val x = index * xStep
            val normalized = (value - minValue) / range
            val y = size.height - normalized * size.height
            drawCircle(
                color = primaryColor,
                radius = 7f,
                center = Offset(x, y)
            )
        }
    }
}
