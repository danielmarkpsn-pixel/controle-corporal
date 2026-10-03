package com.danielmarkpsn.controlecorporal.data

import kotlinx.coroutines.flow.Flow

class ControleRepository(
    private val pesoDao: PesoDao,
    private val medidaDao: MedidaDao,
    private val metaDao: MetaDao
) {

    // Peso
    fun listarPesos(): Flow<List<PesoEntity>> = pesoDao.listarTodos()
    fun listarPesosCronologico(): Flow<List<PesoEntity>> = pesoDao.listarCronologico()
    fun ultimoPeso(): Flow<PesoEntity?> = pesoDao.ultimoPeso()
    suspend fun inserirPeso(peso: PesoEntity) = pesoDao.inserir(peso)
    suspend fun removerPeso(peso: PesoEntity) = pesoDao.remover(peso)

    // Medidas
    fun listarMedidas(): Flow<List<MedidaEntity>> = medidaDao.listarTodas()
    fun listarMedidasPorTipo(tipo: String): Flow<List<MedidaEntity>> =
        medidaDao.listarPorTipo(tipo)
    fun listarTiposDeMedida(): Flow<List<String>> = medidaDao.listarTipos()
    suspend fun inserirMedida(medida: MedidaEntity) = medidaDao.inserir(medida)
    suspend fun removerMedida(medida: MedidaEntity) = medidaDao.remover(medida)

    // Meta
    fun obterMeta(): Flow<MetaEntity?> = metaDao.obterMeta()
    suspend fun salvarMeta(meta: MetaEntity) = metaDao.salvar(meta)

    // IMC
    fun calcularImc(pesoKg: Float, alturaCm: Float): Float {
        if (alturaCm <= 0f) return 0f
        val alturaM = alturaCm / 100f
        return pesoKg / (alturaM * alturaM)
    }

    fun classificarImc(imc: Float): String = when {
        imc <= 0f -> "—"
        imc < 18.5f -> "Abaixo do peso"
        imc < 25f -> "Peso normal"
        imc < 30f -> "Sobrepeso"
        imc < 35f -> "Obesidade grau I"
        imc < 40f -> "Obesidade grau II"
        else -> "Obesidade grau III"
    }
}
