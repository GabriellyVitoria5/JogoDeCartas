class Jogo(
    val jogador1: Jogador,
    private val jogador2: Jogador,
    private val baralho: Baralho
) {

    // Controle sobre qual jogador irá jogar: true - jogador 1, false - jogador 2
    var vezJogador = true
    var rodada = 1 // Variável para rastrear a rodada atual

    // Função para distribuir 5 cartas para um jogador no início ou quando ele ficar sem cartas
    fun distribuirCartas(jogador: Jogador) {
        val maxCartasPorJogador = 5

        // Se o jogador não tiver cartas na mão, distribui novas
        if (jogador.cartasNaMao.isEmpty()) {

            // Verifica se o baralho tem cartas suficientes
            if (baralho.cartas.isEmpty()) {
                println("O baralho está sem cartas para comprar.")
                return
            }

            println("\nDistribuindo 5 cartas para ${jogador.nome}...")

            // Distribui as cartas ao jogador
            for (i in 1..maxCartasPorJogador) {
                if (baralho.cartas.isNotEmpty()) {
                    jogador.comprarCarta(baralho.cartas)
                } else {
                    println("O baralho ficou sem cartas durante a distribuição para ${jogador.nome}.")
                    break
                }
            }
        }
    }

    // Imprimir o menu dinâmico com as jogadas disponíveis para o jogador
    fun imprimirMenuDinamico(jogador: Jogador) {
        println("\n~ Menu de jogadas ~")
        println("\nEscolha uma ou mais ações, ou digite 'f' para finalizar a rodada:")

        // Exibe as opções disponíveis dependendo das jogadas já escolhidas pelo jogador
        if ("a" !in jogador.jogadasEscolhidas) println("a) Posicionar um novo monstro no tabuleiro")
        if ("b" !in jogador.jogadasEscolhidas) println("b) Equipar um monstro com uma carta de equipamento")
        if ("c" !in jogador.jogadasEscolhidas) println("c) Descartar uma carta da mão")
        if ("d" !in jogador.jogadasEscolhidas) println("d) Atacar oponente")
        if ("d" !in jogador.jogadasEscolhidas && "e" !in jogador.jogadasEscolhidas) println("e) Alterar o estado de um monstro (ataque/defesa)")
        println("f) Passar a vez")
    }

    // Cada opção chama uma função correspondente da classe Jogador com base na jogada escolhida
    fun processarJogadas(jogador: Jogador, op: String) {

        // Verificar se a jogada já foi realizada
        if (op in jogador.jogadasEscolhidas) {
            println("Você já escolheu essa opção nesta rodada. Por favor, escolha outra.")
            return
        }

        when (op.lowercase()) {
            "a" -> {
                println("${jogador.nome} escolheu posicionar um novo monstro no tabuleiro.")
                jogador.posicionarMonstro()
                jogador.jogadasEscolhidas.add(op)
            }
            "b" -> {
                println("${jogador.nome} escolheu equipar um monstro com uma carta de equipamento.")
                jogador.equiparMonstro()
            }
            "c" -> {
                println("${jogador.nome} escolheu descartar uma carta da mão.")
                jogador.descartar()
                jogador.jogadasEscolhidas.add(op)
            }
            "d" -> {
                // Restrição de ataques na primeira rodada
                if (rodada == 1) {
                    println("Ataques só são permitidos a partir da segunda rodada.")
                    return
                }

                println("${jogador.nome} escolheu realizar um ataque contra o oponente.")
                if (vezJogador) {
                    jogador.atacarOponente(jogador2) // Jogador 1 ataca jogador 2
                } else {
                    jogador.atacarOponente(jogador1) // Jogador 2 ataca jogador 1
                }
                jogador.jogadasEscolhidas.add(op)
            }
            "e" -> {
                // Validar se monstro já atacou nesta rodada
                if ("d" in jogador.jogadasEscolhidas) {
                    println("Não é possível alterar o estado do monstro após o ataque, tente novamente na próxima partida.")
                    return
                }
                println("${jogador.nome} escolheu alterar o estado de um monstro (ataque/defesa).")
                jogador.alterarEstadoMonstro()
                jogador.jogadasEscolhidas.add(op)
            }
            "f" -> {
                println("${jogador.nome} passou a vez.")
            }
            else -> println("Opção inválida! Por favor, escolha uma opção válida.")
        }
    }

    // Atualizar o número da rodada ao final do turno de cada jogador
    fun atualizarRodada() {
        rodada++
    }

    // Função para verificar se os dois jogadores têm vida para continuar o jogo
    fun jogadoresTemVida(): Boolean {
        return (jogador1.temVida() && jogador2.temVida()) // Retorna verdadeiro se ambos tiverem vida
    }

    // Função principal para iniciar o jogo
    fun iniciarJogo() {
        // O jogo continua enquanto houver cartas no baralho e ambos os jogadores tiverem vida
        while (baralho.temCartas() && (jogador1.temVida() && jogador2.temVida())) {
            println("\n----------------------------------------------------------------------")
            println("\nRodada de ${jogador1.nome}:")
            jogador1.jogar(this) // Jogador 1 faz sua jogada
            if (!jogador2.temVida()) break // Se o jogador 2 perder a vida, o jogo termina

            println("\n----------------------------------------------------------------------")
            println("\nRodada de ${jogador2.nome}:")
            jogador2.jogar(this) // Jogador 2 faz sua jogada

            atualizarRodada() // Atualiza a rodada após o turno de ambos os jogadores
        }
    }

    // Função para calcular o vencedor do jogo baseado na vida dos jogadores
    fun calcularVencedor() {
        val vencedor: (Jogador, Jogador) -> String = { jogador1, jogador2 ->
            when {
                jogador1.vida > jogador2.vida -> "${jogador1.nome} venceu!" // Jogador 1 vence
                jogador2.vida > jogador1.vida -> "${jogador2.nome} venceu!" // Jogador 2 vence
                else -> "O jogo terminou em empate!" // Empate
            }
        }
        println("\nResultado do jogo: ${vencedor(jogador1, jogador2)}") // Exibe o vencedor ou empate
    }
}
