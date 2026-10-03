package com.danielmarkpsn.controlecorporal.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MetaDao {

    @Query("SELECT * FROM metas WHERE id = 1 LIMIT 1")
    fun obterMeta(): Flow<MetaEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salvar(meta: MetaEntity)
}
