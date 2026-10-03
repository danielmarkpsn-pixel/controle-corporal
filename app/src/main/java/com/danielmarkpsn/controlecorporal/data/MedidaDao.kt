package com.danielmarkpsn.controlecorporal.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MedidaDao {

    @Query("SELECT * FROM medidas ORDER BY data DESC")
    fun listarTodas(): Flow<List<MedidaEntity>>

    @Query("SELECT * FROM medidas WHERE tipo = :tipo ORDER BY data ASC")
    fun listarPorTipo(tipo: String): Flow<List<MedidaEntity>>

    @Query("SELECT DISTINCT tipo FROM medidas ORDER BY tipo ASC")
    fun listarTipos(): Flow<List<String>>

    @Insert
    suspend fun inserir(medida: MedidaEntity)

    @Delete
    suspend fun remover(medida: MedidaEntity)
}
