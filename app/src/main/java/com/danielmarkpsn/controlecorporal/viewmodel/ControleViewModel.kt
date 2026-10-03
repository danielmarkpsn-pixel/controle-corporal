package com.danielmarkpsn.controlecorporal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.danielmarkpsn.controlecorporal.data.ControleRepository
import com.danielmarkpsn.controlecorporal.data.MedidaEntity
import com.danielmarkpsn.controlecorporal.data.MetaEntity
import com.danielmarkpsn.controlecorporal.data.PesoEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
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

    // IMC calculado a partir do último peso + altura da meta
    val imcAtual: StateFlow<Float> = combine(ultimoPeso, meta) { peso, metaAtual ->
        if (peso == null || metaAtual == null || metaAtual.alturaCm <= 0f) 0f
        else repository.calcularImc(peso.pesoKg, metaAtual.alturaCm)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0f)

    val classificacaoImc: StateFlow<String> = imcAtual
        .combine(imcAtual) { imc, _ -> repository.classificarImc(imc) }
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
