package com.danielmarkpsn.controlecorporal.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PesoDao {

    @Query("SELECT * FROM pesos ORDER BY data DESC")
    fun listarTodos(): Flow<List<PesoEntity>>

    @Query("SELECT * FROM pesos ORDER BY data ASC")
    fun listarCronologico(): Flow<List<PesoEntity>>

    @Query("SELECT * FROM pesos ORDER BY data DESC LIMIT 1")
    fun ultimoPeso(): Flow<PesoEntity?>

    @Insert
    suspend fun inserir(peso: PesoEntity)

    @Delete
    suspend fun remover(peso: PesoEntity)
}
