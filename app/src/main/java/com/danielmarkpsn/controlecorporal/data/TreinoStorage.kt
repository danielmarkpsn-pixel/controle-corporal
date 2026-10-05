package com.danielmarkpsn.controlecorporal.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

data class TreinoExercicio(
    val nome: String,
    val musculo: String,
    val series: Int,
    val repeticoes: Int,
    val carga: Float
)

data class Treino(
    val nome: String,
    val objetivo: String,
    val exercicios: List<TreinoExercicio>
)

class TreinoStorage(context: Context) {
    private val prefs = context.getSharedPreferences("treinos", Context.MODE_PRIVATE)

    fun carregar(): List<Treino> {
        return runCatching {
            val array = JSONArray(prefs.getString("lista", "[]"))
            buildList {
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    val exArray = obj.optJSONArray("exercicios") ?: JSONArray()
                    val ex = buildList {
                        for (j in 0 until exArray.length()) {
                            val e = exArray.getJSONObject(j)
                            add(TreinoExercicio(e.getString("nome"), e.getString("musculo"), e.optInt("series",3), e.optInt("repeticoes",10), e.optDouble("carga",0.0).toFloat()))
                        }
                    }
                    add(Treino(obj.getString("nome"), obj.optString("objetivo","Hipertrofia"), ex))
                }
            }
        }.getOrDefault(emptyList())
    }

    fun salvar(treinos: List<Treino>) {
        val array = JSONArray()
        treinos.forEach { treino ->
            val obj = JSONObject().put("nome", treino.nome).put("objetivo", treino.objetivo)
            val ex = JSONArray()
            treino.exercicios.forEach { e ->
                ex.put(JSONObject().put("nome",e.nome).put("musculo",e.musculo).put("series",e.series).put("repeticoes",e.repeticoes).put("carga",e.carga))
            }
            obj.put("exercicios",ex)
            array.put(obj)
        }
        prefs.edit().putString("lista",array.toString()).apply()
    }
}
