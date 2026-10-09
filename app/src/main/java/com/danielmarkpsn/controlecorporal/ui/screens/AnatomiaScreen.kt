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
import com.danielmarkpsn.controlecorporal.ui.theme.Lilas

private data class Ex(val nome:String,val grupo:String,val foco:String,val tecnica:String,val cuidado:String,val imagem:Int)
private val lista=listOf(
Ex("Agachamento","Pernas","Quadríceps e glúteos","Pés firmes, quadril para trás e descida controlada.","Evite perder a postura.",R.drawable.img_agachamento),
Ex("Leg Press","Pernas","Quadríceps e glúteos","Apoie as costas e empurre a plataforma com controle.","Não deixe a lombar perder o apoio.",R.drawable.img_leg_press),
Ex("Cadeira Extensora","Pernas","Quadríceps","Estenda os joelhos com controle.","Evite movimentos bruscos.",R.drawable.img_cadeira_extensora),
Ex("Mesa Flexora","Pernas","Posteriores","Flexione os joelhos mantendo o corpo estável.","Controle o retorno.",R.drawable.img_mesa_flexora),
Ex("Supino Reto","Peito","Peitoral, tríceps e deltóide anterior","Deite com os pés firmes, estabilize as escápulas, desça a carga com controle e empurre sem perder a postura.","Não quique a carga nem force os ombros.",R.drawable.img_supino_reto),
Ex("Crucifixo","Peito","Peitoral maior","Mantenha os cotovelos levemente flexionados e abra os braços até uma amplitude confortável; retorne aproximando as mãos.","Evite alongar além do confortável ou usar impulso.",R.drawable.img_crucifixo),
Ex("Puxada Frontal","Costas","Latíssimo do dorso","Conduza a barra com os cotovelos.","Evite balançar o tronco.",R.drawable.img_puxada),
Ex("Remada Curvada","Costas","Dorsais e trapézio","Mantenha coluna estável e puxe com os cotovelos.","Reduza a carga se perder a postura.",R.drawable.img_remada),
Ex("Desenvolvimento","Ombros","Deltóides","Empurre acima da cabeça com tronco firme.","Evite compensar com a lombar.",R.drawable.img_desenvolvimento),
Ex("Elevação Lateral","Ombros","Deltóide lateral","Eleve os braços com controle.","Evite balanço.",R.drawable.img_elevacao_lateral),
Ex("Rosca Direta","Braços","Bíceps","Flexione os braços sem impulso.","Não use as costas.",R.drawable.img_rosca),
Ex("Tríceps na Polia","Braços","Tríceps","Estenda os antebraços com cotovelos estáveis e controle o retorno.","Evite abrir os cotovelos.",R.drawable.img_triceps),
Ex("Prancha","Abdômen","Reto abdominal e estabilizadores do tronco","Apoie antebraços e pontas dos pés, mantenha o corpo alinhado e respire sem prender o ar.","Pare se sentir dor lombar e evite deixar o quadril cair.",R.drawable.img_prancha),
Ex("Abdominal","Abdômen","Reto abdominal","Eleve o tronco de forma curta e controlada, aproximando as costelas da pelve.","Evite puxar o pescoço ou fazer movimentos bruscos.",R.drawable.img_abdominal)
)
private val grupos=listOf("Todos","Pernas","Peito","Costas","Ombros","Braços","Abdômen")

@OptIn(ExperimentalMaterial3Api::class)
@Composable fun AnatomiaScreen(onVoltar:()->Unit){
    var filtro by remember { mutableStateOf("Todos") };var busca by remember{mutableStateOf("")};var selecionado by remember{mutableStateOf<Ex?>(null)}
    val filtrados=lista.filter{(filtro=="Todos"||it.grupo==filtro)&&(busca.isBlank()||it.nome.contains(busca,true)||it.foco.contains(busca,true))}
    Scaffold(containerColor=MaterialTheme.colorScheme.background,topBar={TopAppBar(title={Text("Biblioteca",fontWeight=FontWeight.Bold)},navigationIcon={IconButton(onClick=onVoltar){Icon(Icons.Default.ArrowBack,"Voltar")}},colors=TopAppBarDefaults.topAppBarColors(containerColor=MaterialTheme.colorScheme.background))}){p->
        LazyColumn(Modifier.padding(p).fillMaxSize(),contentPadding=PaddingValues(16.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){
            item{JosyMini(message="Escolha um exercício e eu te mostro foco, técnica e cuidados.")}
            item{OutlinedTextField(busca,{busca=it},Modifier.fillMaxWidth(),singleLine=true,leadingIcon={Icon(Icons.Default.Search,null)},placeholder={Text("Buscar exercício ou músculo...")})}
            item{Row(horizontalArrangement=Arrangement.spacedBy(8.dp),modifier=Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())){grupos.forEach{g->FilterChip(selected=filtro==g,onClick={filtro=g},label={Text(g,maxLines=1)})}}}
            item{Text(filtrados.size.toString()+" exercícios disponíveis",color=Lilas,fontWeight=FontWeight.Bold)}
            items(filtrados){e->Card(onClick={selecionado=e},colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surfaceVariant),modifier=Modifier.fillMaxWidth()){Row(Modifier.padding(10.dp),horizontalArrangement=Arrangement.spacedBy(12.dp)){Image(painterResource(e.imagem),e.nome,Modifier.size(92.dp),contentScale=ContentScale.Fit);Column(Modifier.weight(1f)){Text(e.nome,style=MaterialTheme.typography.titleMedium,fontWeight=FontWeight.Bold);Text(e.grupo,color=Lilas,style=MaterialTheme.typography.labelMedium);Text(e.foco,style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant);Text("Ver ficha completa  ›",color=Lilas,fontWeight=FontWeight.SemiBold)}}}}
        }
    }
    selecionado?.let{e->Dialog(onDismissRequest={selecionado=null},properties=DialogProperties(usePlatformDefaultWidth=false)){Box(Modifier.fillMaxSize().padding(0.dp)){Image(painterResource(e.imagem),contentDescription=null,modifier=Modifier.fillMaxSize(),contentScale=ContentScale.Fit)}}}
}
