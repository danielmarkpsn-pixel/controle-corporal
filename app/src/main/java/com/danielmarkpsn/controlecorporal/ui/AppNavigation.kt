package com.danielmarkpsn.controlecorporal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.danielmarkpsn.controlecorporal.ui.screens.AddMedidaScreen
import com.danielmarkpsn.controlecorporal.ui.screens.AddPesoScreen
import com.danielmarkpsn.controlecorporal.ui.screens.GraficoScreen
import com.danielmarkpsn.controlecorporal.ui.screens.HistoricoScreen
import com.danielmarkpsn.controlecorporal.ui.screens.HomeScreen
import com.danielmarkpsn.controlecorporal.ui.screens.MetaScreen
import com.danielmarkpsn.controlecorporal.ui.screens.PhotosScreen
import com.danielmarkpsn.controlecorporal.ui.screens.RelatorioScreen
import com.danielmarkpsn.controlecorporal.viewmodel.ControleViewModel

object Rotas {
    const val HOME = "home"
    const val ADD_PESO = "add_peso"
    const val ADD_MEDIDA = "add_medida"
    const val HISTORICO = "historico"
    const val META = "meta"
    const val GRAFICO = "grafico"
    const val FOTOS = "fotos"
    const val RELATORIO = "relatorio"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = androidx.compose.ui.platform.LocalContext.current.applicationContext as com.danielmarkpsn.controlecorporal.ControleApp

    val viewModel: ControleViewModel = viewModel(
        factory = ControleViewModel.Factory(context.container.repository)
    )

    NavHost(navController = navController, startDestination = Rotas.HOME) {

        composable(Rotas.HOME) {
            HomeScreen(
                viewModel = viewModel,
                onAdicionarPeso = { navController.navigate(Rotas.ADD_PESO) },
                onAdicionarMedida = { navController.navigate(Rotas.ADD_MEDIDA) },
                onVerHistorico = { navController.navigate(Rotas.HISTORICO) },
                onVerMeta = { navController.navigate(Rotas.META) },
                onVerGrafico = { navController.navigate(Rotas.GRAFICO) },
                onVerFotos = { navController.navigate(Rotas.FOTOS) },
                onVerRelatorio = { navController.navigate(Rotas.RELATORIO) }
            )
        }

        composable(Rotas.ADD_PESO) {
            AddPesoScreen(
                viewModel = viewModel,
                onVoltar = { navController.popBackStack() }
            )
        }

        composable(Rotas.ADD_MEDIDA) {
            AddMedidaScreen(
                viewModel = viewModel,
                onVoltar = { navController.popBackStack() }
            )
        }

        composable(Rotas.HISTORICO) {
            val medicoes by viewModel.medicoes.collectAsStateWithLifecycle()
            HistoricoScreen(
                medicoes = medicoes,
                onVoltar = { navController.popBackStack() }
            )
        }

        composable(Rotas.META) {
            MetaScreen(
                viewModel = viewModel,
                onVoltar = { navController.popBackStack() }
            )
        }

        composable(Rotas.GRAFICO) {
            val medicoes by viewModel.medicoes.collectAsStateWithLifecycle()
            GraficoScreen(
                medicoes = medicoes,
                onVoltar = { navController.popBackStack() }
            )
        }

        composable(Rotas.FOTOS) {
            PhotosScreen(onVoltar = { navController.popBackStack() })
        }

        composable(Rotas.RELATORIO) {
            val medicoes by viewModel.medicoes.collectAsStateWithLifecycle()
            val meta by viewModel.meta.collectAsStateWithLifecycle()
            RelatorioScreen(
                medicoes = medicoes,
                meta = meta,
                onVoltar = { navController.popBackStack() }
            )
        }
    }
}
