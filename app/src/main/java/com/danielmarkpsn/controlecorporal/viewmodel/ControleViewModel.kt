package com.danielmarkpsn.controlecorporal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.danielmarkpsn.controlecorporal.data.ControleRepository
import com.danielmarkpsn.controlecorporal.data.MedidaEntity
import com.danielmarkpsn.controlecorporal.data.MetaEntity
import com.danielmarkpsn.controlecorporal.data.PesoEntity
import com.danielmarkpsn.controlecorporal.data.Medicao
import java.util.Calendar
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ControleViewModel(
    private val repository: ControleRepository
) : ViewModel() {

    val pesos: StateFlow<List<PesoEntity>> = repository.listarPesosCronologico()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val medidas: StateFlow<List<MedidaEntity>> = repository.listarMedidas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val tiposMedida: StateFlow<List<String>> = repository.listarTiposDeMedida()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val meta: StateFlow<MetaEntity?> = repository.obterMeta()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val ultimoPeso: StateFlow<PesoEntity?> = repository.ultimoPeso()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // ✅ NOVO — Lista de Medicao combinada (Peso + Medidas) para as telas
    val medicoes: StateFlow<List<Medicao>> = combine(
        repository.listarPesosCronologico(),
        repository.listarMedidas()
    ) { pesos, medidas ->
        fun chaveDia(timestamp: Long): String {
            val calendar = Calendar.getInstance().apply { timeInMillis = timestamp }
            return "${calendar.get(Calendar.YEAR)}-${calendar.get(Calendar.DAY_OF_YEAR)}"
        }

        val pesosPorDia = pesos.groupBy { chaveDia(it.data) }
        val medidasPorDia = medidas.groupBy { chaveDia(it.data) }

        // O histórico deve existir mesmo quando o usuário registrou apenas medidas,
        // sem precisar ter um peso lançado no mesmo dia.
        val dias = (pesosPorDia.keys + medidasPorDia.keys).toSet()

        dias.mapNotNull { chave ->
            val peso = pesosPorDia[chave].orEmpty().maxByOrNull { it.data }
            val medidasDoDia = medidasPorDia[chave].orEmpty()

            fun medida(vararg nomes: String): Float =
                medidasDoDia.firstOrNull { item ->
                    nomes.any { nome -> item.tipo.equals(nome, ignoreCase = true) }
                }?.valorCm ?: 0f

            val data = peso?.data ?: medidasDoDia.maxOfOrNull { it.data } ?: return@mapNotNull null

            Medicao(
                data = data,
                peso = peso?.pesoKg ?: 0f,
                cintura = medida("cintura"),
                abdomen = medida("abdômen", "abdomen"),
                quadril = medida("quadril"),
                peito = medida("peito"),
                bracoDireito = medida("braço direito", "braco direito"),
                bracoEsquerdo = medida("braço esquerdo", "braco esquerdo"),
                coxaDireita = medida("coxa direita"),
                coxaEsquerda = medida("coxa esquerda"),
                panturrilhaDireita = medida("panturrilha direita"),
                panturrilhaEsquerda = medida("panturrilha esquerda")
            )
        }.sortedBy { it.data }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // IMC calculado a partir do último peso + altura da meta
    val imcAtual: StateFlow<Float> = combine(ultimoPeso, meta) { peso, metaAtual ->
        if (peso == null || metaAtual == null || metaAtual.alturaCm <= 0f) 0f
        else repository.calcularImc(peso.pesoKg, metaAtual.alturaCm)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0f)

    // ✅ CORRIGIDO — usa map em vez de combine consigo mesmo
    val classificacaoImc: StateFlow<String> = imcAtual
        .map { imc -> repository.classificarImc(imc) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "—")

    fun adicionarPeso(data: Long, pesoKg: Float, observacao: String?) {
        viewModelScope.launch {
            repository.inserirPeso(PesoEntity(data = data, pesoKg = pesoKg, observacao = observacao))
        }
    }

    fun removerPeso(peso: PesoEntity) {
        viewModelScope.launch { repository.removerPeso(peso) }
    }

    fun adicionarMedida(data: Long, tipo: String, valorCm: Float) {
        viewModelScope.launch {
            repository.inserirMedida(MedidaEntity(data = data, tipo = tipo, valorCm = valorCm))
        }
    }

    fun removerMedida(medida: MedidaEntity) {
        viewModelScope.launch { repository.removerMedida(medida) }
    }

    fun salvarMeta(pesoAlvoKg: Float, alturaCm: Float, dataAlvo: Long?) {
        viewModelScope.launch {
            repository.salvarMeta(MetaEntity(pesoAlvoKg = pesoAlvoKg, alturaCm = alturaCm, dataAlvo = dataAlvo))
        }
    }

    class Factory(private val repository: ControleRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ControleViewModel::class.java)) {
                return ControleViewModel(repository) as T
            }
            throw IllegalArgumentException("ViewModel desconhecido")
        }
    }
}
