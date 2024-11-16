/**
 * Classe responsável por gerenciar o fluxo principal do jogo.
 * Controla as ações dos jogadores, a distribuição de cartas, e o progresso das rodadas.
 * Também gerencia as condições de vitória e o término do jogo.
 *
 * @param jogador1 O primeiro jogador.
 * @param jogador2 O segundo jogador.
 * @param baralho O baralho utilizado durante o jogo.
 */
class Jogo(
    val jogador1: Jogador,
    private val jogador2: Jogador,
    private val baralho: Baralho
) {
    private var vezJogador = true // Controle sobre qual jogador irá jogar: true - jogador 1, false - jogador 2
    private var numRodada = 1 // Rastrear a rodada atual

    /**
     * Imprime uma mensagem de boas-vindas para os jogadores
     */
    fun imprimirMensagemBoasVindas(){
        val mensagem = """
        
        ${GREEN}--- Boas-vindas ---${RESET}
        
        Prepare-se para entrar em um universo de fantasia e estratégia! 
        Este jogo de cartas colecionáveis, inspirado em clássicos como Magic: The Gathering e Yu-Gi-Oh, convida você a montar baralhos únicos e enfrentar desafios épicos.
        Posicione monstros no tabuleiro, equipe-os com itens poderosos, e use suas habilidades estratégicas para derrotar seu oponente.
        
        Boa sorte, duelista!
        """.trimIndent()
        println(mensagem)
    }

    fun imprimirRegrasGerais(){
        val mensagem = """
        
        ${GREEN}--- Regras Gerais ---${RESET}
        
        Após escolher uma opção no menu de jodadas, não é possível cancelar a ação.
        OBS: As únicas exceções são 'Descartar uma carta da mão' e 'Atacar oponente' quando não há monstros no campo, pois o jogador pode tentar posicionar um monstro e tentar atacar novamente.
        
        Não é possível escolher a opção 'Alterar o estado de um monstro (ataque/defesa)' após atacar o oponente. O estado só pode ser alterado na próxima rodada.
        
        O jogo só termina quando um jogador perder todos os pontos de vida ou quando acabar as cartas do baralho.
        """.trimIndent()
        println(mensagem)
    }

    /**
     * Imprime as regras do ataque no começo da partida
     */
    fun imprimirRegrasAtaque(){
        val regras = """
            
        ${GREEN}--- Regras de ataque ---${RESET}
        
        Oponente tem monstros no campo:
        - Ataque x Ataque
           .oponente tem menos ataque: monstro do oponente morre e a diferença de pontos de ataque entre eles é retirada da vida do oponente.
           .oponente tem mais ataque: monstro do jogador atual perde pontos de ataque com base na diferença de pontos de ataque entre eles.
           .oponente tem ataque igual: valor da defesa irá desempatar, o monstro com menos defesa perderá 10% dos seus pontos de ataque, mas se defesa também for igual, nada acontece.
            OBS: se o monstro do jogador chegar em 0 de ataque ao atacar um monstro de ataque alto, o monstro morre, mas o jogador atual não perde vida.
        
        - Ataque x Defesa
           .oponente tem menos defesa que o ataque do atacante: monstro do oponente morre e a diferença de pontos entre ataque do atacante e defesa do defensor é retirada da vida do oponente.
           .oponente tem mais defesa que o ataque do atacante: monstro do jogador atual perde pontos de ataque com base na diferença de pontos do ataque do atacante e defesa do defensor.
           .oponente tem defesa igual ao ataque do atacante: monstro atacante perde 10% de pontos de ataque, e monstro defensor perde 10% de pontos de defesa.
           OBS: se o monstro do jogador chegar em 0 de ataque ao atacar um monstro de defesa alta, o monstro morre, mas o jogador atual não perde vida.

        Oponente não tem monstros no campo:
        - Ataque direto: oponente perde em vida a quantidade de pontos ataque que o monstro do jogador atual tem.
        
        OBS: monstro morre se seu ataque ou defesa chegar a 0.
    """.trimIndent()
        println(regras)
    }

    /**
     *
     *
     */
    fun iniciarJogo(){
        // Imprimindo mensagens iniciais
        imprimirMensagemBoasVindas()
        imprimirRegrasGerais()
        imprimirRegrasAtaque()

        println("\n${GREEN}--- Começando a partida ---${RESET}")

        // Distribui 5 cartas iniciais para cada jogador
        distribuirCartas(jogador1)
        distribuirCartas(jogador2)

        // Loop do jogo onde os jogadores alternam turnos até que um deles perca toda a vida ou o baralho acabe
        while (jogador1.temVida() && jogador2.temVida() && baralho.temCartas()) {
            println("\n---------------------------------------------")
            println("\n${GREEN}--- $numRodada° rodada ---${RESET}")

            println("\nVida dos jogadores:\n${jogador1.nome}: ${jogador1.vida}\n${jogador2.nome}: ${jogador2.vida}")

            // Turno do Jogador 1
            turnoJogador(jogador1, CYAN)
            if (!jogador2.temVida()) break  // Verifica se o jogador 2 perdeu

            // Turno do Jogador 2
            turnoJogador(jogador2, MAGENTA)
            if (!jogador1.temVida()) break  // Verifica se o jogador 1 perdeu

            // Incrementa o número do turno após ambos os jogadores jogarem
            numRodada++
        }

        // Verifica o motivo pelo qual o jogo terminou (baralho vazio ou jogador sem vida)
        if (baralho.cartas.isEmpty()) {
            println("${YELLOW}O jogo terminou, porque o baralho não tem mais cartas!${RESET}")
        }
    }

    /**
     * Controlar o turno de um jogador, gerenciar a compra de cartas e a execução das jogadas.
     *
     * @param jogador O jogador que está no turno atual.
     * @param cor A cor utilizada para destacar o turno do jogador.
     */
    fun turnoJogador(jogador: Jogador, cor: String) {
        // Exibe o turno do jogador atual com a cor especificada
        println("\n${cor}Turno de ${jogador.nome}:${RESET}")

        // Define se é a vez do jogador 1 ou 2
        vezJogador = jogador == jogador1

        // Se o jogador tiver cartas na mão, ele compra uma carta, caso contrário distribui 5 novas cartas
        if (jogador.cartasNaMao.isNotEmpty()) {
            jogador.comprarCarta(baralho.cartas)
        } else {
            distribuirCartas(jogador)
        }

        // O jogador faz sua jogada
        jogador.jogar(this)
    }

    /**
     * Função para distribuir 5 cartas para um jogador.
     * Verifica se o jogador está sem cartas na mão e, se houver cartas disponíveis no baralho, realiza a distribuição.
     *
     * @param jogador O jogador que receberá as cartas.
     */
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

    /**
     * Imprime o menu dinâmico com as jogadas disponíveis para o jogador.
     * As opções são ajustadas com base nas jogadas já realizadas pelo jogador na rodada atual.
     * @param jogador O jogador atual.
     */
    fun imprimirMenuDinamico(jogador: Jogador) {
        println("\n${GREEN}--- Menu de jogadas ---${RESET}")
        println("\nEscolha uma ou mais ações, ou digite 'f' para finalizar a rodada:")

        // Exibe as opções disponíveis dependendo das jogadas já escolhidas pelo jogador
        if ("a" !in jogador.jogadasEscolhidas) println("a) Posicionar um novo monstro no tabuleiro")
        if ("b" !in jogador.jogadasEscolhidas) println("b) Equipar um monstro com uma carta de equipamento")
        if ("c" !in jogador.jogadasEscolhidas) println("c) Descartar uma carta da mão")
        if ("d" !in jogador.jogadasEscolhidas) println("d) Atacar oponente")
        if ("d" !in jogador.jogadasEscolhidas && "e" !in jogador.jogadasEscolhidas) println("e) Alterar o estado de um monstro (ataque/defesa)")
        println("f) Passar a vez")
    }

    /**
     * Processa as jogadas escolhidas pelo jogador.
     * Valida as opções e executa a ação correspondente, atualizando o estado do jogo.
     * @param jogador O jogador que está realizando a jogada.
     * @param op A opção escolhida pelo jogador.
     */
    fun processarJogadas(jogador: Jogador, op: String) {

        // Verificar se a jogada já foi realizada
        if (op in jogador.jogadasEscolhidas) {
            println("${YELLOW}Você já escolheu essa opção nesta rodada. Por favor, escolha outra.${RESET}")
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
                jogador.jogadasEscolhidas.add(op)
            }
            "c" -> {
                println("${jogador.nome} escolheu descartar uma carta da mão.")
                jogador.descartar()
                jogador.jogadasEscolhidas.add(op)
            }
            "d" -> {
                // Restrição de ataques na primeira rodada
                if (numRodada == 1) {
                    println("${RED}Ataques só são permitidos a partir da segunda rodada.${RESET}")
                    jogador.jogadasEscolhidas.add(op)
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
                    println("${RED}Não é possível alterar o estado do monstro após o ataque, tente novamente na próxima partida.${RESET}")
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

    /**
     * Verifica se ambos os jogadores ainda possuem pontos de vida para continuar o jogo.
     * @return `true` se ambos os jogadores têm vida, caso contrário `false`.
     */
    fun jogadoresTemVida(): Boolean {
        return (jogador1.temVida() && jogador2.temVida())
    }

    /**
     * Calcula o vencedor do jogo com base nos pontos de vida dos jogadores.
     * Exibe o resultado indicando o vencedor ou se o jogo terminou empatado.
     */
    fun calcularVencedor() {
        val vencedor: (Jogador, Jogador) -> String = { jogador1, jogador2 ->
            when {
                jogador1.vida > jogador2.vida -> "${GREEN}${jogador1.nome} venceu!${RESET}"
                jogador2.vida > jogador1.vida -> "${GREEN}{jogador2.nome} venceu!${RESET}"
                else -> "${YELLOW}O jogo terminou em empate!${RESET}"
            }
        }
        println("\nResultado do jogo: ${vencedor(jogador1, jogador2)}")
    }
}