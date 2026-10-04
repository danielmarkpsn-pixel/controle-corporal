package com.danielmarkpsn.controlecorporal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.danielmarkpsn.controlecorporal.viewmodel.ControleViewModel

private val tiposPredefinidos = listOf(
    "Cintura", "Abdômen", "Quadril", "Peito",
    "Braço direito", "Braço esquerdo",
    "Coxa direita", "Coxa esquerda",
    "Panturrilha direita", "Panturrilha esquerda"
)

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
                title = { Text("Medidas corporais") },
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
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Row(Modifier.padding(18.dp)) {
                    Icon(Icons.Default.Straighten, contentDescription = null)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Nova medição", fontWeight = FontWeight.Bold)
                        Text(
                            "Registre cada parte do corpo separadamente. Braços e coxas agora têm lado direito e esquerdo.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = expandido,
                onExpandedChange = { expandido = !expandido }
            ) {
                OutlinedTextField(
                    value = tipoSelecionado,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Parte do corpo") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido)
                    },
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
                onValueChange = {
                    valorTexto = it.replace(',', '.').filter { c -> c.isDigit() || c == '.' }
                },
                label = { Text("Medida em centímetros") },
                placeholder = { Text("Ex.: 35,5") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Button(
                onClick = {
                    valorTexto.toFloatOrNull()?.takeIf { it > 0f }?.let {
                        viewModel.adicionarMedida(dataAtual, tipoSelecionado, it)
                        onVoltar()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = valorTexto.toFloatOrNull()?.let { it > 0f } == true
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Salvar medida")
            }
        }
    }
}
