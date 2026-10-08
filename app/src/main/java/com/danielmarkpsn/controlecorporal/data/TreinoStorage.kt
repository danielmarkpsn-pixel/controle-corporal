package com.danielmarkpsn.controlecorporal.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

data class TreinoExercicio(
    val nome: String, val musculo: String, val series: Int, val repeticoes: Int, val carga: Float,
    val instrucoes: String = "",
    val respiracao: String = "Inspire na fase de retorno e expire durante o esforço.",
    val erros: String = "Evite acelerar o movimento, perder a postura ou usar carga excessiva."
)

data class Treino(val nome: String, val objetivo: String, val exercicios: List<TreinoExercicio>, val dias: List<String> = emptyList())

data class TreinoHistorico(
    val data: Long, val nome: String, val objetivo: String, val duracaoMin: Int, val volume: Float,
    val exerciciosConcluidos: Int, val seriesConcluidas: Int
)

data class SerieRegistro(
    val data: Long, val treinoNome: String, val exercicio: String, val serie: Int, val carga: Float, val repeticoes: Int
) { val volume: Float get() = carga * repeticoes }

class TreinoStorage(context: Context) {
    private val prefs = context.getSharedPreferences("treinos", Context.MODE_PRIVATE)

    fun carregar(): List<Treino> = runCatching {
        val array = JSONArray(prefs.getString("lista", "[]"))
        buildList {
            for (i in 0 until array.length()) {
                val o = array.getJSONObject(i)
                val ea = o.optJSONArray("exercicios") ?: JSONArray()
                val ex = buildList {
                    for (j in 0 until ea.length()) {
                        val e = ea.getJSONObject(j)
                        add(TreinoExercicio(e.optString("nome"), e.optString("musculo", "Geral"), e.optInt("series", 3).coerceAtLeast(1), e.optInt("repeticoes", 10).coerceAtLeast(1), e.optDouble("carga", 0.0).toFloat().coerceAtLeast(0f), e.optString("instrucoes"), e.optString("respiracao", "Inspire na fase de retorno e expire durante o esforço."), e.optString("erros", "Evite acelerar o movimento, perder a postura ou usar carga excessiva.")))
                    }
                }
                val da = o.optJSONArray("dias") ?: JSONArray()
                val dias = buildList { for (d in 0 until da.length()) add(da.optString(d)) }
                add(Treino(o.optString("nome", "Treino"), o.optString("objetivo", "Hipertrofia"), ex, dias))
            }
        }
    }.getOrDefault(emptyList())

    fun salvar(treinos: List<Treino>) {
        val a = JSONArray()
        treinos.forEach { t ->
            val ex = JSONArray()
            t.exercicios.forEach { e -> ex.put(JSONObject().put("nome",e.nome).put("musculo",e.musculo).put("series",e.series).put("repeticoes",e.repeticoes).put("carga",e.carga).put("instrucoes",e.instrucoes).put("respiracao",e.respiracao).put("erros",e.erros)) }
            val dias = JSONArray(); t.dias.forEach { dias.put(it) }
            a.put(JSONObject().put("nome",t.nome).put("objetivo",t.objetivo).put("exercicios",ex).put("dias",dias))
        }
        prefs.edit().putString("lista", a.toString()).apply()
    }

    fun carregarHistorico(): List<TreinoHistorico> = runCatching {
        val a = JSONArray(prefs.getString("historico","[]"))
        buildList {
            for(i in 0 until a.length()){ val o=a.getJSONObject(i); add(TreinoHistorico(o.optLong("data"),o.optString("nome","Treino"),o.optString("objetivo","Hipertrofia"),o.optInt("duracaoMin"),o.optDouble("volume").toFloat(),o.optInt("exerciciosConcluidos"),o.optInt("seriesConcluidas"))) }
        }.sortedByDescending { it.data }
    }.getOrDefault(emptyList())

    fun adicionarHistorico(item: TreinoHistorico) {
        val list=carregarHistorico().toMutableList(); list.add(0,item)
        val a=JSONArray(); list.take(100).forEach { a.put(JSONObject().put("data",it.data).put("nome",it.nome).put("objetivo",it.objetivo).put("duracaoMin",it.duracaoMin).put("volume",it.volume).put("exerciciosConcluidos",it.exerciciosConcluidos).put("seriesConcluidas",it.seriesConcluidas)) }
        prefs.edit().putString("historico",a.toString()).apply()
    }

    fun carregarRegistros(): List<SerieRegistro> = runCatching {
        val a=JSONArray(prefs.getString("registros","[]"))
        buildList { for(i in 0 until a.length()){ val o=a.getJSONObject(i); add(SerieRegistro(o.optLong("data"),o.optString("treinoNome","Treino"),o.optString("exercicio"),o.optInt("serie",1),o.optDouble("carga").toFloat(),o.optInt("repeticoes"))) } }.sortedByDescending { it.data }
    }.getOrDefault(emptyList())

    fun adicionarRegistro(item: SerieRegistro) {
        val list=carregarRegistros().toMutableList(); list.add(0,item)
        val a=JSONArray(); list.take(500).forEach { a.put(JSONObject().put("data",it.data).put("treinoNome",it.treinoNome).put("exercicio",it.exercicio).put("serie",it.serie).put("carga",it.carga).put("repeticoes",it.repeticoes)) }
        prefs.edit().putString("registros",a.toString()).apply()
    }
}
