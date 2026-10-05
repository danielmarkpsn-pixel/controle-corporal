package com.danielmarkpsn.controlecorporal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private data class ExercicioAnatomia(
    val nome: String,
    val grupo: String,
    val foco: String,
    val tecnica: String,
    val cuidado: String
)

private val exercicios = listOf(
    ExercicioAnatomia("Agachamento", "Pernas", "Quadríceps e glúteos", "Fique com os pés firmes, leve o quadril para trás e desça com controle. Suba empurrando o chão e mantendo os joelhos acompanhando os pés.", "Evite perder a postura ou usar carga incompatível com a técnica."),
    ExercicioAnatomia("Leg press", "Pernas", "Quadríceps e glúteos", "Ajuste o banco, apoie os pés na plataforma e empurre com controle, retornando sem perder a posição do quadril.", "Não deixe a lombar perder o apoio nem force uma amplitude sem controle."),
    ExercicioAnatomia("Cadeira extensora", "Pernas", "Quadríceps", "Estenda os joelhos de forma controlada e retorne lentamente à posição inicial.", "Evite movimentos bruscos e carga excessiva."),
    ExercicioAnatomia("Mesa flexora", "Pernas", "Posteriores da coxa", "Flexione os joelhos controlando a subida e a descida, mantendo o corpo estável.", "Evite deixar a carga retornar de forma abrupta."),
    ExercicioAnatomia("Panturrilha em pé", "Pernas", "Panturrilhas", "Fique estável, eleve os calcanhares com controle e retorne lentamente à posição inicial.", "Use apoio quando necessário e evite movimentos rápidos."),
    ExercicioAnatomia("Supino reto", "Peito", "Peitoral, tríceps e deltóide anterior", "Mantenha os pés firmes, estabilize as escápulas, desça a carga com controle e empurre mantendo a postura.", "Evite quicar a carga ou perder a posição dos ombros."),
    ExercicioAnatomia("Puxada frontal", "Costas", "Latíssimo do dorso e musculatura das costas", "Conduza a barra com os cotovelos, mantenha o tronco estável e controle o retorno.", "Evite balançar o tronco para gerar impulso."),
    ExercicioAnatomia("Remada curvada", "Costas", "Dorsais, trapézio e musculatura posterior", "Mantenha a coluna estável, incline o tronco de forma controlada e puxe a carga em direção ao corpo.", "Reduza a carga se a posição da coluna se perder."),
    ExercicioAnatomia("Desenvolvimento", "Ombros", "Deltóides e estabilizadores dos ombros", "Com o tronco firme, empurre os pesos acima da cabeça e retorne com controle.", "Evite compensar com a lombar ou usar carga excessiva."),
    ExercicioAnatomia("Elevação lateral", "Ombros", "Deltóide lateral", "Eleve os braços para os lados com controle e retorne lentamente.", "Evite balanço do tronco e amplitude desconfortável."),
    ExercicioAnatomia("Rosca direta", "Braços", "Bíceps", "Mantenha os cotovelos próximos ao corpo e flexione os braços sem balançar o tronco.", "Evite usar as costas para levantar a carga."),
    ExercicioAnatomia("Tríceps na polia", "Braços", "Tríceps", "Mantenha os cotovelos estáveis e estenda os antebraços de forma controlada.", "Evite abrir os cotovelos ou usar o tronco para empurrar.")
)

private val capitulos = listOf("Todos", "Pernas", "Peito", "Costas", "Ombros", "Braços")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnatomiaScreen(onVoltar: () -> Unit) {
    var filtro by remember { mutableStateOf("Todos") }
    var busca by remember { mutableStateOf("") }
    var selecionado by remember { mutableStateOf<ExercicioAnatomia?>(null) }
    val lista = exercicios.filter { e ->
        (filtro == "Todos" || e.grupo == filtro) &&
            (busca.isBlank() || e.nome.contains(busca, ignoreCase = true) || e.foco.contains(busca, ignoreCase = true))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Biblioteca de exercícios", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { p ->
        LazyColumn(
            modifier = Modifier.padding(p).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                    Column(Modifier.padding(18.dp)) {
                        Text("🏋️ Biblioteca de exercícios", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))
                        Text("Encontre um exercício por grupo muscular, veja os músculos envolvidos e abra a ficha com técnica e cuidados.")
                    }
                }
            }
            item {
                OutlinedTextField(
                    value = busca,
                    onValueChange = { busca = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    placeholder = { Text("Buscar exercício ou músculo") },
                    label = { Text("Pesquisar") }
                )
            }
            item {
                Text("Grupos musculares", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    capitulos.chunked(2).forEach { linha ->
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            linha.forEach { grupo ->
                                FilterChip(
                                    selected = filtro == grupo,
                                    onClick = { filtro = grupo },
                                    label = { Text(grupo) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (linha.size == 1) Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }
            item {
                Text(lista.size.toString() + " exercícios disponíveis", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
            }
            items(lista) { e ->
                Card(onClick = { selecionado = e }, modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(12.dp)) {
                        ExercicioVisual(e.nome, e.grupo, Modifier.fillMaxWidth())
                        Spacer(Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Icon(Icons.Default.FitnessCenter, contentDescription = null)
                            Column(Modifier.weight(1f)) {
                                Text(e.nome, fontWeight = FontWeight.Bold)
                                Text(e.grupo, style = MaterialTheme.typography.labelMedium)
                                Text(e.foco, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }

    selecionado?.let { e ->
        AlertDialog(
            onDismissRequest = { selecionado = null },
            title = { Text(e.nome) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ExercicioVisual(e.nome, e.grupo, Modifier.fillMaxWidth())
                    Text("Grupo: " + e.grupo, fontWeight = FontWeight.Bold)
                    Text("Foco anatômico", fontWeight = FontWeight.Bold)
                    Text(e.foco)
                    Text("Execução", fontWeight = FontWeight.Bold)
                    Text(e.tecnica)
                    Text("Cuidados", fontWeight = FontWeight.Bold)
                    Text(e.cuidado)
                }
            },
            confirmButton = {
                Button(onClick = { selecionado = null }) { Text("Fechar") }
            }
        )
    }
}
