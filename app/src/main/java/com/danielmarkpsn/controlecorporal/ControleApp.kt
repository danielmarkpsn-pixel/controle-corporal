package com.danielmarkpsn.controlecorporal

import android.app.Application
import com.danielmarkpsn.controlecorporal.data.AppContainer

class ControleApp : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
