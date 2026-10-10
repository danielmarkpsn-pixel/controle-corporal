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
    "agachamento com kettlebell" to "agachamento_com_kettlebell",
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

private val availableGifAssets = setOf(
    "abdominal_cruzado",
    "abdominal_declinado",
    "abdominal_obliquo",
    "abdominal_tesoura",
    "afundo",
    "agachamento_bulgaro",
    "agachamento_com_kettlebell",
    "agachamento_com_salto",
    "agachamento_frontal",
    "agachamento_goblet",
    "agachamento_low_bar",
    "agachamento_sumo",
    "agachamento_unilateral_com_pe_elevado",
    "barra_fixa",
    "barra_fixa_graviton",
    "barra_fixa_supinada",
    "cadeira_abdutora",
    "cadeira_flexora",
    "crucifixo",
    "crucifixo_chao",
    "crucifixo_com_uma_mao",
    "crucifixo_declinado",
    "crucifixo_em_pe_na_polia_alta",
    "crucifixo_em_pe_na_polia_baixa",
    "crucifixo_em_pe_na_polia_media",
    "crucifixo_inclinado",
    "crucifixo_invertido",
    "crucifixo_invertido_com_halteres",
    "crucifixo_no_crossover",
    "crucifixo_no_crossover_de_baixo_para_cima",
    "crucifixo_no_crossover_de_cima_para_baixo",
    "desenvolvimento_arnold",
    "desenvolvimento_arnold_unilateral",
    "desenvolvimento_nuca",
    "desenvolvimento_sentado",
    "elevacao_de_panturrilhas_com_barra_livre",
    "elevacao_de_panturrilhas_em_pe_na_maquina",
    "elevacao_de_panturrilhas_no_leg_press",
    "elevacao_de_panturrilhas_sentado",
    "elevacao_de_panturrilhas_sentado_com_halteres",
    "elevacao_frontal",
    "elevacao_lateral_na_polia",
    "elevacao_lateral_na_polia_baixa",
    "elevacao_pelvica_com_barra",
    "encolhimento",
    "encolhimento_com_halteres",
    "execucao_correta_da_rosca_punho_invertida",
    "execucao_correta_do_rack_pull",
    "flexao_de_punho_invertida",
    "flexao_declinada",
    "levantamento_terra_com_barra_hexagonal",
    "levantamento_terra_com_halteres",
    "levantamento_terra_romeno",
    "levantamento_terra_sumo",
    "levantamento_terra_unilateral",
    "mesa_flexora_unilateral",
    "panturrilhas_em_pe",
    "passada_invertida",
    "passada_lateral",
    "pull_down",
    "pulley_costas",
    "pullover_com_halter",
    "puxada_neutra",
    "puxada_unilateral",
    "remada_baixa",
    "remada_cavalinho",
    "remada_curvada",
    "remada_curvada_com_halteres",
    "remada_em_pe",
    "remada_renegada",
    "remada_serrote",
    "rosca_concentrada",
    "rosca_direta_com_halteres",
    "rosca_direta_com_pegada_afastada",
    "rosca_direta_com_pegada_proxima",
    "rosca_direta_na_barra_w",
    "rosca_direta_na_polia",
    "rosca_direta_no_cross",
    "rosca_francesa_unilateral_na_polia",
    "rosca_inclinada",
    "rosca_invertida",
    "rosca_invertida_com_halteres",
    "rosca_martelo",
    "rosca_martelo_com_halteres_alternada",
    "rosca_martelo_com_peito_apoiado_no_banco_inclinado",
    "rosca_martelo_corda",
    "rosca_martelo_cruzada",
    "rosca_martelo_inclinada",
    "rosca_martelo_na_maquina",
    "rosca_martelo_scott",
    "rosca_scott",
    "rosca_spider",
    "rosca_zottman",
    "stiff",
    "supino_declinado",
    "supino_inclinado_com_halteres",
    "supino_na_maquina_articulada",
    "supino_no_crossover",
    "triceps_frances",
    "voador_invertido"
)

private fun normalizeExerciseName(value: String): String =
    java.text.Normalizer.normalize(value.trim().lowercase(), java.text.Normalizer.Form.NFD)
        .replace(Regex("\\p{Mn}+"), "")
        .replace(Regex("[^a-z0-9]+"), "_")
        .trim('_')

private fun gifAssetFor(name: String): String? {
    val normalized = normalizeExerciseName(name)
    if (normalized in availableGifAssets) return normalized

    return exerciseGifs
        .sortedByDescending { it.first.length }
        .firstOrNull { (key, asset) ->
            normalized.contains(normalizeExerciseName(key)) && asset in availableGifAssets
        }?.second
}

@Composable
fun ExerciseMedia(
    name: String,
    fallbackDrawable: Int,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    useGif: Boolean = true,
    usePng: Boolean = true
) {
    val context = LocalContext.current
    val gif = gifAssetFor(name)
    if (!useGif) {
        if (usePng) {
            Image(
                painter = painterResource(fallbackDrawable),
                contentDescription = "Ilustração de $name",
                modifier = modifier,
                contentScale = contentScale
            )
        } else {
            Box(modifier = modifier)
        }
    } else if (gif == null) {
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
fun ExerciseMediaFullScreen(name: String, fallbackDrawable: Int, onClose: () -> Unit, useGif: Boolean = true, usePng: Boolean = true) {
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
            if (!useGif) {
                if (usePng) {
                    Image(
                        painter = painterResource(fallbackDrawable),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Box(Modifier.fillMaxSize())
                }
            } else if (gif == null) {
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
