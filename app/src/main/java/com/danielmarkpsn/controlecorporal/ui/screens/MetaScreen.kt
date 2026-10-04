package com.danielmarkpsn.controlecorporal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.danielmarkpsn.controlecorporal.viewmodel.ControleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MetaScreen(
    viewModel: ControleViewModel,
    onVoltar: () -> Unit
) {
    val meta by viewModel.meta.collectAsStateWithLifecycle()
    var pesoAlvo by remember { mutableStateOf("") }
    var altura by remember { mutableStateOf("") }

    LaunchedEffect(meta) {
        altura = meta?.alturaCm?.takeIf { it > 0f }?.toString() ?: ""
        pesoAlvo = meta?.pesoAlvoKg?.takeIf { it > 0f }?.toString() ?: ""
    }

    val alturaValida = altura.replace(',', '.').toFloatOrNull()?.let { it in 80f..250f } == true
    val pesoValidoOuVazio = pesoAlvo.isBlank() ||
        pesoAlvo.replace(',', '.').toFloatOrNull()?.let { it in 20f..400f } == true

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil e meta") },
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
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Dados para o seu acompanhamento",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Cadastre sua altura para calcular o IMC. A meta de peso é opcional.",
                style = MaterialTheme.typography.bodyMedium
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(18.dp)) {
                    Row {
                        Icon(Icons.Default.Height, contentDescription = null)
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text("Altura", style = MaterialTheme.typography.titleMedium)
                            Text(
                                "Usada no cálculo do IMC",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    OutlinedTextField(
                        value = altura,
                        onValueChange = { altura = it.replace(',', '.').filter { c -> c.isDigit() || c == '.' } },
                        label = { Text("Altura (cm)") },
                        placeholder = { Text("Ex.: 175") },
                        supportingText = {
                            Text(if (alturaValida) "Altura válida" else "Informe entre 80 e 250 cm")
                        },
                        isError = altura.isNotBlank() && !alturaValida,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(18.dp)) {
                    Row {
                        Icon(Icons.Default.Flag, contentDescription = null)
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text("Meta de peso", style = MaterialTheme.typography.titleMedium)
                            Text(
                                "Opcional — você pode definir depois",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    OutlinedTextField(
                        value = pesoAlvo,
                        onValueChange = { pesoAlvo = it.replace(',', '.').filter { c -> c.isDigit() || c == '.' } },
                        label = { Text("Peso alvo (kg)") },
                        placeholder = { Text("Ex.: 75") },
                        supportingText = { Text("Deixe vazio se não quiser definir uma meta") },
                        isError = pesoAlvo.isNotBlank() && !pesoValidoOuVazio,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }

            Button(
                onClick = {
                    val alt = altura.replace(',', '.').toFloatOrNull()
                    val peso = pesoAlvo.replace(',', '.').toFloatOrNull() ?: 0f
                    if (alt != null && alt > 0f) {
                        viewModel.salvarMeta(
                            pesoAlvoKg = peso,
                            alturaCm = alt,
                            dataAlvo = null
                        )
                        onVoltar()
                    }
                },
                enabled = alturaValida && pesoValidoOuVazio,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Salvar dados")
            }
        }
    }
}
