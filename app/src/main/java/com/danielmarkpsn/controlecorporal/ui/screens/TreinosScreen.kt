package com.danielmarkpsn.controlecorporal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.danielmarkpsn.controlecorporal.data.Treino
import com.danielmarkpsn.controlecorporal.data.TreinoExercicio
import com.danielmarkpsn.controlecorporal.data.TreinoStorage

private val biblioteca = listOf(
    "Supino reto" to "Peito", "Supino inclinado" to "Peito", "Crucifixo" to "Peito",
    "Remada curvada" to "Costas", "Puxada frontal" to "Costas", "Remada baixa" to "Costas",
    "Desenvolvimento de ombros" to "Ombros", "Elevação lateral" to "Ombros",
    "Rosca direta" to "Bíceps", "Rosca martelo" to "Bíceps",
    "Tríceps na polia" to "Tríceps", "Tríceps francês" to "Tríceps",
    "Agachamento" to "Pernas", "Leg press" to "Pernas", "Cadeira extensora" to "Pernas",
    "Mesa flexora" to "Pernas", "Elevação pélvica" to "Glúteos",
    "Abdominal curto" to "Abdômen", "Prancha" to "Abdômen"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TreinosScreen(onVoltar: () -> Unit) {
    val context = LocalContext.current
    val storage = remember { TreinoStorage(context) }
    var treinos by remember { mutableStateOf(storage.carregar()) }
    var aberto by remember { mutableStateOf<Treino?>(null) }
    var novo by remember { mutableStateOf(false) }

    if (aberto != null) {
        EditorTreino(
            treino = aberto!!,
            onBack = { aberto = null },
            onSave = { atualizado ->
                treinos = treinos.map { if (it.nome == aberto!!.nome) atualizado else it }
                storage.salvar(treinos)
                aberto = null
            }
        )
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meus Treinos", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onVoltar) { Icon(Icons.Default.ArrowBack, "Voltar") } }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { novo = true }) {
                Icon(Icons.Default.Add, "Novo treino")
            }
        }
    ) { padding ->
        LazyColumn(
            Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                    Column(Modifier.padding(18.dp)) {
                        Text("🏋️ Programação de treino", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))
                        Text("Crie treinos A, B, C ou quantos precisar. Adicione exercícios, séries, repetições e carga.")
                    }
                }
            }
            if (treinos.isEmpty()) {
                item {
                    OutlinedButton(onClick = { novo = true }, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Default.Add, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Criar meu primeiro treino")
                    }
                }
            }
            items(treinos) { treino ->
                Card(onClick = { aberto = treino }, modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(18.dp)) {
                        Text(treino.nome, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text(treino.objetivo + " • " + treino.exercicios.size + " exercícios")
                        Spacer(Modifier.height(8.dp))
                        Text(
                            treino.exercicios.take(3).joinToString(" • ") { it.nome }.ifBlank { "Nenhum exercício adicionado" },
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(Modifier.height(8.dp))
                        Text("Toque para abrir", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }
    }

    if (novo) {
        NovoTreinoDialog(
            onClose = { novo = false },
            onCreate = { nome, objetivo ->
                val t = Treino(nome, objetivo, emptyList())
                treinos = treinos + t
                storage.salvar(treinos)
                novo = false
                aberto = t
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NovoTreinoDialog(onClose: () -> Unit, onCreate: (String, String) -> Unit) {
    var nome by remember { mutableStateOf("Treino A") }
    var objetivo by remember { mutableStateOf("Hipertrofia") }

    AlertDialog(
        onDismissRequest = onClose,
        title = { Text("Novo treino") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(nome, { nome = it }, label = { Text("Nome do treino") }, singleLine = true)
                Text("Objetivo", fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    listOf("Hipertrofia", "Força", "Resistência").forEach { op ->
                        FilterChip(selected = objetivo == op, onClick = { objetivo = op }, label = { Text(op) })
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { if (nome.isNotBlank()) onCreate(nome.trim(), objetivo) }) { Text("Criar") }
        },
        dismissButton = { TextButton(onClick = onClose) { Text("Cancelar") } }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditorTreino(treino: Treino, onBack: () -> Unit, onSave: (Treino) -> Unit) {
    var exercicios by remember { mutableStateOf(treino.exercicios) }
    var adicionar by remember { mutableStateOf(false) }
    var feitos by remember { mutableStateOf(setOf<Int>()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(treino.nome, fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Voltar") } },
                actions = { IconButton(onClick = { adicionar = true }) { Icon(Icons.Default.Add, "Adicionar exercício") } }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(selected = false, onClick = onBack, icon = { Icon(Icons.Default.Close, "Sair") }, label = { Text("Sair") })
                NavigationBarItem(selected = false, onClick = { adicionar = true }, icon = { Icon(Icons.Default.Add, "Adicionar") }, label = { Text("Exercício") })
                NavigationBarItem(selected = false, onClick = { onSave(treino.copy(exercicios = exercicios)) }, icon = { Icon(Icons.Default.Save, "Salvar") }, label = { Text("Salvar") })
            }
        }
    ) { padding ->
        LazyColumn(
            Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text(treino.objetivo + " • " + exercicios.size + " exercícios", style = MaterialTheme.typography.titleMedium)
            }
            if (exercicios.isEmpty()) {
                item { Text("Adicione exercícios usando o botão +.", style = MaterialTheme.typography.bodyMedium) }
            }
            items(exercicios.indices.toList()) { index ->
                val e = exercicios[index]
                Card {
                    Column(Modifier.padding(14.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column(Modifier.weight(1f)) {
                                Text(e.nome, fontWeight = FontWeight.Bold)
                                Text(e.musculo, style = MaterialTheme.typography.labelMedium)
                            }
                            IconButton(onClick = { exercicios = exercicios.filterIndexed { i, _ -> i != index } }) {
                                Icon(Icons.Default.Delete, "Excluir")
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            e.series.toString() + " séries × " + e.repeticoes + " repetições • " +
                                if (e.carga > 0f) "%.1f kg".format(e.carga) else "carga não definida"
                        )
                        Spacer(Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(onClick = {
                                feitos = if (index in feitos) feitos - index else feitos + index
                            }) {
                                Icon(
                                    if (index in feitos) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                    null
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(if (index in feitos) "Concluído" else "Marcar feito")
                            }
                        }
                    }
                }
            }
            item {
                if (exercicios.isNotEmpty()) {
                    Text("Progresso: " + feitos.size + "/" + exercicios.size + " exercícios concluídos", fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    if (adicionar) {
        AddExerciseDialog(
            onClose = { adicionar = false },
            onAdd = { nome, musculo, series, reps, carga ->
                exercicios = exercicios + TreinoExercicio(nome, musculo, series, reps, carga)
                adicionar = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddExerciseDialog(onClose: () -> Unit, onAdd: (String, String, Int, Int, Float) -> Unit) {
    var nome by remember { mutableStateOf("") }
    var musculo by remember { mutableStateOf("") }
    var series by remember { mutableStateOf("3") }
    var reps by remember { mutableStateOf("10") }
    var carga by remember { mutableStateOf("0") }

    AlertDialog(
        onDismissRequest = onClose,
        title = { Text("Adicionar exercício") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(nome, { nome = it }, label = { Text("Exercício") }, singleLine = true)
                Text("Sugestões: " + biblioteca.take(5).joinToString(", "), style = MaterialTheme.typography.bodySmall)
                OutlinedTextField(musculo, { musculo = it }, label = { Text("Músculo") }, singleLine = true)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(series, { series = it.filter(Char::isDigit) }, label = { Text("Séries") }, modifier = Modifier.weight(1f), singleLine = true)
                    OutlinedTextField(reps, { reps = it.filter(Char::isDigit) }, label = { Text("Repetições") }, modifier = Modifier.weight(1f), singleLine = true)
                }
                OutlinedTextField(
                    carga,
                    { carga = it.filter { c -> c.isDigit() || c == '.' || c == ',' } },
                    label = { Text("Carga (kg)") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                if (nome.isNotBlank()) {
                    onAdd(
                        nome.trim(),
                        musculo.ifBlank { "Geral" },
                        series.toIntOrNull()?.coerceAtLeast(1) ?: 3,
                        reps.toIntOrNull()?.coerceAtLeast(1) ?: 10,
                        carga.replace(',', '.').toFloatOrNull() ?: 0f
                    )
                }
            }) { Text("Adicionar") }
        },
        dismissButton = { TextButton(onClick = onClose) { Text("Cancelar") } }
    )
}
