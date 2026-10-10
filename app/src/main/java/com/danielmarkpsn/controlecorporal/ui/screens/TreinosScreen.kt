package com.danielmarkpsn.controlecorporal.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.danielmarkpsn.controlecorporal.R
import com.danielmarkpsn.controlecorporal.data.*
import com.danielmarkpsn.controlecorporal.ui.components.JosyHero
import com.danielmarkpsn.controlecorporal.ui.components.ExerciseMedia
import com.danielmarkpsn.controlecorporal.ui.components.ExerciseMediaFullScreen
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
    var registros by remember { mutableStateOf(storage.carregarRegistros()) }
    var aba by remember { mutableIntStateOf(0) }
    var aberto by remember { mutableStateOf<Treino?>(null) }
    var executando by remember { mutableStateOf<Treino?>(null) }
    var novo by remember { mutableStateOf(false) }
    var prontos by remember { mutableStateOf(false) }

    if (executando != null) {
        ExecucaoTreino(executando!!, { executando = null }, onRegisterSet = { registro ->
            storage.adicionarRegistro(registro)
            registros = storage.carregarRegistros()
        }) {
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
                title = { Text("TREINOS", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onVoltar) { Icon(Icons.Default.ArrowBack, "Voltar") } }
            )
        },
        floatingActionButton = {
            if (aba == 0) FloatingActionButton(onClick = { novo = true }) { Icon(Icons.Default.Add, "Novo treino") }
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(aba == 0, { aba = 0 }, { Icon(Icons.Default.FitnessCenter, null) }, label = { Text("TREINOS") })
                NavigationBarItem(aba == 1, { aba = 1 }, { Icon(Icons.Default.DateRange, null) }, label = { Text("Semana") })
                NavigationBarItem(aba == 2, { aba = 2 }, { Icon(Icons.Default.History, null) }, label = { Text("Histórico") })
            }
        }
    ) { p ->
        when (aba) {
            0 -> ListaTreinos(treinos, p, { novo = true }, { aberto = it }, { executando = it }, { prontos = true })
            1 -> SemanaTreinos(treinos, p) { aberto = it }
            else -> HistoricoTreinos(historico, p, registros)
        }
    }

    if (prontos) TreinosProntosDialog(
        existentes = treinos,
        onClose = { prontos = false },
        onAdd = { escolhidos ->
            treinos = treinos + escolhidos
            storage.salvar(treinos)
            prontos = false
        }
    )

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
    onOpen: (Treino) -> Unit, onStart: (Treino) -> Unit, onProntos: () -> Unit
) {
    var divisao by remember { mutableStateOf("Todos") }
    val filtros = listOf("Todos", "ABC", "ABCD", "PPL", "Full Body")
    val filtrados = treinos.filter { treino ->
        when (divisao) {
            "ABC" -> treino.nome.contains("ABC", ignoreCase = true) && !treino.nome.contains("ABCD", ignoreCase = true)
            "ABCD" -> treino.nome.contains("ABCD", ignoreCase = true)
            "PPL" -> treino.nome.contains("PPL", ignoreCase = true)
            "Full Body" -> treino.nome.contains("Full Body", ignoreCase = true)
            else -> true
        }
    }

    LazyColumn(
        Modifier.padding(p).fillMaxSize(),
        contentPadding = PaddingValues(16.dp, 12.dp, 16.dp, 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            JosyHero(
                title = "Vamos treinar juntos!",
                message = "A Josy acompanha suas séries, seus descansos e sua evolução.",
                actionLabel = "Ver treinos prontos",
                onAction = onProntos
            )
        }
        item {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Meus treinos", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
                    Text("Divisões, exercícios, registros e evolução.")
                }
                FilledIconButton(onClick = onNovo) {
                    Icon(Icons.Default.Add, "Novo treino")
                }
            }
        }
        item {
            Card(
                Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.FitnessCenter, null, Modifier.size(30.dp))
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text("Central de treinamento", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Text("${treinos.size} fichas • ${treinos.sumOf { it.exercicios.size }} exercícios")
                        }
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = onProntos, Modifier.weight(1f)) {
                            Icon(Icons.Default.LibraryAdd, null); Spacer(Modifier.width(5.dp)); Text("Treinos prontos")
                        }
                        OutlinedButton(onClick = onNovo, Modifier.weight(1f)) {
                            Icon(Icons.Default.Add, null); Spacer(Modifier.width(5.dp)); Text("Novo treino")
                        }
                    }
                }
            }
        }
        item {
            Text("Divisões de treino", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                filtros.forEach { filtro ->
                    FilterChip(
                        selected = divisao == filtro,
                        onClick = { divisao = filtro },
                        label = { Text(filtro) }
                    )
                }
            }
        }
        if (filtrados.isEmpty()) {
            item {
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.FitnessCenter, null, Modifier.size(44.dp))
                        Spacer(Modifier.height(8.dp))
                        Text("Nenhum treino nesta divisão", fontWeight = FontWeight.Bold)
                        Text("Adicione um treino pronto ou crie sua própria ficha.")
                        Spacer(Modifier.height(12.dp))
                        Button(onClick = onProntos) { Text("Ver treinos prontos") }
                    }
                }
            }
        }
        items(filtrados) { t ->
            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp)) {
                Column {
                    if (t.exercicios.isNotEmpty()) {
                        ExercicioVisual(t.exercicios.first().nome, t.exercicios.first().musculo, Modifier.fillMaxWidth())
                    } else {
                        Surface(Modifier.fillMaxWidth().height(110.dp), color = MaterialTheme.colorScheme.surfaceVariant) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.FitnessCenter, null, Modifier.size(42.dp))
                            }
                        }
                    }
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text(t.nome, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
                                Text(t.objetivo, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                            }
                            AssistChip(
                                onClick = { onOpen(t) },
                                label = { Text("${t.exercicios.size} exercícios") },
                                leadingIcon = { Icon(Icons.Default.List, null) }
                            )
                        }
                        if (t.dias.isNotEmpty()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.DateRange, null, Modifier.size(18.dp))
                                Spacer(Modifier.width(5.dp))
                                Text(t.dias.joinToString(" • "), style = MaterialTheme.typography.bodySmall)
                            }
                        }
                        Text(
                            t.exercicios.take(4).joinToString(" • ") { it.nome }.ifBlank { "Nenhum exercício cadastrado" }
                        )
                        HorizontalDivider()
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { onStart(t) },
                                enabled = t.exercicios.isNotEmpty(),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Icon(Icons.Default.PlayArrow, null); Spacer(Modifier.width(5.dp)); Text("Iniciar")
                            }
                            OutlinedButton(
                                onClick = { onOpen(t) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Icon(Icons.Default.Edit, null); Spacer(Modifier.width(5.dp)); Text("Editar")
                            }
                        }
                        OutlinedButton(
                            onClick = { onOpen(t) },
                            Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Icon(Icons.Default.Assessment, null); Spacer(Modifier.width(6.dp)); Text("Exercícios e registros")
                        }
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
private fun HistoricoTreinos(h: List<TreinoHistorico>, p: PaddingValues, registros: List<SerieRegistro>) {
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
        item {
            Text("Histórico de cargas e séries", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
        if (registros.isEmpty()) item { Text("As cargas e repetições registradas durante os treinos aparecerão aqui.") }
        items(registros) { r ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(14.dp)) {
                    Text(r.exercicio, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("${formatarData(r.data)} • ${r.treinoNome}")
                    Text("Série ${r.serie} • ${r.carga} kg × ${r.repeticoes} repetições • Volume ${String.format(Locale.US, "%.1f", r.volume)} kg")
                }
            }
        }
        item {
            Text("Sessões concluídas", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
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
                        ExercicioVisual(e.nome, e.musculo, Modifier.fillMaxWidth())
                        Spacer(Modifier.height(10.dp))
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
    var busca by remember { mutableStateOf("") }
    var grupoSelecionado by remember { mutableStateOf("Todos") }
    val grupos = listOf("Todos", "Peito", "Costas", "Ombros", "Bíceps", "Tríceps", "Pernas", "Glúteos", "Abdômen")
    val disponiveis = biblioteca.filter { (exercicio, grupo) ->
        (grupoSelecionado == "Todos" || grupo == grupoSelecionado ||
            (grupoSelecionado == "Pernas" && grupo in listOf("Quadríceps", "Posterior de coxa"))) &&
            (busca.isBlank() || exercicio.contains(busca, ignoreCase = true) || grupo.contains(busca, ignoreCase = true))
    }
    AlertDialog(
        onDismissRequest = onClose,
        title = { Text("Adicionar exercício") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = busca, onValueChange = { busca = it },
                    label = { Text("Buscar exercício") }, leadingIcon = { Icon(Icons.Default.Search, null) },
                    singleLine = true, modifier = Modifier.fillMaxWidth()
                )
                androidx.compose.foundation.lazy.LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(grupos) { grupo ->
                        FilterChip(
                            selected = grupoSelecionado == grupo,
                            onClick = { grupoSelecionado = grupo },
                            label = { Text(grupo) }
                        )
                    }
                }
                Text("Toque em um exercício para preencher a ficha", style = MaterialTheme.typography.labelMedium)
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().heightIn(max = 190.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(disponiveis) { (exercicio, grupo) ->
                        Card(
                            onClick = {
                                nome = exercicio
                                musculo = grupo
                            },
                            colors = CardDefaults.cardColors(
                                containerColor = if (nome == exercicio) MaterialTheme.colorScheme.primaryContainer
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Row(
                                Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.FitnessCenter, null, Modifier.size(20.dp))
                                Spacer(Modifier.width(8.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(exercicio, fontWeight = FontWeight.SemiBold)
                                    Text(grupo, style = MaterialTheme.typography.labelSmall)
                                }
                                if (nome == exercicio) Icon(Icons.Default.CheckCircle, "Selecionado")
                            }
                        }
                    }
                    if (disponiveis.isEmpty()) {
                        item { Text("Nenhum exercício encontrado para esse filtro.") }
                    }
                }
                OutlinedTextField(
                    value = nome, onValueChange = { nome = it },
                    label = { Text("Nome do exercício") }, singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = musculo, onValueChange = { musculo = it },
                    label = { Text("Grupo muscular") }, singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                    OutlinedTextField(
                        value = series, onValueChange = { series = it.filter(Char::isDigit) },
                        label = { Text("Séries") }, modifier = Modifier.weight(1f), singleLine = true
                    )
                    OutlinedTextField(
                        value = reps, onValueChange = { reps = it.filter(Char::isDigit) },
                        label = { Text("Reps") }, modifier = Modifier.weight(1f), singleLine = true
                    )
                }
                OutlinedTextField(
                    value = carga,
                    onValueChange = { carga = it.filter { c -> c.isDigit() || c == '.' || c == ',' } },
                    label = { Text("Carga (kg)") }, singleLine = true, modifier = Modifier.fillMaxWidth()
                )
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
            }, enabled = nome.isNotBlank()) { Text("Adicionar à ficha") }
        },
        dismissButton = { TextButton(onClick = onClose) { Text("Cancelar") } }
    )
}

@Composable
private fun ExerciseInfoDialog(e: TreinoExercicio, onClose: () -> Unit) {
    val f = ficha(e.nome)
    val n = e.nome.lowercase()
    val principais = when {
        "supino" in n -> listOf("Peitoral maior")
        "puxada" in n || "remada" in n -> listOf(e.musculo.ifBlank { "Costas" })
        "desenvolvimento" in n || "elevação lateral" in n -> listOf("Deltoides")
        "tríceps" in n -> listOf("Tríceps")
        "rosca" in n -> listOf("Bíceps")
        "agachamento" in n || "leg press" in n -> listOf("Quadríceps", "Glúteos")
        "extensora" in n -> listOf("Quadríceps")
        "flexora" in n -> listOf("Posteriores de coxa")
        "abdominal" in n || "prancha" in n -> listOf("Abdômen")
        else -> listOf(e.musculo.ifBlank { "Músculo principal" })
    }
    val secundarios = when {
        "supino" in n -> listOf("Tríceps", "Ombros")
        "puxada" in n || "remada" in n -> listOf("Bíceps", "Ombros")
        "desenvolvimento" in n -> listOf("Tríceps", "Trapézio")
        "elevação lateral" in n -> listOf("Trapézio")
        "tríceps" in n -> listOf("Ombros")
        "rosca" in n -> listOf("Antebraços")
        "agachamento" in n || "leg press" in n -> listOf("Posteriores de coxa", "Panturrilhas")
        else -> listOf("Músculos estabilizadores")
    }
    val passos = listOf(
        f.como,
        "Execute a fase de retorno de forma lenta e controlada, mantendo a postura.",
        "Complete a amplitude confortável sem perder o alinhamento do corpo.",
        "Volte à posição inicial e repita mantendo a mesma técnica."
    )

    AlertDialog(
        onDismissRequest = onClose,
        title = {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(e.nome, fontWeight = FontWeight.Bold)
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.BookmarkBorder, contentDescription = "Favoritar")
                }
            }
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    AssistChip(
                        onClick = {},
                        label = { Text(e.musculo.ifBlank { "Geral" }) },
                        leadingIcon = { Icon(Icons.Default.FitnessCenter, null) }
                    )
                }
                item {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Column(Modifier.padding(8.dp)) {
                            ExercicioVisual(e.nome, e.musculo, Modifier.fillMaxWidth())
                        }
                    }
                }
                item {
                    Card {
                        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                            Text("Músculos envolvidos", fontWeight = FontWeight.Bold)
                            Text("Principais:", fontWeight = FontWeight.SemiBold)
                            principais.forEach { Text("• " + it) }
                            Spacer(Modifier.height(3.dp))
                            Text("Secundários:", fontWeight = FontWeight.SemiBold)
                            secundarios.forEach { Text("• " + it) }
                        }
                    }
                }
                item {
                    Text("Passo a passo", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
                items(passos) { passo ->
                    val numero = passos.indexOf(passo) + 1
                    Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                        Surface(
                            shape = androidx.compose.foundation.shape.CircleShape,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(numero.toString(), color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
                            }
                        }
                        Text(passo, modifier = Modifier.weight(1f))
                    }
                }
                item {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(7.dp)
                    ) {
                        InfoTreino("Séries", e.series.toString() + "-" + (e.series + 1), Modifier.weight(1f))
                        InfoTreino("Repetições", e.repeticoes.toString() + "-" + (e.repeticoes + 5), Modifier.weight(1f))
                        InfoTreino("Dificuldade", "Média", Modifier.weight(1f))
                    }
                }
                item {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(Modifier.padding(13.dp)) {
                            Text("💡 Dica", fontWeight = FontWeight.Bold)
                            Text(f.erros.replace("Evite ", "Mantenha o controle e evite "))
                        }
                    }
                }
                item {
                    Button(
                        onClick = onClose,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(Modifier.width(6.dp))
                        Text("Adicionar ao treino")
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {}
    )
}

@Composable
private fun InfoTreino(titulo: String, valor: String, modifier: Modifier = Modifier) {
    Card(modifier) {
        Column(
            Modifier.padding(9.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(titulo, style = MaterialTheme.typography.labelSmall)
            Text(valor, fontWeight = FontWeight.Bold)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExecucaoTreino(treino: Treino, onBack: () -> Unit, onRegisterSet: (SerieRegistro) -> Unit, onFinish: (TreinoHistorico) -> Unit) {
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
                        ExercicioVisual(e.nome, e.musculo, Modifier.fillMaxWidth())
                        Spacer(Modifier.height(10.dp))
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
                                    onRegisterSet(SerieRegistro(System.currentTimeMillis(), treino.nome, e.nome, feitasEx + 1, e.carga, e.repeticoes))
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

@Composable
fun ExercicioVisual(nome: String, musculo: String, modifier: Modifier = Modifier) {
    val n = nome.lowercase()
    val imagem = when {
        "supino" in n -> R.drawable.img_supino_reto
        "crucifixo" in n -> R.drawable.img_crucifixo
        "agachamento" in n -> R.drawable.img_agachamento
        "leg press" in n -> R.drawable.img_leg_press
        "extensora" in n -> R.drawable.img_cadeira_extensora
        "flexora" in n -> R.drawable.img_mesa_flexora
        "puxada" in n -> R.drawable.img_puxada
        "remada" in n -> R.drawable.img_remada
        "desenvolvimento" in n -> R.drawable.img_desenvolvimento
        "elevação lateral" in n -> R.drawable.img_elevacao_lateral
        "rosca" in n -> R.drawable.img_rosca
        "tríceps" in n -> R.drawable.img_triceps
        "prancha" in n -> R.drawable.img_prancha
        "abdominal" in n -> R.drawable.img_abdominal
        else -> R.drawable.img_agachamento
    }
    var mostrarTelaCheia by remember(nome) { mutableStateOf(false) }
    Card(modifier = modifier, onClick = { mostrarTelaCheia = true }) {
        Column {
            ExerciseMedia(nome, imagem, Modifier.fillMaxWidth().height(150.dp), ContentScale.Fit, useGif = true, usePng = false)
            Column(Modifier.padding(12.dp)) {
                Text(nome, fontWeight = FontWeight.Bold)
                Text(musculo, style = MaterialTheme.typography.labelMedium)
            }
        }
    }
    if (mostrarTelaCheia) ExerciseMediaFullScreen(nome, imagem, { mostrarTelaCheia = false }, useGif = true, usePng = false)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TreinosProntosDialog(
    existentes: List<Treino>,
    onClose: () -> Unit,
    onAdd: (List<Treino>) -> Unit
) {
    var selecionado by remember { mutableStateOf<Treino?>(null) }
    AlertDialog(
        onDismissRequest = onClose,
        title = { Text("Treinos pré-estabelecidos") },
        text = {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                item { Text("Escolha uma divisão comum. O treino será adicionado à sua lista e poderá ser editado.", style = MaterialTheme.typography.bodyMedium) }
                items(TreinoPresets.todos) { t ->
                    val existe = existentes.any { it.nome == t.nome }
                    Card(
                        onClick = { if (!existe) selecionado = t },
                        colors = CardDefaults.cardColors(
                            containerColor = if (selecionado?.nome == t.nome) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Icon(Icons.Default.FitnessCenter, null)
                            Column(Modifier.weight(1f)) {
                                Text(t.nome, fontWeight = FontWeight.Bold)
                                Text("${t.objetivo} • ${t.exercicios.size} exercícios • ${t.dias.joinToString("/")}")
                            }
                            if (existe) Text("Já adicionado", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { selecionado?.let { onAdd(listOf(it)) } }, enabled = selecionado != null) { Text("Adicionar") }
        },
        dismissButton = { TextButton(onClick = onClose) { Text("Cancelar") } }
    )
}

private fun formatarData(timestamp: Long): String =
    SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR")).format(Date(timestamp))
