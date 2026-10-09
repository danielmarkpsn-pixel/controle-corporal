package com.danielmarkpsn.controlecorporal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.danielmarkpsn.controlecorporal.data.*
import com.danielmarkpsn.controlecorporal.ui.components.JosyHero
import com.danielmarkpsn.controlecorporal.ui.theme.Lilas
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TreinosScreen(onVoltar: () -> Unit) {
    val context=androidx.compose.ui.platform.LocalContext.current
    val storage=remember{TreinoStorage(context)}
    var treinos by remember{mutableStateOf(storage.carregar())}
    var historico by remember{mutableStateOf(storage.carregarHistorico())}
    var registros by remember{mutableStateOf(storage.carregarRegistros())}
    var tab by remember{mutableIntStateOf(0)}
    var filtro by remember{mutableStateOf("Todos")}
    var treinoAtual by remember{mutableStateOf<Treino?>(null)}
    var exercicioAtual by remember{mutableStateOf<TreinoExercicio?>(null)}
    var registroTreino by remember{mutableStateOf("")}

    val divisao=listOf("Todos","ABC","ABCD","PPL","Full Body")
    val filtrados=treinos.filter{filtro=="Todos"||it.nome.startsWith(filtro)}
    val volume=registros.sumOf{it.volume.toDouble()}.toFloat()

    Scaffold(
        containerColor=MaterialTheme.colorScheme.background,
        topBar={TopAppBar(title={Text("Treinos",fontWeight=FontWeight.Bold)},navigationIcon={IconButton(onClick=onVoltar){Icon(Icons.Default.ArrowBack,"Voltar")}},actions={IconButton(onClick={onClick@{tab=2}}){Icon(Icons.Default.MenuBook,"Biblioteca")}},colors=TopAppBarDefaults.topAppBarColors(containerColor=MaterialTheme.colorScheme.background))}
    ){p->
        Column(Modifier.padding(p).fillMaxSize()){
            TabRow(selectedTabIndex=tab,containerColor=MaterialTheme.colorScheme.background){
                listOf("Treinos","Registros","Biblioteca").forEachIndexed{i,t->Tab(tab==i,{tab=i},text={Text(t)})}
            }
            when(tab){
                0->LazyColumn(contentPadding=PaddingValues(16.dp),verticalArrangement=Arrangement.spacedBy(14.dp)){
                    item{JosyHero(title = "Seu treino começa aqui.", message = "A Josy organiza sua rotina, registra suas séries e ajuda você a acompanhar evolução.", actionLabel = "Treinos prontos", onAction = { treinos = (treinos + TreinoPresets.todos).distinctBy { it.nome }; storage.salvar(treinos) })}
                    item{Row(horizontalArrangement=Arrangement.spacedBy(8.dp),modifier=Modifier.fillMaxWidth()){Info("SESSÕES",historico.size.toString(),Icons.Default.EventAvailable,Modifier.weight(1f));Info("VOLUME","%.0f kg".format(volume),Icons.Default.TrendingUp,Modifier.weight(1f));Info("TREINOS",treinos.size.toString(),Icons.Default.FitnessCenter,Modifier.weight(1f))}}
                    item{Text("Divisão de treino",style=MaterialTheme.typography.titleMedium,fontWeight=FontWeight.Bold)}
                    item{Row(horizontalArrangement=Arrangement.spacedBy(5.dp),modifier=Modifier.fillMaxWidth()){divisao.forEach{d->FilterChip(filtro==d,{filtro=d},label={Text(d)},modifier=Modifier.weight(1f))}}}
                    if(filtrados.isEmpty())item{Empty("Nenhum treino nesta divisão.","Use Treinos prontos para começar.")}
                    else items(filtrados){t->Workout(t,{treinoAtual=t},{registroTreino=t.nome;exercicioAtual=t.exercicios.firstOrNull()})}
                }
                1->LazyColumn(contentPadding=PaddingValues(16.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){
                    item{JosyHero("Registros que mostram evolução.","Cada série salva cria um histórico real de carga e repetições.")}
                    item{Row(horizontalArrangement=Arrangement.spacedBy(8.dp),modifier=Modifier.fillMaxWidth()){Info("SÉRIES",registros.size.toString(),Icons.Default.CheckCircle,Modifier.weight(1f));Info("VOLUME","%.0f kg".format(volume),Icons.Default.BarChart,Modifier.weight(1f));Info("SESSÕES",historico.size.toString(),Icons.Default.CalendarMonth,Modifier.weight(1f))}}
                    if(registros.isEmpty())item{Empty("Ainda não há registros.","Abra um treino e registre sua primeira série.")}
                    else items(registros){r->Record(r)}
                }
                else->Library()
            }
        }
    }

    treinoAtual?.let{t->AlertDialog(onDismissRequest={treinoAtual=null},title={Text(t.nome)},text={Column(verticalArrangement=Arrangement.spacedBy(8.dp)){Text(t.objetivo,color=Lilas,fontWeight=FontWeight.Bold);t.exercicios.forEachIndexed{i,e->Row{Text((i+1).toString()+".",color=Lilas,modifier=Modifier.width(24.dp));Text(e.nome+" • "+e.series+" × "+e.repeticoes)}}}},confirmButton={Button(onClick={val item=TreinoHistorico(System.currentTimeMillis(),t.nome,t.objetivo,45,t.exercicios.sumOf{(it.series*it.repeticoes*it.carga).toDouble()}.toFloat(),t.exercicios.size,t.exercicios.sumOf{it.series});storage.adicionarHistorico(item);historico=storage.carregarHistorico();treinoAtual=null}){Text("Concluir treino")}},dismissButton={TextButton(onClick={treinoAtual=null}){Text("Fechar")}})}
    exercicioAtual?.let{e->RegisterDialog(e,registroTreino,{exercicioAtual=null}){s,c,r->storage.adicionarRegistro(SerieRegistro(System.currentTimeMillis(),registroTreino,e.nome,s,c,r));registros=storage.carregarRegistros();exercicioAtual=null}}
}

@Composable private fun Info(t:String,v:String,icon:androidx.compose.ui.graphics.vector.ImageVector,m:Modifier){Card(m,colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surfaceVariant)){Column(Modifier.padding(10.dp)){Icon(icon,null,tint=Lilas);Text(t,style=MaterialTheme.typography.labelSmall,color=MaterialTheme.colorScheme.onSurfaceVariant);Text(v,style=MaterialTheme.typography.titleMedium,fontWeight=FontWeight.Bold)}}}

@Composable private fun Workout(t:Treino,onOpen:()->Unit,onRegister:()->Unit){Card(colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surfaceVariant),modifier=Modifier.fillMaxWidth()){Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(9.dp)){Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween){Column(Modifier.weight(1f)){Text(t.nome,style=MaterialTheme.typography.titleLarge,fontWeight=FontWeight.Bold);Text(t.objetivo,color=Lilas,style=MaterialTheme.typography.labelMedium)};Icon(Icons.Default.FitnessCenter,null,tint=Lilas)};Text(t.exercicios.size.toString()+" exercícios • "+t.dias.joinToString().ifBlank{"sem agenda"},color=MaterialTheme.colorScheme.onSurfaceVariant);t.exercicios.take(4).forEachIndexed{i,e->Text((i+1).toString()+"  "+e.nome+"  •  "+e.series+" × "+e.repeticoes)};Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){OutlinedButton(onClick=onRegister,modifier=Modifier.weight(1f)){Text("Registrar")};Button(onClick=onOpen,modifier=Modifier.weight(1f)){Icon(Icons.Default.PlayArrow,null);Spacer(Modifier.width(4.dp));Text("Abrir")}}}}}

@Composable private fun Record(r:SerieRegistro){val d=remember(r.data){SimpleDateFormat("dd/MM • HH:mm",Locale.getDefault()).format(Date(r.data))};Card(colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surfaceVariant),modifier=Modifier.fillMaxWidth()){Row(Modifier.padding(14.dp),horizontalArrangement=Arrangement.spacedBy(12.dp)){Icon(Icons.Default.FitnessCenter,null,tint=Lilas);Column(Modifier.weight(1f)){Text(r.exercicio,fontWeight=FontWeight.Bold);Text(r.treinoNome,style=MaterialTheme.typography.labelSmall,color=MaterialTheme.colorScheme.onSurfaceVariant);Text(r.carga.toString()+" kg × "+r.repeticoes+" reps",color=Lilas)};Text(d,style=MaterialTheme.typography.labelSmall,color=MaterialTheme.colorScheme.onSurfaceVariant)}}}

@Composable private fun RegisterDialog(e:TreinoExercicio,treino:String,onDismiss:()->Unit,onSave:(Int,Float,Int)->Unit){var s by remember { mutableStateOf("1") };var c by remember{mutableStateOf(if(e.carga>0)e.carga.toString() else "")};var r by remember{mutableStateOf(e.repeticoes.toString())};AlertDialog(onDismissRequest=onDismiss,title={Text("Registrar série")},text={Column(verticalArrangement=Arrangement.spacedBy(8.dp)){Text(e.nome,fontWeight=FontWeight.Bold);Text(treino,color=Lilas);OutlinedTextField(s,{s=it},label={Text("Série")},singleLine=true);OutlinedTextField(c,{c=it},label={Text("Carga (kg)")},singleLine=true);OutlinedTextField(r,{r=it},label={Text("Repetições")},singleLine=true)}},confirmButton={Button(onClick={onSave(s.toIntOrNull()?.coerceAtLeast(1)?:1,c.replace(",","." ).toFloatOrNull()?:0f,r.toIntOrNull()?.coerceAtLeast(1)?:1)}){Text("Salvar")}},dismissButton={TextButton(onClick=onDismiss){Text("Cancelar")}})}

@Composable private fun Library(){val ex=listOf("Agachamento" to "Pernas","Leg Press" to "Pernas","Cadeira Extensora" to "Quadríceps","Mesa Flexora" to "Posterior","Supino Reto" to "Peito","Puxada Frontal" to "Costas","Remada Curvada" to "Costas","Desenvolvimento" to "Ombros","Rosca Direta" to "Bíceps","Tríceps na Polia" to "Tríceps");LazyColumn(contentPadding=PaddingValues(16.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){item{JosyHero("Biblioteca profissional.","Pesquise por exercício ou músculo e mantenha a técnica como prioridade.")};item{OutlinedTextField("",{},Modifier.fillMaxWidth(),singleLine=true,leadingIcon={Icon(Icons.Default.Search,null)},placeholder={Text("Buscar exercício ou músculo...")})};items(ex){(n,g)->Card(colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surfaceVariant),modifier=Modifier.fillMaxWidth()){Row(Modifier.padding(14.dp),horizontalArrangement=Arrangement.spacedBy(12.dp)){Icon(Icons.Default.FitnessCenter,null,tint=Lilas);Column(Modifier.weight(1f)){Text(n,fontWeight=FontWeight.Bold);Text(g,color=Lilas,style=MaterialTheme.typography.labelMedium);Text("Técnica • músculos • dica",style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant)};Icon(Icons.Default.ChevronRight,null,tint=Lilas)}}}}}

@Composable private fun Empty(title:String,text:String){Card(colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surfaceVariant)){Column(Modifier.fillMaxWidth().padding(24.dp)){Icon(Icons.Default.AutoAwesome,null,tint=Lilas);Text(title,style=MaterialTheme.typography.titleMedium,fontWeight=FontWeight.Bold);Text(text,color=MaterialTheme.colorScheme.onSurfaceVariant)}}}
