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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.danielmarkpsn.controlecorporal.viewmodel.ControleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MetaScreen(
    viewModel: ControleViewModel,
    onVoltar: () -> Unit
) {
    val meta by viewModel.meta.collectAsStateWithLifecycle()
    var pesoAlvo by remember { mutableStateOf(meta?.pesoAlvoKg?.toString() ?: "") }
    var altura by remember { mutableStateOf(meta?.alturaCm?.toString() ?: "") }

    LaunchedEffect(meta) {
        meta?.let {
            pesoAlvo = it.pesoAlvoKg.toString()
            altura = it.alturaCm.toString()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Definir meta") },
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
            OutlinedTextField(
                value = pesoAlvo,
                onValueChange = { pesoAlvo = it.replace(',', '.') },
                label = { Text("Peso alvo (kg)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = altura,
                onValueChange = { altura = it.replace(',', '.') },
                label = { Text("Altura (cm)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Button(
                onClick = {
                    val peso = pesoAlvo.toFloatOrNull()
                    val alt = altura.toFloatOrNull()
                    if (peso != null && peso > 0f && alt != null && alt > 0f) {
                        viewModel.salvarMeta(
                            pesoAlvoKg = peso,
                            alturaCm = alt,
                            dataAlvo = null
                        )
                        onVoltar()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = pesoAlvo.toFloatOrNull() != null && altura.toFloatOrNull() != null
            ) {
                Text("Salvar meta")
            }
        }
    }
}
