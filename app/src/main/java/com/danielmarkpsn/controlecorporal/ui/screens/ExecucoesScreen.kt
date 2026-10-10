package com.danielmarkpsn.controlecorporal.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.danielmarkpsn.controlecorporal.R
import com.danielmarkpsn.controlecorporal.ui.components.ExerciseMedia
import com.danielmarkpsn.controlecorporal.ui.components.ExerciseMediaFullScreen
import com.danielmarkpsn.controlecorporal.ui.components.JosyMini
import com.danielmarkpsn.controlecorporal.ui.theme.Lilas

private data class ExecucaoItem(val nome: String, val grupo: String, val imagem: Int)

private val execucoesDisponiveis = listOf(
    ExecucaoItem("Agachamento Sumô", "Pernas e glúteos", R.drawable.img_agachamento),
    ExecucaoItem("Agachamento com Salto", "Pernas e condicionamento", R.drawable.img_agachamento),
    ExecucaoItem("Agachamento com Kettlebell", "Pernas e glúteos", R.drawable.img_agachamento),
    ExecucaoItem("Cadeira Abdutora", "Glúteos", R.drawable.img_agachamento),
    ExecucaoItem("Crucifixo", "Peito", R.drawable.img_crucifixo),
    ExecucaoItem("Crucifixo Inclinado", "Peito", R.drawable.img_crucifixo),
    ExecucaoItem("Crucifixo Declinado", "Peito", R.drawable.img_crucifixo),
    ExecucaoItem("Crucifixo em Pé na Polia Alta", "Peito", R.drawable.img_crucifixo),
    ExecucaoItem("Crucifixo em Pé na Polia Baixa", "Peito", R.drawable.img_crucifixo),
    ExecucaoItem("Crucifixo em Pé na Polia Média", "Peito", R.drawable.img_crucifixo),
    ExecucaoItem("Elevação Pélvica com Barra", "Glúteos", R.drawable.img_agachamento),
    ExecucaoItem("Encolhimento", "Trapézio", R.drawable.img_desenvolvimento),
    ExecucaoItem("Levantamento Terra com Barra Hexagonal", "Pernas e costas", R.drawable.img_remada),
    ExecucaoItem("Levantamento Terra com Halteres", "Pernas e costas", R.drawable.img_remada),
    ExecucaoItem("Levantamento Terra Romeno", "Posteriores e glúteos", R.drawable.img_mesa_flexora),
    ExecucaoItem("Levantamento Terra Sumô", "Pernas e glúteos", R.drawable.img_agachamento),
    ExecucaoItem("Levantamento Terra Unilateral", "Posteriores e glúteos", R.drawable.img_mesa_flexora),
    ExecucaoItem("Passada Lateral", "Pernas e glúteos", R.drawable.img_agachamento),
    ExecucaoItem("Puxada Neutra", "Costas", R.drawable.img_puxada),
    ExecucaoItem("Pulley Costas", "Costas", R.drawable.img_puxada),
    ExecucaoItem("Remada Baixa", "Costas", R.drawable.img_remada),
    ExecucaoItem("Remada Cavalinho", "Costas", R.drawable.img_remada),
    ExecucaoItem("Remada Curvada", "Costas", R.drawable.img_remada),
    ExecucaoItem("Remada Serrote", "Costas", R.drawable.img_remada),
    ExecucaoItem("Rosca Direta na Barra W", "Bíceps", R.drawable.img_rosca),
    ExecucaoItem("Rosca Direta na Polia", "Bíceps", R.drawable.img_rosca),
    ExecucaoItem("Rosca Martelo com Halteres", "Bíceps", R.drawable.img_rosca),
    ExecucaoItem("Rosca Martelo Corda", "Bíceps", R.drawable.img_rosca),
    ExecucaoItem("Rosca Martelo Cruzada", "Bíceps", R.drawable.img_rosca),
    ExecucaoItem("Rosca Martelo Inclinada", "Bíceps", R.drawable.img_rosca),
    ExecucaoItem("Rosca Martelo Scott", "Bíceps", R.drawable.img_rosca),
    ExecucaoItem("Rosca Punho Invertida", "Antebraços", R.drawable.img_rosca),
    ExecucaoItem("Stiff", "Posteriores e glúteos", R.drawable.img_mesa_flexora),
    ExecucaoItem("Supino Declinado", "Peito", R.drawable.img_supino_reto),
    ExecucaoItem("Tríceps Francês", "Tríceps", R.drawable.img_triceps),
    ExecucaoItem("Voador Invertido", "Ombros e costas", R.drawable.img_remada),
    ExecucaoItem("Abdominal Cruzado", "Abdômen", R.drawable.img_prancha),
    ExecucaoItem("Abdominal Declinado", "Abdômen", R.drawable.img_prancha),
    ExecucaoItem("Barra Fixa", "Costas e braços", R.drawable.img_puxada),
    ExecucaoItem("Barra Fixa Supinada", "Costas e bíceps", R.drawable.img_puxada),
    ExecucaoItem("Rack Pull", "Costas e posteriores", R.drawable.img_remada)
)

private val filtrosExecucao = listOf("Todos", "Pernas", "Peito", "Costas", "Ombros", "Braços", "Glúteos", "Abdômen")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExecucoesScreen(onVoltar: () -> Unit) {
    var busca by remember { mutableStateOf("") }
    var filtro by remember { mutableStateOf("Todos") }
    var selecionado by remember { mutableStateOf<ExecucaoItem?>(null) }

    val filtrados = execucoesDisponiveis.filter { item ->
        (filtro == "Todos" || item.grupo.contains(filtro, ignoreCase = true)) &&
            (busca.isBlank() || item.nome.contains(busca, ignoreCase = true) || item.grupo.contains(busca, ignoreCase = true))
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("EXECUÇÕES", fontWeight = FontWeight.ExtraBold)
                        Text("Demonstrações animadas dos exercícios", style = MaterialTheme.typography.labelSmall)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onVoltar) { Icon(Icons.Default.ArrowBack, contentDescription = "Voltar") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { JosyMini(message = "Veja o movimento em animação e confira como executar cada exercício.") }
            item {
                OutlinedTextField(
                    value = busca,
                    onValueChange = { busca = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    placeholder = { Text("Buscar exercício...") }
                )
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    filtrosExecucao.forEach { grupo ->
                        FilterChip(
                            selected = filtro == grupo,
                            onClick = { filtro = grupo },
                            label = { Text(grupo) }
                        )
                    }
                }
            }
            item {
                Text("${filtrados.size} execuções", color = Lilas, fontWeight = FontWeight.Bold)
            }
            items(filtrados, key = { it.nome }) { item ->
                Card(
                    onClick = { selecionado = item },
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ExerciseMedia(
                            name = item.nome,
                            fallbackDrawable = item.imagem,
                            modifier = Modifier.size(112.dp),
                            contentScale = ContentScale.Fit
                        )
                        Column(
                            modifier = Modifier.weight(1f).align(androidx.compose.ui.Alignment.CenterVertically),
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Text(item.nome, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text(item.grupo, color = Lilas, style = MaterialTheme.typography.labelMedium)
                            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                                Icon(Icons.Default.PlayCircle, contentDescription = null, tint = Lilas, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(5.dp))
                                Text("Toque para ampliar GIF", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
            if (filtrados.isEmpty()) {
                item { Text("Nenhuma execução encontrada. Tente outro nome ou grupo.") }
            }
        }
    }

    selecionado?.let { item ->
        ExerciseMediaFullScreen(item.nome, item.imagem) { selecionado = null }
    }
}
