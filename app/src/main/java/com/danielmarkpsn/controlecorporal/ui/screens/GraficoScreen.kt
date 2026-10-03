package com.danielmarkpsn.controlecorporal.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import com.danielmarkpsn.controlecorporal.viewmodel.ControleViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun GraficoScreen(viewModel: ControleViewModel, onVoltar: () -> Unit) {
    val pesos by viewModel.pesos.collectAsState()

    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Evolução do peso", style = MaterialTheme.typography.headlineSmall)
            Button(onClick = onVoltar) { Text("Voltar") }
        }

        Spacer(Modifier.height(16.dp))

        if (pesos.isEmpty()) {
            Text("Ainda não há registros de peso para montar o gráfico.")
        } else {
            val dados = pesos.sortedBy { it.data }.takeLast(30)
            val min = dados.minOf { it.pesoKg }
            val max = dados.maxOf { it.pesoKg }
            val margem = ((max - min) * 0.15f).coerceAtLeast(1f)
            val escalaMin = min - margem
            val escalaMax = max + margem

            Canvas(Modifier.fillMaxWidth().height(300.dp)) {
                val left = 50f
                val top = 20f
                val right = size.width - 20f
                val bottom = size.height - 35f
                val width = (right - left).coerceAtLeast(1f)
                val height = (bottom - top).coerceAtLeast(1f)

                repeat(5) { i ->
                    val y = top + height * i / 4f
                    drawLine(
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
                        start = Offset(left, y),
                        end = Offset(right, y),
                        strokeWidth = 1f
                    )
                }

                if (dados.size > 1) {
                    val path = Path()
                    dados.forEachIndexed { index, item ->
                        val x = left + width * index / dados.lastIndex.toFloat()
                        val normalized = ((item.pesoKg - escalaMin) / (escalaMax - escalaMin)).coerceIn(0f, 1f)
                        val y = bottom - normalized * height
                        if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
                        drawCircle(
                            color = MaterialTheme.colorScheme.primary,
                            radius = 5f,
                            center = Offset(x, y)
                        )
                    }
                    drawPath(path = path, color = MaterialTheme.colorScheme.primary, strokeWidth = 5f)
                } else {
                    val normalized = ((dados.first().pesoKg - escalaMin) / (escalaMax - escalaMin)).coerceIn(0f, 1f)
                    val x = left + width / 2f
                    val y = bottom - normalized * height
                    drawCircle(
                        color = MaterialTheme.colorScheme.primary,
                        radius = 7f,
                        center = Offset(x, y)
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
            Text("Últimos ${dados.size} registros", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))

            dados.takeLast(5).reversed().forEach { item ->
                val dataFormatada = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(item.data))
                val pesoFormatado = String.format(Locale.getDefault(), "%.1f kg", item.pesoKg)
                Text("$dataFormatada: $pesoFormatado")
            }
        }
    }
}
