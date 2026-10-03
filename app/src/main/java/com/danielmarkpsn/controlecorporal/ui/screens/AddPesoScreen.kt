package com.danielmarkpsn.controlecorporal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.danielmarkpsn.controlecorporal.viewmodel.ControleViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPesoScreen(
    viewModel: ControleViewModel,
    onVoltar: () -> Unit
) {
    var pesoTexto by remember { mutableStateOf("") }
    var observacao by remember { mutableStateOf("") }
    val dataAtual = remember { System.currentTimeMillis() }
    val dataFormatada = remember {
        SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")).format(Date(dataAtual))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar peso") },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Data: $dataFormatada", style = MaterialTheme.typography.bodyMedium)

            OutlinedTextField(
                value = pesoTexto,
                onValueChange = { pesoTexto = it.replace(',', '.') },
                label = { Text("Peso (kg)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = observacao,
                onValueChange = { observacao = it },
                label = { Text("Observação (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Button(
                onClick = {
                    val peso = pesoTexto.toFloatOrNull()
                    if (peso != null && peso > 0f) {
                        viewModel.adicionarPeso(
                            data = dataAtual,
                            pesoKg = peso,
                            observacao = observacao.ifBlank { null }
                        )
                        onVoltar()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = pesoTexto.toFloatOrNull() != null
            ) {
                Text("Salvar")
            }
        }
    }
}
