package com.danielmarkpsn.controlecorporal.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "metas")
data class MetaEntity(
    @PrimaryKey val id: Int = 1,      // sempre uma única meta ativa
    val pesoAlvoKg: Float,
    val alturaCm: Float,              // usada para o IMC
    val dataAlvo: Long? = null
)
