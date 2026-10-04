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
        fun dia(timestamp: Long): IntArray {
            val calendar = Calendar.getInstance().apply { timeInMillis = timestamp }
            return intArrayOf(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.DAY_OF_YEAR)
            )
        }

        val medidasPorDia = medidas.groupBy { dia(it.data).contentToString() }

        pesos.map { peso ->
            val medidasDoDia = medidasPorDia[dia(peso.data).contentToString()].orEmpty()
            Medicao(
                data = peso.data,
                peso = peso.pesoKg,
                cintura = medidasDoDia.find { it.tipo.equals("cintura", ignoreCase = true) }?.valorCm ?: 0f,
                abdomen = medidasDoDia.find { it.tipo.equals("abdômen", ignoreCase = true) || it.tipo.equals("abdomen", ignoreCase = true) }?.valorCm ?: 0f,
                quadril = medidasDoDia.find { it.tipo.equals("quadril", ignoreCase = true) }?.valorCm ?: 0f,
                peito = medidasDoDia.find { it.tipo.equals("Peito", ignoreCase = true) }?.valorCm ?: 0f,
                bracoDireito = medidasDoDia.find { it.tipo.equals("Braço direito", ignoreCase = true) }?.valorCm ?: 0f,
                bracoEsquerdo = medidasDoDia.find { it.tipo.equals("Braço esquerdo", ignoreCase = true) }?.valorCm ?: 0f,
                coxaDireita = medidasDoDia.find { it.tipo.equals("Coxa direita", ignoreCase = true) }?.valorCm ?: 0f,
                coxaEsquerda = medidasDoDia.find { it.tipo.equals("Coxa esquerda", ignoreCase = true) }?.valorCm ?: 0f,
                panturrilhaDireita = medidasDoDia.find { it.tipo.equals("Panturrilha direita", ignoreCase = true) }?.valorCm ?: 0f,
                panturrilhaEsquerda = medidasDoDia.find { it.tipo.equals("Panturrilha esquerda", ignoreCase = true) }?.valorCm ?: 0f
            )
        }
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
