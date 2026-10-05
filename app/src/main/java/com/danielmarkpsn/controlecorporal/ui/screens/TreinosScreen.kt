package com.danielmarkpsn.controlecorporal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private data class Exercise(val name:String,val muscle:String,val execution:String,val breathing:String,val error:String,val suggestion:String)

private val exercises = listOf(
Exercise("Supino reto","Peito","Deite-se no banco, mantenha os pés firmes e desça a barra de forma controlada até a linha do peito. Empurre sem perder o controle.","Inspire na descida e solte o ar durante a subida.","Abrir excessivamente os cotovelos ou tirar os ombros do banco.","3–4 séries de 8–12 repetições • descanso de 60–120 s"),
Exercise("Remada curvada","Costas","Incline o tronco com coluna neutra, puxe a carga em direção ao abdômen e retorne lentamente.","Inspire ao retornar e expire ao puxar.","Arredondar a lombar ou usar impulso.","3–4 séries de 8–12 repetições • descanso de 60–120 s"),
Exercise("Puxada frontal","Costas","Sente-se, firme as pernas e puxe a barra em direção à parte superior do peito, mantendo o tronco estável.","Expire durante a puxada e inspire na volta.","Puxar atrás da nuca ou balançar o corpo.","3 séries de 10–12 repetições • descanso de 60–90 s"),
Exercise("Desenvolvimento de ombros","Ombros","Mantenha o tronco estável e empurre os pesos acima da cabeça sem arquear a lombar.","Expire ao subir e inspire ao descer.","Arquear excessivamente a lombar.","3 séries de 8–12 repetições • descanso de 60–120 s"),
Exercise("Rosca direta","Bíceps","Mantenha os cotovelos próximos ao corpo e flexione os braços sem balançar o tronco.","Expire ao subir e inspire ao descer.","Usar o corpo para lançar a carga.","3 séries de 10–12 repetições • descanso de 60–90 s"),
Exercise("Tríceps na polia","Tríceps","Com os cotovelos junto ao corpo, empurre a barra para baixo e retorne controladamente.","Expire ao estender os braços.","Movimentar os cotovelos para frente e para trás.","3 séries de 10–15 repetições • descanso de 60–90 s"),
Exercise("Agachamento","Pernas e glúteos","Afaste os pés em posição confortável, desça mantendo o tronco estável e suba pressionando o chão.","Inspire na descida e expire na subida.","Perder o controle da descida ou deixar os joelhos colapsarem para dentro.","3–4 séries de 6–12 repetições • descanso de 90–180 s"),
Exercise("Leg press","Pernas e glúteos","Apoie completamente as costas, desça a plataforma com controle e empurre sem tirar o quadril do encosto.","Inspire na descida e expire ao empurrar.","Descer além do controle ou travar os joelhos.","3 séries de 10–15 repetições • descanso de 90–120 s"),
Exercise("Elevação pélvica","Pernas e glúteos","Apoie as costas, mantenha os pés firmes e eleve o quadril até alinhar tronco e coxas.","Expire ao elevar o quadril.","Compensar com excesso de movimento da lombar.","3–4 séries de 8–15 repetições • descanso de 60–120 s"),
Exercise("Abdominal curto","Abdômen","Deite-se, mantenha o abdômen contraído e eleve o tronco apenas o necessário, sem puxar o pescoço.","Expire durante a contração.","Puxar a cabeça com as mãos.","3 séries de 12–20 repetições • descanso de 45–60 s"),
Exercise("Caminhada inclinada","Cardio","Caminhe em ritmo confortável, mantendo postura estável e aumentando a inclinação gradualmente.","Respiração contínua e confortável.","Apoiar todo o peso nos braços do aparelho.","15–30 minutos, ajustando a intensidade ao condicionamento")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TreinosScreen(onVoltar:()->Unit) {
    var selectedCategory by remember { mutableStateOf("Todos") }
    var selectedExercise by remember { mutableStateOf<Exercise?>(null) }
    var showProgram by remember { mutableStateOf(false) }
    val categories = listOf("Todos","Peito","Costas","Ombros","Bíceps","Tríceps","Pernas e glúteos","Abdômen","Cardio")
    val filtered = exercises.filter { selectedCategory=="Todos" || it.muscle==selectedCategory }

    Scaffold(topBar={
        TopAppBar(title={Text("Treinos",fontWeight=FontWeight.Bold)},
            navigationIcon={IconButton(onClick=onVoltar){Icon(Icons.Default.ArrowBack,"Voltar")}},
            actions={IconButton(onClick={showProgram=true}){Icon(Icons.Default.FitnessCenter,"Programar treino")}})
    }) { padding ->
        LazyColumn(Modifier.padding(padding).fillMaxSize(),contentPadding=PaddingValues(16.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
            item {
                Card(colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primaryContainer)) {
                    Column(Modifier.padding(18.dp)) {
                        Text("🏋️ Sua área de treino",style=MaterialTheme.typography.headlineSmall,fontWeight=FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))
                        Text("Aprenda a executar exercícios, organize sua musculação e monte uma programação de treino.")
                        Spacer(Modifier.height(12.dp))
                        Button(onClick={showProgram=true}){Icon(Icons.Default.FitnessCenter,null);Spacer(Modifier.width(8.dp));Text("Montar programação")}
                    }
                }
            }
            item { Text("Biblioteca de exercícios",style=MaterialTheme.typography.titleLarge,fontWeight=FontWeight.Bold) }
            item {
                Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(6.dp)) {
                    categories.take(3).forEach { c ->
                        FilterChip(selected=selectedCategory==c,onClick={selectedCategory=c},label={Text(c)})
                    }
                }
            }
            item {
                if (selectedCategory!="Todos") TextButton(onClick={selectedCategory="Todos"}){Text("Mostrar todos os exercícios")}
            }
            items(filtered) { exercise ->
                Card(onClick={selectedExercise=exercise},modifier=Modifier.fillMaxWidth()) {
                    Row(Modifier.padding(16.dp)) {
                        Icon(Icons.Default.FitnessCenter,null);Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)){Text(exercise.name,style=MaterialTheme.typography.titleMedium,fontWeight=FontWeight.Bold);Text(exercise.muscle,style=MaterialTheme.typography.labelMedium);Spacer(Modifier.height(4.dp));Text(exercise.suggestion,style=MaterialTheme.typography.bodySmall)}
                        Icon(Icons.Default.Info,"Ver instruções")
                    }
                }
            }
            item { Text("Importante: estas sugestões são gerais. Carga, volume e intensidade devem ser adaptados ao seu nível e objetivo. Em caso de dor, lesão ou condição clínica, procure orientação profissional.",style=MaterialTheme.typography.bodySmall) }
        }
    }

    selectedExercise?.let { exercise ->
        AlertDialog(onDismissRequest={selectedExercise=null},title={Text(exercise.name)},text={
            Column {
                Text("Músculo: ${exercise.muscle}",fontWeight=FontWeight.Bold);Spacer(Modifier.height(10.dp))
                Text("Como executar",fontWeight=FontWeight.Bold);Text(exercise.execution);Spacer(Modifier.height(10.dp))
                Text("Respiração",fontWeight=FontWeight.Bold);Text(exercise.breathing);Spacer(Modifier.height(10.dp))
                Text("Erro comum",fontWeight=FontWeight.Bold);Text(exercise.error);Spacer(Modifier.height(10.dp))
                Text("Sugestão geral",fontWeight=FontWeight.Bold);Text(exercise.suggestion)
            }
        },confirmButton={TextButton(onClick={selectedExercise=null}){Text("Fechar")}})
    }
    if(showProgram) ProgramDialog{showProgram=false}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProgramDialog(onClose:()->Unit) {
    var days by remember { mutableStateOf(3) }
    var goal by remember { mutableStateOf("Hipertrofia") }
    var division by remember { mutableStateOf("A/B/C") }
    var saved by remember { mutableStateOf(false) }
    AlertDialog(onDismissRequest=onClose,title={Text("Programar treino")},text={
        Column(verticalArrangement=Arrangement.spacedBy(12.dp)) {
            Text("Objetivo",fontWeight=FontWeight.Bold)
            Row(horizontalArrangement=Arrangement.spacedBy(4.dp)){listOf("Hipertrofia","Força","Resistência").forEach{o->FilterChip(selected=goal==o,onClick={goal=o},label={Text(o)})}}
            Text("Dias por semana: $days",fontWeight=FontWeight.Bold)
            Slider(value=days.toFloat(),onValueChange={days=it.toInt().coerceIn(2,6)},valueRange=2f..6f,steps=3)
            Text("Divisão",fontWeight=FontWeight.Bold)
            Row(horizontalArrangement=Arrangement.spacedBy(4.dp)){listOf("A/B","A/B/C","AB/CD").forEach{o->FilterChip(selected=division==o,onClick={division=o},label={Text(o)})}}
            if(saved) Text("✓ Programação preparada: $goal • $days dias • $division",fontWeight=FontWeight.Bold)
        }
    },confirmButton={Button(onClick={saved=true}){Text(if(saved)"Atualizar" else "Salvar")}},dismissButton={TextButton(onClick=onClose){Text("Fechar")}})
}
