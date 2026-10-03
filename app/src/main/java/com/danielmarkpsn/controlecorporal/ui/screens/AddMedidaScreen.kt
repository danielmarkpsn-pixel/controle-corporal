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

val tiposPredefinidos = listOf("Cintura", "Quadril", "Braço", "Coxa", "Peito", "Panturrilha")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedidaScreen(
    viewModel: ControleViewModel,
    onVoltar: () -> Unit
) {
    var tipoSelecionado by remember { mutableStateOf(tiposPredefinidos.first()) }
    var valorTexto by remember { mutableStateOf("") }
    var expandido by remember { mutableStateOf(false) }
    val dataAtual = remember { System.currentTimeMillis() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar medida") },
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
            ExposedDropdownMenuBox(
                expanded = expandido,
                onExpandedChange = { expandido = it }
            ) {
                OutlinedTextField(
                    value = tipoSelecionado,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo de medida") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandido,
                    onDismissRequest = { expandido = false }
                ) {
                    tiposPredefinidos.forEach { tipo ->
                        DropdownMenuItem(
                            text = { Text(tipo) },
                            onClick = {
                                tipoSelecionado = tipo
                                expandido = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = valorTexto,
                onValueChange = { valorTexto = it.replace(',', '.') },
                label = { Text("Valor (cm)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Button(
                onClick = {
                    val valor = valorTexto.toFloatOrNull()
                    if (valor != null && valor > 0f) {
                        viewModel.adicionarMedida(
                            data = dataAtual,
                            tipo = tipoSelecionado,
                            valorCm = valor
                        )
                        onVoltar()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = valorTexto.toFloatOrNull() != null
            ) {
                Text("Salvar")
            }
        }
    }
}
