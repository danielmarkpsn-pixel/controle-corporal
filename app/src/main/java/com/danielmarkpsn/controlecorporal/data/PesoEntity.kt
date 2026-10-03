package com.danielmarkpsn.controlecorporal.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pesos")
data class PesoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val data: Long,          // epoch millis
    val pesoKg: Float,
    val observacao: String? = null
)
