package com.danielmarkpsn.controlecorporal.data

object TreinoPresets {
    private fun ex(
        nome: String,
        musculo: String,
        series: Int,
        reps: Int,
        instrucoes: String,
        respiracao: String = "Inspire na fase de retorno e expire durante o esforço.",
        erros: String = "Evite acelerar o movimento, perder a postura ou usar carga excessiva."
    ) = TreinoExercicio(nome, musculo, series, reps, 0f, instrucoes, respiracao, erros)

    val todos: List<Treino> = listOf(
        Treino(
            "ABC — Peito + Tríceps",
            "Hipertrofia",
            listOf(
                ex("Supino reto","Peito",4,8,"Deite-se com os pés firmes, estabilize as escápulas e desça a carga com controle até o peito. Empurre mantendo a postura."),
                ex("Supino inclinado","Peito",3,10,"Use banco inclinado, mantenha as escápulas estáveis e conduza a carga com controle."),
                ex("Crucifixo","Peito",3,12,"Mantenha leve flexão nos cotovelos e abra os braços de forma controlada, aproximando-os na contração."),
                ex("Tríceps na polia","Tríceps",3,12,"Mantenha os cotovelos próximos ao corpo e estenda o antebraço sem balançar."),
                ex("Tríceps francês","Tríceps",3,10,"Controle a descida atrás da cabeça e estenda os cotovelos sem abrir excessivamente os braços.")
            ),
            listOf("Seg")
        ),
        Treino(
            "ABC — Costas + Bíceps",
            "Hipertrofia",
            listOf(
                ex("Puxada frontal","Costas",4,10,"Puxe a barra em direção à parte superior do peito, mantendo o tronco estável e controlando o retorno."),
                ex("Remada curvada","Costas",4,8,"Incline o tronco com coluna neutra e puxe conduzindo os cotovelos para trás."),
                ex("Remada baixa","Costas",3,12,"Mantenha o tronco estável, puxe o cabo em direção ao abdômen e controle a volta."),
                ex("Rosca direta","Bíceps",3,10,"Mantenha os cotovelos próximos ao corpo e flexione os braços sem usar impulso."),
                ex("Rosca martelo","Bíceps",3,12,"Mantenha as mãos em posição neutra e controle a subida e a descida.")
            ),
            listOf("Qua")
        ),
        Treino(
            "ABC — Pernas + Abdômen",
            "Hipertrofia",
            listOf(
                ex("Agachamento","Pernas",4,8,"Mantenha os pés firmes, coluna neutra e joelhos acompanhando a direção dos pés. Desça com controle."),
                ex("Leg press","Pernas",4,10,"Apoie completamente as costas, mantenha os pés firmes e controle a amplitude."),
                ex("Cadeira extensora","Quadríceps",3,12,"Estenda os joelhos de forma controlada e evite movimentos bruscos."),
                ex("Mesa flexora","Posterior de coxa",3,12,"Mantenha o quadril apoiado e flexione os joelhos controlando o retorno."),
                ex("Prancha","Abdômen",3,30,"Mantenha cabeça, tronco e quadril alinhados, contraindo abdômen e glúteos.","Respire continuamente e de forma controlada.","Não deixe o quadril cair ou subir excessivamente.")
            ),
            listOf("Sex")
        ),
        Treino(
            "ABCD — Peito + Ombros",
            "Hipertrofia",
            listOf(
                ex("Supino reto","Peito",4,8,"Estabilize as escápulas e desça a carga com controle até o peito."),
                ex("Supino inclinado","Peito",3,10,"Mantenha o banco inclinado e conduza a carga com controle."),
                ex("Desenvolvimento de ombros","Ombros",3,10,"Mantenha o tronco firme e empurre a carga verticalmente sem compensar com a lombar."),
                ex("Elevação lateral","Ombros",3,12,"Eleve os braços de forma controlada até uma amplitude confortável, sem impulso."),
                ex("Crucifixo","Peito",3,12,"Controle a abertura dos braços e concentre-se na contração do peitoral.")
            ),
            listOf("Seg")
        ),
        Treino(
            "ABCD — Costas + Bíceps",
            "Hipertrofia",
            listOf(
                ex("Puxada frontal","Costas",4,10,"Puxe conduzindo os cotovelos e controle completamente o retorno."),
                ex("Remada curvada","Costas",4,8,"Mantenha coluna neutra e conduza a puxada com os cotovelos."),
                ex("Remada baixa","Costas",3,12,"Mantenha o tronco estável e controle o cabo durante todo o movimento."),
                ex("Rosca direta","Bíceps",3,10,"Flexione os cotovelos sem balançar o corpo."),
                ex("Rosca martelo","Bíceps",3,12,"Use pegada neutra e movimento controlado.")
            ),
            listOf("Ter")
        ),
        Treino(
            "ABCD — Pernas completas",
            "Hipertrofia",
            listOf(
                ex("Agachamento","Quadríceps e glúteos",4,8,"Desça com controle mantendo coluna neutra e joelhos alinhados aos pés."),
                ex("Leg press","Quadríceps e glúteos",4,10,"Controle a descida e mantenha as costas apoiadas."),
                ex("Cadeira extensora","Quadríceps",3,12,"Estenda os joelhos com controle."),
                ex("Mesa flexora","Posterior de coxa",3,12,"Flexione os joelhos mantendo o quadril estável."),
                ex("Elevação pélvica","Glúteos",3,12,"Eleve o quadril contraindo os glúteos, sem hiperestender a lombar.")
            ),
            listOf("Qui")
        ),
        Treino(
            "ABCD — Ombros + Abdômen",
            "Hipertrofia",
            listOf(
                ex("Desenvolvimento de ombros","Ombros",4,8,"Empurre a carga verticalmente mantendo o tronco firme."),
                ex("Elevação lateral","Ombros",3,12,"Eleve os braços com controle e sem balanço."),
                ex("Prancha","Abdômen",3,30,"Mantenha o corpo alinhado e contraia abdômen e glúteos.","Respire continuamente.","Evite deixar o quadril cair."),
                ex("Abdominal curto","Abdômen",3,15,"Faça a flexão do tronco de forma controlada sem puxar a cabeça.","Expire na contração e inspire no retorno.","Evite impulso e tensão no pescoço.")
            ),
            listOf("Sex")
        ),
        Treino(
            "PPL — Push",
            "Hipertrofia",
            listOf(
                ex("Supino reto","Peito",4,8,"Desça com controle e empurre mantendo as escápulas estáveis."),
                ex("Supino inclinado","Peito",3,10,"Controle a descida e mantenha o banco estável."),
                ex("Desenvolvimento de ombros","Ombros",3,10,"Empurre verticalmente sem compensar com a lombar."),
                ex("Elevação lateral","Ombros",3,12,"Eleve os braços sem impulso."),
                ex("Tríceps na polia","Tríceps",3,12,"Estenda os cotovelos mantendo-os estáveis.")
            ),
            listOf("Seg")
        ),
        Treino(
            "PPL — Pull",
            "Hipertrofia",
            listOf(
                ex("Puxada frontal","Costas",4,10,"Puxe conduzindo os cotovelos e controle o retorno."),
                ex("Remada curvada","Costas",4,8,"Mantenha coluna neutra e puxe com os cotovelos."),
                ex("Remada baixa","Costas",3,12,"Controle a puxada e a volta."),
                ex("Rosca direta","Bíceps",3,10,"Flexione os cotovelos sem balançar."),
                ex("Rosca martelo","Bíceps",3,12,"Mantenha pegada neutra e controle o movimento.")
            ),
            listOf("Qua")
        ),
        Treino(
            "PPL — Legs",
            "Hipertrofia",
            listOf(
                ex("Agachamento","Pernas",4,8,"Mantenha coluna neutra e joelhos acompanhando os pés."),
                ex("Leg press","Pernas",4,10,"Controle a descida e mantenha as costas apoiadas."),
                ex("Cadeira extensora","Quadríceps",3,12,"Estenda os joelhos de forma controlada."),
                ex("Mesa flexora","Posterior de coxa",3,12,"Flexione os joelhos controlando o retorno."),
                ex("Elevação pélvica","Glúteos",3,12,"Eleve o quadril contraindo os glúteos.")
            ),
            listOf("Sex")
        ),
        Treino(
            "Full Body — Iniciante",
            "Resistência",
            listOf(
                ex("Agachamento","Pernas",3,10,"Use amplitude confortável, coluna neutra e movimento controlado."),
                ex("Supino reto","Peito",3,10,"Mantenha os pés firmes e controle a descida."),
                ex("Puxada frontal","Costas",3,10,"Conduza a puxada com os cotovelos e controle a volta."),
                ex("Desenvolvimento de ombros","Ombros",2,12,"Mantenha o tronco firme e movimento controlado."),
                ex("Rosca direta","Bíceps",2,12,"Evite balançar o corpo."),
                ex("Tríceps na polia","Tríceps",2,12,"Mantenha os cotovelos estáveis."),
                ex("Prancha","Abdômen",2,30,"Mantenha o corpo alinhado.","Respire continuamente.","Evite deixar o quadril cair.")
            ),
            listOf("Seg","Qua","Sex")
        )
    )
}
