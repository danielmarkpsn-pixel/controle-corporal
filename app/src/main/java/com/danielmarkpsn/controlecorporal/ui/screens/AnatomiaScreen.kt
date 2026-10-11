package com.danielmarkpsn.controlecorporal.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.danielmarkpsn.controlecorporal.R
import com.danielmarkpsn.controlecorporal.ui.components.JosyMini
import com.danielmarkpsn.controlecorporal.ui.components.ExerciseMedia
import com.danielmarkpsn.controlecorporal.ui.components.ExerciseMediaFullScreen
import com.danielmarkpsn.controlecorporal.ui.theme.Lilas

private data class Ex(val nome:String,val grupo:String,val foco:String,val tecnica:String,val cuidado:String,val imagem:Int)
private val lista=listOf(
Ex("Agachamento","Pernas","Quadríceps e glúteos","Pés firmes, quadril para trás e descida controlada.","Evite perder a postura.",R.drawable.img_agachamento),
Ex("Agachamento Sumô","Pernas","Adutores e glúteos","Afaste os pés, mantenha as pontas ligeiramente para fora e desça com o tronco firme.","Mantenha os joelhos alinhados com os pés.",R.drawable.img_agachamento_sumo),
Ex("Agachamento com Kettlebell","Pernas","Quadríceps e glúteos","Segure o kettlebell junto ao peito e agache mantendo o tronco estável.","Evite arredondar as costas.",R.drawable.img_agachamento_kettlebell),
Ex("Agachamento Búlgaro","Pernas","Quadríceps e glúteos","Apoie o pé de trás em um banco e desça controladamente com a perna da frente.","Mantenha equilíbrio e joelho alinhado.",R.drawable.img_bulgaro),
Ex("Afundo","Pernas","Quadríceps e glúteos","Dê um passo à frente e flexione os joelhos, depois retorne à posição inicial.","Evite perder o equilíbrio ou deixar o joelho colapsar.",R.drawable.img_afundo),
Ex("Leg Press","Pernas","Quadríceps e glúteos","Apoie as costas e empurre a plataforma com controle.","Não deixe a lombar perder o apoio.",R.drawable.img_leg_press),
Ex("Cadeira Extensora","Pernas","Quadríceps","Estenda os joelhos com controle.","Evite movimentos bruscos.",R.drawable.img_cadeira_extensora),
Ex("Mesa Flexora","Pernas","Posteriores","Flexione os joelhos mantendo o corpo estável.","Controle o retorno.",R.drawable.img_mesa_flexora),
Ex("Levantamento Terra Romeno (Stiff)","Pernas","Posteriores e glúteos","Leve o quadril para trás com a coluna neutra e mantenha a carga próxima às pernas.","Não arredonde a lombar.",R.drawable.img_levantamento_terra_romeno),
Ex("Elevação Pélvica","Glúteos","Glúteo máximo","Apoie as costas, empurre pelos pés e eleve o quadril contraindo os glúteos.","Evite hiperestender a lombar.",R.drawable.img_elevacao_pelvica),
Ex("Elevação de Panturrilha","Pernas","Panturrilhas","Eleve os calcanhares e desça lentamente até uma amplitude confortável.","Evite quicar durante o movimento.",R.drawable.img_panturrilha),
Ex("Supino Reto","Peito","Peitoral, tríceps e deltóide anterior","Deite com os pés firmes, estabilize as escápulas, desça a carga com controle e empurre sem perder a postura.","Não quique a carga nem force os ombros.",R.drawable.img_supino_reto),
Ex("Crucifixo","Peito","Peitoral maior","Mantenha os cotovelos levemente flexionados e abra os braços até uma amplitude confortável; retorne aproximando as mãos.","Evite alongar além do confortável ou usar impulso.",R.drawable.img_crucifixo),
Ex("Supino Inclinado","Peito","Peitoral superior e tríceps","Mantenha os pés apoiados, desça a carga até uma amplitude confortável e empurre com controle.","Não arqueie excessivamente a lombar.",R.drawable.img_supino_inclinado),
Ex("Crossover na Polia","Peito","Peitoral maior","Traga as mãos à frente do corpo com os cotovelos levemente flexionados e movimento controlado.","Evite usar impulso do tronco.",R.drawable.img_crossover_com_polia),
Ex("Flexão de Braços","Peito","Peitoral, tríceps e ombros","Mantenha o corpo alinhado e flexione os cotovelos para aproximar o peito do chão.","Não deixe o quadril cair.",R.drawable.img_flexoes_de_braco),
Ex("Puxada Frontal","Costas","Latíssimo do dorso","Conduza a barra com os cotovelos.","Evite balançar o tronco.",R.drawable.img_puxada_alta),
Ex("Remada Curvada","Costas","Dorsais e trapézio","Mantenha coluna estável e puxe com os cotovelos.","Reduza a carga se perder a postura.",R.drawable.img_remada),
Ex("Desenvolvimento","Ombros","Deltóides","Empurre acima da cabeça com tronco firme.","Evite compensar com a lombar.",R.drawable.img_desenvolvimento),
Ex("Elevação Lateral","Ombros","Deltóide lateral","Eleve os braços com controle.","Evite balanço.",R.drawable.img_elevacao_lateral),
Ex("Rosca Direta","Braços","Bíceps","Flexione os braços sem impulso.","Não use as costas.",R.drawable.img_rosca_direta),
Ex("Tríceps na Polia","Braços","Tríceps","Estenda os antebraços com cotovelos estáveis e controle o retorno.","Evite abrir os cotovelos.",R.drawable.img_triceps_na_polia),
Ex("Prancha","Abdômen","Reto abdominal e estabilizadores do tronco","Apoie antebraços e pontas dos pés, mantenha o corpo alinhado e respire sem prender o ar.","Pare se sentir dor lombar e evite deixar o quadril cair.",R.drawable.img_prancha),
Ex("Abdominal","Abdômen","Reto abdominal","Eleve o tronco de forma curta e controlada, aproximando as costelas da pelve.","Evite puxar o pescoço ou fazer movimentos bruscos.",R.drawable.img_abdominal)
)
private val grupos=listOf("Todos","Pernas","Peito","Costas","Ombros","Braços","Abdômen")

@OptIn(ExperimentalMaterial3Api::class)
@Composable fun AnatomiaScreen(onVoltar:()->Unit){
    var filtro by remember { mutableStateOf("Todos") };var busca by remember{mutableStateOf("")};var selecionado by remember{mutableStateOf<Ex?>(null)}
    val filtrados=lista.filter{(filtro=="Todos"||it.grupo==filtro)&&(busca.isBlank()||it.nome.contains(busca,true)||it.foco.contains(busca,true))}
    Scaffold(containerColor=MaterialTheme.colorScheme.background,topBar={TopAppBar(title={Text("DICAS DA JOSY",fontWeight=FontWeight.Bold)},navigationIcon={IconButton(onClick=onVoltar){Icon(Icons.Default.ArrowBack,"Voltar")}},colors=TopAppBarDefaults.topAppBarColors(containerColor=MaterialTheme.colorScheme.background))}){p->
        LazyColumn(Modifier.padding(p).fillMaxSize(),contentPadding=PaddingValues(16.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){
            item{JosyMini(message="Escolha um exercício e eu te mostro foco, técnica e cuidados.")}
            item{OutlinedTextField(busca,{busca=it},Modifier.fillMaxWidth(),singleLine=true,leadingIcon={Icon(Icons.Default.Search,null)},placeholder={Text("Buscar exercício ou músculo...")})}
            item{Row(horizontalArrangement=Arrangement.spacedBy(8.dp),modifier=Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())){grupos.forEach{g->FilterChip(selected=filtro==g,onClick={filtro=g},label={Text(g,maxLines=1)})}}}
            item{Text(filtrados.size.toString()+" exercícios disponíveis",color=Lilas,fontWeight=FontWeight.Bold)}
            items(filtrados){e->Card(onClick={selecionado=e},colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surfaceVariant),modifier=Modifier.fillMaxWidth()){Row(Modifier.padding(10.dp),horizontalArrangement=Arrangement.spacedBy(12.dp)){ExerciseMedia(e.nome, e.imagem, Modifier.size(92.dp), contentScale=ContentScale.Fit, useGif = false, usePng = true);Column(Modifier.weight(1f)){Text(e.nome,style=MaterialTheme.typography.titleMedium,fontWeight=FontWeight.Bold);Text(e.grupo,color=Lilas,style=MaterialTheme.typography.labelMedium);Text(e.foco,style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant);Text("Ver ficha completa  ›",color=Lilas,fontWeight=FontWeight.SemiBold)}}}}
        }
    }
    selecionado?.let { e -> ExerciseMediaFullScreen(e.nome, e.imagem, { selecionado = null }, useGif = false, usePng = true) }
}
