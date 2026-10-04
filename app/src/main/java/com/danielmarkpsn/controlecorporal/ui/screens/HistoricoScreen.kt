package com.danielmarkpsn.controlecorporal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.danielmarkpsn.controlecorporal.data.Medicao

@Composable
fun HistoricoScreen(
    medicoes: List<Medicao>,
    modifier: Modifier = Modifier
) {
    val listaOrdenada = remember(medicoes) {
        medicoes.sortedByDescending { it.data }
    }

    if (listaOrdenada.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Nenhuma medição registrada ainda.",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(listaOrdenada) { medicao ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = medicao.data.toString(),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Peso: ${medicao.peso} kg")
                        Text(text = "Cintura: ${medicao.cintura} cm")
                        Text(text = "Quadril: ${medicao.quadril} cm")
                        Text(text = "Peito: ${medicao.peito} cm")
                        Text(text = "Braço: ${medicao.braco} cm")
                    }
                }
            }
        }
    }
}
