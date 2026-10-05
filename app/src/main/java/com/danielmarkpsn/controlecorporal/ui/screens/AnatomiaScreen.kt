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
    ExercicioAnatomia("Desenvolvimento de ombros com halteres", "Ombros", "Deltóide e músculos estabilizadores", "Mantenha o tronco firme, controle a descida e evite transformar o movimento em impulso.", "Use amplitude confortável e carga compatível com a técnica."),
    ExercicioAnatomia("Levantamento frontal", "Ombros", "Deltóide anterior", "Eleve os braços à frente com controle, mantendo o tronco estável.", "Evite balançar o corpo para vencer a carga."),
    ExercicioAnatomia("Levantamento lateral", "Ombros", "Deltóide lateral", "Eleve os braços para os lados de forma controlada, sem acelerar a fase de retorno.", "Evite elevar a carga excessivamente."),
    ExercicioAnatomia("Remada em pé", "Ombros", "Deltóide e trapézio", "Puxe a carga mantendo os cotovelos sob controle e o tronco estável.", "Evite usar uma carga que force compensações."),
    ExercicioAnatomia("Supino reto", "Tórax", "Peitoral maior, tríceps e deltóide anterior", "Mantenha os pés firmes, estabilize as escápulas e desça a carga de forma controlada.", "Evite quicar a carga ou perder a posição dos ombros."),
    ExercicioAnatomia("Supino inclinado", "Tórax", "Peitoral com ênfase superior", "Use banco inclinado e mantenha a trajetória controlada durante subida e descida.", "Ajuste o ângulo e a carga sem sacrificar a técnica."),
    ExercicioAnatomia("Crucifixo com halteres", "Tórax", "Peitoral", "Faça uma abertura controlada e retorne aproximando os braços sem transformar o exercício em supino.", "Evite amplitude que cause desconforto no ombro."),
    ExercicioAnatomia("Crucifixo inclinado com cabos", "Tórax", "Peitoral", "Mantenha o corpo estável e conduza os braços em arco controlado.", "Não deixe a carga determinar uma amplitude desconfortável."),
    ExercicioAnatomia("Puxada frontal", "Costas", "Latíssimo do dorso e musculatura das costas", "Puxe conduzindo o movimento com os cotovelos e controle o retorno.", "Evite balançar o tronco para gerar impulso."),
    ExercicioAnatomia("Remada curvada", "Costas", "Dorsais, trapézio e musculatura posterior", "Mantenha a coluna estável, incline o tronco de forma controlada e puxe a carga em direção ao corpo.", "Reduza a carga se a posição da coluna se perder."),
    ExercicioAnatomia("Remada baixa", "Costas", "Dorsais e região média das costas", "Puxe mantendo o tronco estável e retorne lentamente.", "Não transforme o exercício em movimento de balanço."),
    ExercicioAnatomia("Rosca direta", "Braços", "Bíceps", "Mantenha os cotovelos próximos ao corpo e faça a flexão do cotovelo sem impulso.", "Evite inclinar o tronco para levantar a carga."),
    ExercicioAnatomia("Rosca martelo", "Braços", "Bíceps e musculatura do antebraço", "Use pegada neutra e mantenha o cotovelo estável durante o movimento.", "Evite acelerar a descida."),
    ExercicioAnatomia("Tríceps na polia", "Braços", "Tríceps", "Mantenha os cotovelos estáveis e faça a extensão de forma controlada.", "Evite abrir os cotovelos ou usar o tronco para empurrar."),
    ExercicioAnatomia("Tríceps francês", "Braços", "Tríceps", "Controle a flexão e a extensão dos cotovelos mantendo o braço estável.", "Escolha uma carga que permita controlar todo o movimento."),
    ExercicioAnatomia("Agachamento", "Pernas", "Quadríceps, glúteos e musculatura posterior", "Mantenha os pés firmes, coluna estável e joelhos acompanhando a direção dos pés.", "Evite perder a postura ao aumentar a carga."),
    ExercicioAnatomia("Leg press", "Pernas", "Quadríceps e glúteos", "Empurre a plataforma com controle e mantenha a posição do quadril durante o movimento.", "Não permita que a carga force uma amplitude sem controle."),
    ExercicioAnatomia("Cadeira extensora", "Pernas", "Quadríceps", "Estenda os joelhos de forma controlada e retorne lentamente.", "Evite movimentos bruscos e cargas incompatíveis."),
    ExercicioAnatomia("Mesa flexora", "Pernas", "Posteriores da coxa", "Flexione os joelhos controlando tanto a subida quanto a descida.", "Evite deixar a carga retornar de forma abrupta."),
    ExercicioAnatomia("Elevação pélvica", "Pernas", "Glúteos e cadeia posterior", "Eleve o quadril com controle mantendo o tronco estável.", "Evite hiperestender a lombar no topo."),
    ExercicioAnatomia("Abdominal curto", "Abdominais", "Músculos abdominais", "Faça a contração do tronco de forma curta e controlada, sem puxar a cabeça.", "Evite usar impulso."),
    ExercicioAnatomia("Prancha", "Abdominais", "Abdômen e estabilizadores do tronco", "Mantenha cabeça, tronco e quadril alinhados e contraia abdômen e glúteos.", "Não deixe o quadril cair e não prenda a respiração.")
)

private val capitulos = listOf("Todos", "Ombros", "Tórax", "Costas", "Braços", "Pernas", "Abdominais")

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
