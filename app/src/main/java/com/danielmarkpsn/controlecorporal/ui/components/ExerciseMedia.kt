package com.danielmarkpsn.controlecorporal.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.decode.GifDecoder

private val exerciseGifs = listOf(
    "agachamento" to "agachamento_com_kettlebell",
    "mesa flexora" to "cadeira_flexora",
    "supino" to "supino_declinado",
    "crucifixo" to "crucifixo",
    "puxada" to "pulley_costas",
    "remada curvada" to "remada_curvada",
    "remada baixa" to "remada_baixa",
    "elevação lateral" to "elevacao_lateral_na_polia",
    "rosca direta" to "rosca_direta_na_barra_w",
    "rosca martelo" to "rosca_martelo_corda",
    "tríceps" to "triceps_frances",
    "abdominal" to "abdominal_cruzado",
    "barra fixa" to "barra_fixa",
    "afundo" to "afundo",
    "stiff" to "stiff",
    "levantamento terra" to "levantamento_terra_romeno",
    "encolhimento" to "encolhimento",
    "passada lateral" to "passada_lateral",
    "agachamento sumô" to "agachamento_sumo"
)

private fun gifAssetFor(name: String): String? {
    val normalized = name.trim().lowercase()
    return exerciseGifs.firstOrNull { (key, _) -> key in normalized }?.second
}

@Composable
fun ExerciseMedia(
    name: String,
    fallbackDrawable: Int,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit
) {
    val context = LocalContext.current
    val gif = gifAssetFor(name)
    if (gif == null) {
        Image(
            painter = painterResource(fallbackDrawable),
            contentDescription = "Ilustração de $name",
            modifier = modifier,
            contentScale = contentScale
        )
    } else {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data("file:///android_asset/exercise_gifs/$gif.gif")
                .decoderFactory(GifDecoder.Factory())
                .crossfade(false)
                .build(),
            contentDescription = "Demonstração animada de $name",
            modifier = modifier,
            contentScale = contentScale,
            error = painterResource(fallbackDrawable)
        )
    }
}

@Composable
fun ExerciseMediaFullScreen(name: String, fallbackDrawable: Int, onClose: () -> Unit) {
    val context = LocalContext.current
    val gif = gifAssetFor(name)
    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Box(Modifier.fillMaxSize()) {
            if (gif == null) {
                Image(
                    painter = painterResource(fallbackDrawable),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            } else {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data("file:///android_asset/exercise_gifs/$gif.gif")
                        .decoderFactory(GifDecoder.Factory())
                        .crossfade(false)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit,
                    error = painterResource(fallbackDrawable)
                )
            }
        }
    }
}
