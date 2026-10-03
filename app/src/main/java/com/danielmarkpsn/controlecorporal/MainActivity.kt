package com.danielmarkpsn.controlecorporal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.danielmarkpsn.controlecorporal.ui.navigation.AppNavigation
import com.danielmarkpsn.controlecorporal.ui.theme.ControleCorporalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ControleCorporalTheme {
                AppNavigation()
            }
        }
    }
}
