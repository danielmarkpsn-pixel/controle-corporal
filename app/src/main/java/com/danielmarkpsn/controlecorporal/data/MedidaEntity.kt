package com.danielmarkpsn.controlecorporal.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medidas")
data class MedidaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val data: Long,          // epoch millis
    val tipo: String,        // cintura, quadril, braço, coxa, etc.
    val valorCm: Float
)
