// Mapeamento atualizado para as imagens profissionais
// Coloque no TreinosScreen.kt

val exercicioImage = when (exercicio.nome) {
    "Agachamento" -> R.drawable.img_agachamento
    "Leg Press" -> R.drawable.img_leg_press
    "Cadeira Extensora" -> R.drawable.img_cadeira_extensora
    "Mesa Flexora" -> R.drawable.img_mesa_flexora
    "Panturrilha em Pé" -> R.drawable.img_panturrilha
    "Supino Reto" -> R.drawable.img_supino_reto
    "Puxada Frontal" -> R.drawable.img_puxada
    "Remada Curvada" -> R.drawable.img_remada
    "Desenvolvimento" -> R.drawable.img_desenvolvimento
    "Elevação Lateral" -> R.drawable.img_elevacao_lateral
    "Rosca Direta" -> R.drawable.img_rosca
    "Tríceps na Polia" -> R.drawable.img_triceps
    else -> R.drawable.img_agachamento // fallback
}
