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
    "agachamento sumô" to "agachamento_sumo",
    "agachamento com salto" to "agachamento_com_salto",
    "agachamento" to "agachamento_com_salto",
    "mesa flexora" to "cadeira_flexora",
    "crucifixo" to "crucifixo",
    "puxada" to "pulley_costas",
    "remada curvada" to "remada_curvada",
    "remada baixa" to "remada_baixa",
    "elevação lateral" to "elevacao_lateral_na_polia",
    "rosca direta" to "rosca_direta_na_barra_w",
    "rosca martelo" to "rosca_martelo_corda",
    "abdominal" to "abdominal_cruzado",
    "barra fixa" to "barra_fixa",
    "afundo" to "afundo",
    "stiff" to "stiff",
    "levantamento terra" to "levantamento_terra_romeno",
    "encolhimento" to "encolhimento",
    "passada lateral" to "passada_lateral",
    "cadeira abdutora" to "cadeira_abdutora",
    "crucifixo declinado" to "crucifixo_declinado",
    "crucifixo inclinado" to "crucifixo_inclinado",
    "crucifixo chão" to "crucifixo_chao",
    "crucifixo em pé na polia alta" to "crucifixo_em_pe_na_polia_alta",
    "crucifixo em pé na polia baixa" to "crucifixo_em_pe_na_polia_baixa",
    "crucifixo em pé na polia média" to "crucifixo_em_pe_na_polia_media",
    "crucifixo com uma mão" to "crucifixo_com_uma_mao",
    "elevação pélvica" to "elevacao_pelvica_com_barra",
    "levantamento terra hexagonal" to "levantamento_terra_com_barra_hexagonal",
    "levantamento terra com halteres" to "levantamento_terra_com_halteres",
    "levantamento terra sumô" to "levantamento_terra_sumo",
    "levantamento terra unilateral" to "levantamento_terra_unilateral",
    "pulley costas" to "pulley_costas",
    "remada cavalinho" to "remada_cavalinho",
    "remada serrote" to "remada_serrote",
    "rosca direta na polia" to "rosca_direta_na_polia",
    "rosca martelo cruzada" to "rosca_martelo_cruzada",
    "rosca martelo scott" to "rosca_martelo_scott",
    "rosca martelo máquina" to "rosca_martelo_na_maquina",
    "rosca martelo inclinada" to "rosca_martelo_inclinada",
    "rosca martelo peito apoiado" to "rosca_martelo_com_peito_apoiado_no_banco_inclinado",
    "tríceps francês" to "triceps_frances",
    "supino declinado" to "supino_declinado",
    "voador invertido" to "voador_invertido",
    "abdominal declinado" to "abdominal_declinado",
    "afundo" to "afundo",
    "rack pull" to "execucao_correta_do_rack_pull",
    "rosca punho invertida" to "execucao_correta_da_rosca_punho_invertida",
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
