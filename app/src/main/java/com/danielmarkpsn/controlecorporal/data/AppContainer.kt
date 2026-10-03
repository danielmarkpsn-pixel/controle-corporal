package com.danielmarkpsn.controlecorporal.data

import android.content.Context

class AppContainer(context: Context) {

    private val database: AppDatabase = AppDatabase.getDatabase(context)

    val repository: ControleRepository = ControleRepository(
        pesoDao = database.pesoDao(),
        medidaDao = database.medidaDao(),
        metaDao = database.metaDao()
    )
}
