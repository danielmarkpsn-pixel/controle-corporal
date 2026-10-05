package com.danielmarkpsn.controlecorporal.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

data class TreinoExercicio(
    val nome: String,
    val musculo: String,
    val series: Int,
    val repeticoes: Int,
    val carga: Float,
    val instrucoes: String = "",
    val respiracao: String = "Inspire na fase de retorno e expire durante o esforço.",
    val erros: String = "Evite acelerar o movimento, perder a postura ou usar carga excessiva."
)

data class Treino(
    val nome: String,
    val objetivo: String,
    val exercicios: List<TreinoExercicio>,
    val dias: List<String> = emptyList()
)

data class TreinoHistorico(
    val data: Long,
    val nome: String,
    val objetivo: String,
    val duracaoMin: Int,
    val volume: Float,
    val exerciciosConcluidos: Int,
    val seriesConcluidas: Int
)

class TreinoStorage(context: Context) {
    private val prefs = context.getSharedPreferences("treinos", Context.MODE_PRIVATE)

    fun carregar(): List<Treino> = runCatching {
        val array = JSONArray(prefs.getString("lista", "[]"))
        buildList {
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val exArray = obj.optJSONArray("exercicios") ?: JSONArray()
                val ex = buildList {
                    for (j in 0 until exArray.length()) {
                        val e = exArray.getJSONObject(j)
                        add(
                            TreinoExercicio(
                                nome = e.optString("nome"),
                                musculo = e.optString("musculo", "Geral"),
                                series = e.optInt("series", 3).coerceAtLeast(1),
                                repeticoes = e.optInt("repeticoes", 10).coerceAtLeast(1),
                                carga = e.optDouble("carga", 0.0).toFloat().coerceAtLeast(0f),
                                instrucoes = e.optString("instrucoes", ""),
                                respiracao = e.optString(
                                    "respiracao",
                                    "Inspire na fase de retorno e expire durante o esforço."
                                ),
                                erros = e.optString(
                                    "erros",
                                    "Evite acelerar o movimento, perder a postura ou usar carga excessiva."
                                )
                            )
                        )
                    }
                }
                val diasArray = obj.optJSONArray("dias") ?: JSONArray()
                val dias = buildList {
                    for (d in 0 until diasArray.length()) add(diasArray.optString(d))
                }
                add(
                    Treino(
                        nome = obj.optString("nome", "Treino"),
                        objetivo = obj.optString("objetivo", "Hipertrofia"),
                        exercicios = ex,
                        dias = dias
                    )
                )
            }
        }
    }.getOrDefault(emptyList())

    fun salvar(treinos: List<Treino>) {
        val array = JSONArray()
        treinos.forEach { treino ->
            val obj = JSONObject()
                .put("nome", treino.nome)
                .put("objetivo", treino.objetivo)
            val ex = JSONArray()
            treino.exercicios.forEach { e ->
                ex.put(
                    JSONObject()
                        .put("nome", e.nome)
                        .put("musculo", e.musculo)
                        .put("series", e.series)
                        .put("repeticoes", e.repeticoes)
                        .put("carga", e.carga)
                        .put("instrucoes", e.instrucoes)
                        .put("respiracao", e.respiracao)
                        .put("erros", e.erros)
                )
            }
            val dias = JSONArray()
            treino.dias.forEach { dias.put(it) }
            obj.put("exercicios", ex).put("dias", dias)
            array.put(obj)
        }
        prefs.edit().putString("lista", array.toString()).apply()
    }

    fun carregarHistorico(): List<TreinoHistorico> = runCatching {
        val array = JSONArray(prefs.getString("historico", "[]"))
        buildList {
            for (i in 0 until array.length()) {
                val o = array.getJSONObject(i)
                add(
                    TreinoHistorico(
                        data = o.optLong("data"),
                        nome = o.optString("nome", "Treino"),
                        objetivo = o.optString("objetivo", "Hipertrofia"),
                        duracaoMin = o.optInt("duracaoMin", 0),
                        volume = o.optDouble("volume", 0.0).toFloat(),
                        exerciciosConcluidos = o.optInt("exerciciosConcluidos", 0),
                        seriesConcluidas = o.optInt("seriesConcluidas", 0)
                    )
                )
            }
        }.sortedByDescending { it.data }
    }.getOrDefault(emptyList())

    fun adicionarHistorico(item: TreinoHistorico) {
        val atual = carregarHistorico().toMutableList()
        atual.add(0, item)
        val array = JSONArray()
        atual.take(100).forEach {
            array.put(
                JSONObject()
                    .put("data", it.data)
                    .put("nome", it.nome)
                    .put("objetivo", it.objetivo)
                    .put("duracaoMin", it.duracaoMin)
                    .put("volume", it.volume)
                    .put("exerciciosConcluidos", it.exerciciosConcluidos)
                    .put("seriesConcluidas", it.seriesConcluidas)
            )
        }
        prefs.edit().putString("historico", array.toString()).apply()
    }
}
