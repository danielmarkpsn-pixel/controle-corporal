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
import com.danielmarkpsn.controlecorporal.data.*
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val diasSemana = listOf("Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom")
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

private data class Ficha(val como: String, val respirar: String, val erros: String)

private fun ficha(nome: String): Ficha {
    val n = nome.lowercase()
    return when {
        "supino" in n -> Ficha(
            "Deite-se com os pés firmes. Mantenha as escápulas estáveis, desça a carga com controle até o peito e empurre sem perder a postura.",
            "Inspire na descida e expire ao empurrar.",
            "Evite quicar a barra, abrir excessivamente os cotovelos ou aumentar a carga sacrificando a técnica."
        )
        "agachamento" in n -> Ficha(
            "Mantenha os pés firmes, coluna neutra e joelhos acompanhando os pés. Desça com controle e suba mantendo o tronco firme.",
            "Inspire antes da descida e expire na subida.",
            "Evite joelhos colapsando para dentro, arredondamento da lombar e carga incompatível com a técnica."
        )
        "remada" in n || "puxada" in n -> Ficha(
            "Estabilize o tronco e conduza o movimento com os cotovelos. Controle o retorno e evite balanço.",
            "Expire ao puxar e inspire ao retornar.",
            "Não use impulso excessivo nem deixe o peso voltar de forma brusca."
        )
        "rosca" in n -> Ficha(
            "Mantenha os cotovelos próximos ao corpo e flexione o cotovelo sem balançar o tronco.",
            "Expire ao subir e inspire ao descer.",
            "Evite roubar com as costas ou deixar a carga cair."
        )
        "tríceps" in n -> Ficha(
            "Mantenha os cotovelos estáveis e estenda o antebraço com controle, retornando lentamente.",
            "Expire na extensão e inspire no retorno.",
            "Evite abrir os cotovelos e usar balanço."
        )
        "prancha" in n -> Ficha(
            "Mantenha cabeça, tronco e quadril alinhados, contraindo abdômen e glúteos.",
            "Respire continuamente e de forma controlada.",
            "Não deixe o quadril cair ou subir excessivamente e não prenda a respiração."
        )
        "abdominal" in n -> Ficha(
            "Contraia o abdômen e faça o movimento de forma controlada, sem puxar a cabeça.",
            "Expire na contração e inspire no retorno.",
            "Evite impulso e tensão desnecessária no pescoço."
        )
        else -> Ficha(
            "Priorize postura, amplitude confortável e controle em todas as fases do movimento.",
            "Inspire no retorno e expire durante o esforço.",
            "Evite carga excessiva, movimentos bruscos e perda de postura."
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TreinosScreen(onVoltar: () -> Unit) {
    val context = LocalContext.current
    val storage = remember { TreinoStorage(context) }
    var treinos by remember { mutableStateOf(storage.carregar()) }
    var historico by remember { mutableStateOf(storage.carregarHistorico()) }
    var aba by remember { mutableIntStateOf(0) }
    var aberto by remember { mutableStateOf<Treino?>(null) }
    var executando by remember { mutableStateOf<Treino?>(null) }
    var novo by remember { mutableStateOf(false) }

    if (executando != null) {
        ExecucaoTreino(executando!!, { executando = null }) {
            storage.adicionarHistorico(it)
            historico = storage.carregarHistorico()
            executando = null
            aba = 2
        }
        return
    }

    if (aberto != null) {
        EditorTreino(
            treino = aberto!!,
            onBack = { aberto = null },
            onStart = { executando = aberto },
            onSave = { atualizado ->
                val antigo = aberto!!.nome
                treinos = treinos.map { if (it.nome == antigo) atualizado else it }
                storage.salvar(treinos)
                aberto = null
            }
        )
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Treinos", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onVoltar) { Icon(Icons.Default.ArrowBack, "Voltar") } }
            )
        },
        floatingActionButton = {
            if (aba == 0) FloatingActionButton(onClick = { novo = true }) { Icon(Icons.Default.Add, "Novo treino") }
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(aba == 0, { aba = 0 }, { Icon(Icons.Default.FitnessCenter, null) }, label = { Text("Treinos") })
                NavigationBarItem(aba == 1, { aba = 1 }, { Icon(Icons.Default.DateRange, null) }, label = { Text("Semana") })
                NavigationBarItem(aba == 2, { aba = 2 }, { Icon(Icons.Default.History, null) }, label = { Text("Histórico") })
            }
        }
    ) { p ->
        when (aba) {
            0 -> ListaTreinos(treinos, p, { novo = true }, { aberto = it }, { executando = it })
            1 -> SemanaTreinos(treinos, p) { aberto = it }
            else -> HistoricoTreinos(historico, p)
        }
    }

    if (novo) NovoTreinoDialog(
        onClose = { novo = false },
        onCreate = { nome, objetivo ->
            val t = Treino(nome, objetivo, emptyList(), emptyList())
            treinos = treinos + t
            storage.salvar(treinos)
            novo = false
            aberto = t
        }
    )
}

@Composable
private fun ListaTreinos(
    treinos: List<Treino>, p: PaddingValues, onNovo: () -> Unit,
    onOpen: (Treino) -> Unit, onStart: (Treino) -> Unit
) {
    LazyColumn(
        Modifier.padding(p).fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                Column(Modifier.padding(18.dp)) {
                    Text("🏋️ Programação de treino", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text("Ficha, execução por séries, descanso, técnica, divisão semanal e histórico.")
                }
            }
        }
        if (treinos.isEmpty()) item {
            OutlinedButton(onClick = onNovo, modifier = Modifier.fillMaxWidth()) { Text("Criar meu primeiro treino") }
        }
        items(treinos) { t ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text(t.nome, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("${t.objetivo} • ${t.exercicios.size} exercícios")
                    if (t.dias.isNotEmpty()) Text("📅 ${t.dias.joinToString(" • ")}")
                    Spacer(Modifier.height(8.dp))
                    Text(t.exercicios.take(3).joinToString(" • ") { it.nome }.ifBlank { "Nenhum exercício" })
                    Spacer(Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { onStart(t) }, enabled = t.exercicios.isNotEmpty()) { Text("▶ Iniciar") }
                        OutlinedButton(onClick = { onOpen(t) }) { Text("Editar") }
                    }
                }
            }
        }
    }
}

@Composable
private fun SemanaTreinos(treinos: List<Treino>, p: PaddingValues, onOpen: (Treino) -> Unit) {
    LazyColumn(
        Modifier.padding(p).fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text("📅 Programação semanal", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text("Organize sua divisão de treino por dia.")
        }
        items(diasSemana) { dia ->
            val lista = treinos.filter { dia in it.dias }
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(14.dp)) {
                    Text(dia, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    if (lista.isEmpty()) Text("Descanso / nenhum treino programado.")
                    lista.forEach { t ->
                        TextButton(onClick = { onOpen(t) }, modifier = Modifier.fillMaxWidth()) {
                            Column(Modifier.fillMaxWidth()) {
                                Text(t.nome, fontWeight = FontWeight.Bold)
                                Text("${t.objetivo} • ${t.exercicios.size} exercícios")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoricoTreinos(h: List<TreinoHistorico>, p: PaddingValues) {
    val volume = h.sumOf { it.volume.toDouble() }.toFloat()
    LazyColumn(
        Modifier.padding(p).fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                Column(Modifier.padding(16.dp)) {
                    Text("📈 Evolução", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text("${h.size} sessões registradas")
                    Text("Volume acumulado: ${String.format(Locale.US, "%.1f", volume)} kg")
                    if (h.isNotEmpty()) Text("Último: ${formatarData(h.first().data)}")
                }
            }
        }
        if (h.isEmpty()) item { Text("Ainda não há treinos concluídos. Inicie uma sessão para criar seu histórico.") }
        items(h) { x ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(14.dp)) {
                    Text(x.nome, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("${formatarData(x.data)} • ${x.objetivo}")
                    Text("${x.exerciciosConcluidos} exercícios • ${x.seriesConcluidas} séries • ${x.duracaoMin} min")
                    Text("Volume: ${String.format(Locale.US, "%.1f", x.volume)} kg")
                }
            }
        }
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
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(nome, { nome = it }, label = { Text("Nome") }, singleLine = true)
                Text("Objetivo", fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    listOf("Hipertrofia", "Força", "Resistência").forEach {
                        FilterChip(objetivo == it, { objetivo = it }, label = { Text(it) })
                    }
                }
            }
        },
        confirmButton = { Button(onClick = { if (nome.isNotBlank()) onCreate(nome.trim(), objetivo) }) { Text("Criar") } },
        dismissButton = { TextButton(onClick = onClose) { Text("Cancelar") } }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditorTreino(
    treino: Treino, onBack: () -> Unit, onStart: () -> Unit, onSave: (Treino) -> Unit
) {
    var ex by remember { mutableStateOf(treino.exercicios) }
    var dias by remember { mutableStateOf(treino.dias) }
    var adicionar by remember { mutableStateOf(false) }
    var info by remember { mutableStateOf<TreinoExercicio?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(treino.nome) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Voltar") } }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(false, onBack, { Icon(Icons.Default.Close, null) }, label = { Text("Sair") })
                NavigationBarItem(false, { adicionar = true }, { Icon(Icons.Default.Add, null) }, label = { Text("Exercício") })
                NavigationBarItem(false, { onSave(treino.copy(exercicios = ex, dias = dias)) }, { Icon(Icons.Default.Save, null) }, label = { Text("Salvar") })
            }
        }
    ) { p ->
        LazyColumn(Modifier.padding(p).fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            item {
                Card {
                    Column(Modifier.padding(14.dp)) {
                        Text(treino.objetivo, fontWeight = FontWeight.Bold)
                        Text("Dias da semana")
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                            diasSemana.forEach { d ->
                                FilterChip(d in dias, { dias = if (d in dias) dias - d else dias + d }, label = { Text(d) })
                            }
                        }
                    }
                }
            }
            item {
                Button(onClick = onStart, enabled = ex.isNotEmpty(), modifier = Modifier.fillMaxWidth()) { Text("▶ INICIAR TREINO") }
            }
            items(ex.indices.toList()) { i ->
                val e = ex[i]
                Card {
                    Column(Modifier.padding(14.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column(Modifier.weight(1f)) {
                                Text(e.nome, fontWeight = FontWeight.Bold)
                                Text(e.musculo)
                            }
                            IconButton(onClick = { ex = ex.filterIndexed { j, _ -> i != j } }) { Icon(Icons.Default.Delete, "Excluir") }
                        }
                        Text("${e.series} séries × ${e.repeticoes} • ${if (e.carga > 0) String.format(Locale.US, "%.1f kg", e.carga) else "peso livre"}")
                        TextButton(onClick = { info = e }) { Text("ℹ Ver técnica e instruções") }
                    }
                }
            }
        }
    }
    if (adicionar) AddExerciseDialog({ adicionar = false }) { ex = ex + it; adicionar = false }
    if (info != null) ExerciseInfoDialog(info!!) { info = null }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddExerciseDialog(onClose: () -> Unit, onAdd: (TreinoExercicio) -> Unit) {
    var nome by remember { mutableStateOf("") }
    var musculo by remember { mutableStateOf("") }
    var series by remember { mutableStateOf("3") }
    var reps by remember { mutableStateOf("10") }
    var carga by remember { mutableStateOf("0") }
    AlertDialog(
        onDismissRequest = onClose,
        title = { Text("Adicionar exercício") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
                OutlinedTextField(nome, { nome = it }, label = { Text("Exercício") }, singleLine = true)
                Text("Sugestões: ${biblioteca.take(8).joinToString(", ")}", style = MaterialTheme.typography.bodySmall)
                OutlinedTextField(musculo, { musculo = it }, label = { Text("Músculo") }, singleLine = true)
                Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                    OutlinedTextField(series, { series = it.filter(Char::isDigit) }, label = { Text("Séries") }, modifier = Modifier.weight(1f), singleLine = true)
                    OutlinedTextField(reps, { reps = it.filter(Char::isDigit) }, label = { Text("Reps") }, modifier = Modifier.weight(1f), singleLine = true)
                }
                OutlinedTextField(carga, { carga = it.filter { c -> c.isDigit() || c == '.' || c == ',' } }, label = { Text("Carga kg") }, singleLine = true)
            }
        },
        confirmButton = {
            Button(onClick = {
                if (nome.isNotBlank()) {
                    val f = ficha(nome)
                    onAdd(TreinoExercicio(
                        nome.trim(), musculo.ifBlank { "Geral" },
                        series.toIntOrNull()?.coerceAtLeast(1) ?: 3,
                        reps.toIntOrNull()?.coerceAtLeast(1) ?: 10,
                        carga.replace(',', '.').toFloatOrNull() ?: 0f,
                        f.como, f.respirar, f.erros
                    ))
                }
            }) { Text("Adicionar") }
        },
        dismissButton = { TextButton(onClick = onClose) { Text("Cancelar") } }
    )
}

@Composable
private fun ExerciseInfoDialog(e: TreinoExercicio, onClose: () -> Unit) {
    val f = ficha(e.nome)
    AlertDialog(
        onDismissRequest = onClose,
        title = { Text(e.nome) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Músculo: ${e.musculo}", fontWeight = FontWeight.Bold)
                Text("Como executar", fontWeight = FontWeight.Bold)
                Text(if (e.instrucoes.isNotBlank()) e.instrucoes else f.como)
                Text("Respiração", fontWeight = FontWeight.Bold)
                Text(if (e.respiracao.isNotBlank()) e.respiracao else f.respirar)
                Text("Erros comuns", fontWeight = FontWeight.Bold)
                Text(if (e.erros.isNotBlank()) e.erros else f.erros)
                Text("${e.series} séries × ${e.repeticoes} repetições")
            }
        },
        confirmButton = { Button(onClick = onClose) { Text("Entendi") } }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExecucaoTreino(treino: Treino, onBack: () -> Unit, onFinish: (TreinoHistorico) -> Unit) {
    val inicio = remember { System.currentTimeMillis() }
    var feitas by remember { mutableStateOf<Map<Int, Int>>(emptyMap()) }
    var descanso by remember { mutableIntStateOf(0) }
    var info by remember { mutableStateOf<TreinoExercicio?>(null) }

    LaunchedEffect(descanso) {
        if (descanso > 0) {
            delay(1000)
            descanso -= 1
        }
    }

    val total = treino.exercicios.sumOf { it.series }
    val concluidas = feitas.values.sum()
    val exerciciosConcluidos = treino.exercicios.indices.count { (feitas[it] ?: 0) >= treino.exercicios[it].series }
    val volume = treino.exercicios.indices.sumOf { i ->
        (feitas[i] ?: 0) * treino.exercicios[i].repeticoes * treino.exercicios[i].carga.toDouble()
    }.toFloat()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Executando: ${treino.nome}") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Sair") } }
            )
        }
    ) { p ->
        LazyColumn(Modifier.padding(p).fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            item {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Progresso: ${concluidas} / ${total} séries", fontWeight = FontWeight.Bold)
                        Text("${exerciciosConcluidos} / ${treino.exercicios.size} exercícios concluídos")
                        Text("Volume atual: ${String.format(Locale.US, "%.1f", volume)} kg")
                        if (descanso > 0) Text("⏱️ Descanso: ${descanso}s", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
            items(treino.exercicios.indices.toList()) { i ->
                val e = treino.exercicios[i]
                val feitasEx = feitas[i] ?: 0
                Card {
                    Column(Modifier.padding(14.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column(Modifier.weight(1f)) {
                                Text(e.nome, fontWeight = FontWeight.Bold)
                                Text("${e.series} × ${e.repeticoes} • ${if (e.carga > 0) String.format(Locale.US, "%.1f kg", e.carga) else "peso livre"}")
                            }
                            IconButton(onClick = { info = e }) { Icon(Icons.Default.Info, "Técnica") }
                        }
                        LinearProgressIndicator(
                            progress = if (e.series == 0) 0f else (feitasEx.toFloat() / e.series).coerceIn(0f, 1f),
                            Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(8.dp))
                        Button(
                            onClick = {
                                if (feitasEx < e.series) {
                                    feitas = feitas + (i to feitasEx + 1)
                                    descanso = 60
                                }
                            },
                            enabled = feitasEx < e.series,
                            modifier = Modifier.fillMaxWidth()
                        ) { Text(if (feitasEx < e.series) "Registrar série ${feitasEx + 1}" else "Exercício concluído") }
                        TextButton(onClick = { descanso = 90 }, modifier = Modifier.fillMaxWidth()) { Text("⏱ Descanso 90 segundos") }
                    }
                }
            }
            item {
                Button(
                    onClick = {
                        val minutos = (((System.currentTimeMillis() - inicio) / 60000L).toInt()).coerceAtLeast(1)
                        onFinish(TreinoHistorico(
                            System.currentTimeMillis(), treino.nome, treino.objetivo, minutos,
                            volume, exerciciosConcluidos, concluidas
                        ))
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("FINALIZAR E SALVAR TREINO") }
            }
        }
    }
    if (info != null) ExerciseInfoDialog(info!!) { info = null }
}

private fun formatarData(timestamp: Long): String =
    SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR")).format(Date(timestamp))
