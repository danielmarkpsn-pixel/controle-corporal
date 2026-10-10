package com.danielmarkpsn.controlecorporal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.danielmarkpsn.controlecorporal.ui.components.JosyHero
import com.danielmarkpsn.controlecorporal.ui.theme.Lilas
import com.danielmarkpsn.controlecorporal.viewmodel.ControleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: ControleViewModel,
    onAdicionarPeso: () -> Unit,
    onAdicionarMedida: () -> Unit,
    onVerHistorico: () -> Unit,
    onVerMeta: () -> Unit,
    onVerGrafico: () -> Unit,
    onVerFotos: () -> Unit,
    onVerRelatorio: () -> Unit,
    onVerTreinos: () -> Unit,
    onVerAnatomia: () -> Unit,
    onVerExecucoes: () -> Unit
) {
    val ultimoPeso by viewModel.ultimoPeso.collectAsStateWithLifecycle()
    val meta by viewModel.meta.collectAsStateWithLifecycle()
    val imc by viewModel.imcAtual.collectAsStateWithLifecycle()
    val classificacao by viewModel.classificacaoImc.collectAsStateWithLifecycle()
    val pesoAtual = ultimoPeso
    val metaAtual = meta

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Column {
                    Text("Controle Corporal", fontWeight = FontWeight.Bold)
                    Text("Sua jornada de evolução", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }},
                actions = { IconButton(onClick = onVerMeta) { Icon(Icons.Default.Tune, "Configurar metas") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            JosyHero(
                title = "Olá! Eu sou a Josy.",
                message = "Sua parceira de evolução. Registre seus dados, treine com foco e acompanhe cada conquista.",
                actionLabel = "Começar treino",
                onAction = onVerTreinos
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                MetricCard("PESO", ultimoPeso?.let { "%.1f kg".format(it.pesoKg) } ?: "—", "Atual", Modifier.weight(1f))
                MetricCard("IMC", if (imc > 0f) "%.1f".format(imc) else "—", if (imc > 0f) classificacao else "Cadastre altura", Modifier.weight(1f))
                MetricCard("META", meta?.pesoAlvoKg?.takeIf { it > 0f }?.let { "%.1f kg".format(it) } ?: "—", "Objetivo", Modifier.weight(1f))
            }
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Icon(Icons.Default.Favorite, contentDescription = null, tint = Lilas, modifier = Modifier.size(28.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        Text("DICA DA JOSY", color = Lilas, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.ExtraBold)
                        Text(
                            when {
                                pesoAtual == null -> "Comece registrando seu peso. Pequenos passos ajudam a enxergar sua evolução!"
                                metaAtual != null && metaAtual.pesoAlvoKg > 0f && pesoAtual.pesoKg <= metaAtual.pesoAlvoKg -> "Você chegou à meta cadastrada! Celebre a conquista e mantenha hábitos consistentes."
                                imc <= 0f -> "Registre sua altura para acompanhar o IMC junto com o peso. Lembre-se: o IMC é apenas um indicador geral."
                                else -> "Treine com técnica, respeite seu descanso e registre seus resultados. Consistência vale mais que perfeição!"
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            Text("Centro de controle", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("Tudo o que você precisa em um só lugar.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            ActionCard("Treinos", "Divisões, exercícios, registros e evolução", Icons.Default.FitnessCenter, onVerTreinos, true)
            ActionCard("Biblioteca de exercícios", "Técnica, músculos envolvidos e dicas", Icons.Default.MenuBook, onVerAnatomia, false)
            ActionCard("EXECUÇÕES", "GIFs animados para visualizar os movimentos", Icons.Default.PlayCircle, onVerExecucoes, true)
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                SmallAction("Evolução", Icons.Default.ShowChart, onVerGrafico, Modifier.weight(1f))
                SmallAction("Histórico", Icons.Default.History, onVerHistorico, Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                SmallAction("Medidas", Icons.Default.Straighten, onAdicionarMedida, Modifier.weight(1f))
                SmallAction("Fotos", Icons.Default.PhotoCamera, onVerFotos, Modifier.weight(1f))
                SmallAction("PDF", Icons.Default.PictureAsPdf, onVerRelatorio, Modifier.weight(1f))
            }
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                    Text("Meta de peso", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    val alvo = meta?.pesoAlvoKg ?: 0f
                    val atual = ultimoPeso?.pesoKg ?: 0f
                    Text(if (alvo > 0f && atual > 0f) {
                        if (atual <= alvo) "Meta atingida. Excelente trabalho!" else "Faltam %.1f kg para sua meta.".format(atual - alvo)
                    } else "Defina sua meta para acompanhar seu progresso.")
                    if (alvo > 0f && atual > 0f) LinearProgressIndicator(progress = { (alvo / atual).coerceIn(0f, 1f) }, modifier = Modifier.fillMaxWidth(), color = Lilas)
                }
            }
            OutlinedButton(onClick = onAdicionarPeso, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Add, null); Spacer(Modifier.width(8.dp)); Text("Registrar nova pesagem")
            }
            Spacer(Modifier.height(72.dp))
        }
    }
}

@Composable
private fun MetricCard(title: String, value: String, subtitle: String, modifier: Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(title, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(subtitle, style = MaterialTheme.typography.labelSmall, color = Lilas)
        }
    }
}

@Composable
private fun ActionCard(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit, featured: Boolean) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = if (featured) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)) {
        Row(Modifier.padding(18.dp), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Surface(shape = MaterialTheme.shapes.medium, color = Lilas.copy(alpha = .18f), modifier = Modifier.size(48.dp)) { Icon(icon, null, tint = Lilas, modifier = Modifier.padding(12.dp)) }
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(Icons.Default.ChevronRight, null, tint = Lilas)
        }
    }
}

@Composable
private fun SmallAction(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit, modifier: Modifier) {
    FilledTonalButton(onClick = onClick, modifier = modifier.height(52.dp), contentPadding = PaddingValues(horizontal = 6.dp, vertical = 8.dp)) { Icon(icon, null, modifier = Modifier.size(18.dp)); Spacer(Modifier.width(4.dp)); Text(title, maxLines = 1, style = MaterialTheme.typography.labelMedium) }
}
