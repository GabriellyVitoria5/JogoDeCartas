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
    private val jogador1: Jogador,
    private val jogador2: Jogador,
    private val baralho: Baralho
) {
    private var vezJogador = true // Controle de qual jogador irá jogar: true - jogador 1, false - jogador 2
    private var numRodada = 1 // Contar rodadas

    /**
     * Imprime uma mensagem de boas-vindas para os jogadores
     */
    private fun imprimirMensagemBoasVindas(){
        val mensagem = """
        
        ${GREEN}--- Boas-vindas ---${RESET}
        
        Prepare-se para entrar em um universo de fantasia e estratégia! 
        Este jogo de cartas colecionáveis, inspirado em clássicos como Magic: The Gathering e Yu-Gi-Oh, convida você a montar baralhos únicos e enfrentar desafios épicos.
        Posicione monstros no tabuleiro, equipe-os com itens poderosos, e use suas habilidades estratégicas para derrotar seu oponente.
        
        Boa sorte, duelista!
        """.trimIndent()
        println(mensagem)
    }

    /**
     * Imprime uma mensagem com as regras do jogo
     */
    private fun imprimirRegrasGerais(){
        val mensagem = """
        
        ${GREEN}--- Regras Gerais ---${RESET}
        
        Após escolher uma opção no menu de jogadas, não é possível cancelar a ação.
        OBS: As únicas exceções são 'Descartar uma carta da mão' e 'Atacar oponente' quando não há monstros no campo, pois o jogador pode tentar posicionar um monstro e tentar atacar novamente.
        
        Não é possível escolher a opção 'Alterar o estado de um monstro (ataque/defesa)' após atacar o oponente. O estado só pode ser alterado na próxima rodada.
        
        O jogo só termina quando um jogador perder todos os pontos de vida ou quando acabar as cartas do baralho.
        """.trimIndent()
        println(mensagem)
    }

    /**
     * Imprime as regras do ataque no começo da partida
     */
    private fun imprimirRegrasAtaque(){
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
     * Ordem do que acontecerá no jogo está aqui
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

            numRodada++ // Incrementa o número do turno após ambos os jogadores jogarem
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
    private fun turnoJogador(jogador: Jogador, cor: String) {
        // Exibe o turno do jogador atual com a cor especificada
        println("\n${cor}Turno de ${jogador.nome}:${RESET}")
        println("${cor}Vida ${jogador.vida}${RESET}")

        // Define se é a vez do jogador 1 ou 2
        vezJogador = jogador == jogador1

        // Se o jogador tiver cartas na mão, ele compra uma carta, caso contrário distribui 5 novas cartas
        if (jogador.cartasNaMao.isNotEmpty()) {
            jogador.comprarCarta(baralho.cartas)
        } else {
            distribuirCartas(jogador)
        }

        // O jogador faz a sua jogada
        jogador.jogar(this)
    }

    /**
     * Função para distribuir 5 cartas para um jogador.
     * Verifica se o jogador está sem cartas na mão e, se houver cartas disponíveis no baralho, realiza a distribuição.
     *
     * @param jogador O jogador que receberá as cartas.
     */
    private fun distribuirCartas(jogador: Jogador) {
        val maxCartasPorJogador = 5

        // Se o jogador não tiver cartas na mão, distribui novas
        if (jogador.cartasNaMao.isEmpty()) {

            if (baralho.cartas.isEmpty()) {
                println("${RED}O baralho está sem cartas para comprar.${RESET}")
                return
            }

            // Distribui as cartas ao jogador
            println("\nDistribuindo 5 cartas para ${jogador.nome}...")
            for (i in 1..maxCartasPorJogador) {
                if (baralho.cartas.isNotEmpty()) {
                    jogador.comprarCarta(baralho.cartas)
                } else {
                    println("$YELLOW O baralho ficou sem cartas durante a distribuição para ${jogador.nome}${RESET}.")
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
                    jogador.atacarOponente(jogador2, this) // Jogador 1 ataca jogador 2
                } else {
                    jogador.atacarOponente(jogador1, this) // Jogador 2 ataca jogador 1
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
     * Processa o combate entre dois monstros ou o ataque direto, calculando o dano causado e atualizando os atributos.
     * A função considera o estado do alvo (Ataque ou Defesa) e aplica as regras de combate.
     * @param jogadorAtual Jogador que está realizando a jogada.
     * @param monstroAtacante Monstro do jogador que realiza a jogada
     * @param oponente Jogador que sefrerá o ataque
     */
    fun processarAtaque(jogadorAtual: Jogador, monstroAtacante: CartaMonstro, oponente: Jogador) {
        if (oponente.cartasNaMao.isNotEmpty()){
            // Escolhe um monstro do oponente para atacar
            var escolha = -1
            while (escolha == -1) {
                escolha = oponente.escolherCarta(
                    oponente.monstrosNoCampo.map { "${it.nome.padEnd(12)} - A:${it.ataque.toString().padEnd(4)} D:${it.defesa.toString().padEnd(4)} - ${it.estado}" },
                    "Escolha um monstro do oponente para atacar: "
                )
                if (escolha == -1) println("${RED}Escolha inválida! Por favor, selecione um número de opção válida.${RESET}")
            }
            val monstroOponente = oponente.monstrosNoCampo[escolha]

            // Implementando as regras do ataque
            print("\n${monstroAtacante.nome} ataca ${monstroOponente.nome}!!")

            // Ataque X Ataque -> diferença = ataque - ataque
            // Ataque X Defesa -> diferença = ataque - defesa

            // Calcula a diferença de ataque entre o monstro atacante e o monstro do oponente (considerando o estado do alvo)
            val diferenca = monstroAtacante.ataque - (if (monstroOponente.estado == "Defesa") monstroOponente.defesa else monstroOponente.ataque)

            // Verificando as regras do ataque
            if (monstroOponente.estado == "Ataque") {
                // Ataque X Ataque
                when {
                    diferenca > 0 -> { // Atacante vence, derrota o monstro e causa dano ao oponente
                        oponente.monstrosNoCampo.remove(monstroOponente)
                        oponente.vida -= diferenca
                        println("\n${GREEN}O ataque foi um sucesso! ${monstroAtacante.nome} superar o ataque do monstro e destrói ${monstroOponente.nome}! ${oponente.nome} perde $diferenca pontos de vida durante o ataque.${RESET}")
                        println("${monstroOponente.nome} foi destruído e removido do campo de ${oponente.nome}.")
                    }
                    diferenca < 0 -> { // O atacante perde o combate e seu ataque é reduzido
                        monstroAtacante.ataque += diferenca // Subtrai o valor absoluto da diferença
                        println("\n${YELLOW}O ataque falhou, pois ${monstroAtacante.nome} não é forte o suficiente para superar o ataque de ${monstroOponente.nome}. Seu monstro perde ${-diferenca} pontos de ataque ao receber um golpe de ${monstroOponente.nome}!${RESET}")
                    }
                    else -> { // Empate no ataque: defesa irá desempatar
                        val ataquePerdidoCalculado = (monstroOponente.ataque * 0.1).toInt()
                        when{
                            monstroAtacante.defesa > monstroOponente.ataque ->{ // Atacante perde 10% de ataque
                                monstroOponente.ataque -= ataquePerdidoCalculado
                                println("\n${GREEN}Os dois monstros são igualmente fortes! Mas seu monstro ${monstroAtacante.nome} tinha mais defesa para resistir a luta e consegue tirar $ataquePerdidoCalculado pontos de ataque do monstro do oponente${RESET}")
                            }
                            monstroAtacante.defesa < monstroOponente.ataque ->{ // Monstro do oponente perde 10% ataque
                                monstroAtacante.ataque -= ataquePerdidoCalculado
                                println("\n${YELLOW}Os dois monstros são igualmente fortes! Mas ${monstroOponente.nome} tinha mais defesa para resistir ao ataque e conseguiu tirar $ataquePerdidoCalculado pontos de ataque do seu monstro!${RESET}")
                            }
                            else -> { // Empate na defesa também: nada acontece
                                println("${YELLOW}\nOs dois monstros são igualmente fortes! Suas defesas também são as mesmas! O ataque não surtiu efeito em nenhum dos lados.${RESET}")
                            }
                        }
                    }
                }
            }
            else{
                // Ataque X Defesa
                when {
                    diferenca > 0 -> { // Atacante vence, derrota o monstro e causa dano ao oponente
                        oponente.monstrosNoCampo.remove(monstroOponente)
                        oponente.vida -= diferenca
                        println("\n${GREEN}O ataque foi um sucesso! ${monstroAtacante.nome} destrói ${monstroOponente.nome}! ${oponente.nome} perde $diferenca pontos de vida durante o ataque.${RESET}")
                        println("${monstroOponente.nome} foi destruído e removido do campo de ${oponente.nome}.")
                    }
                    diferenca < 0 -> { // O atacante perde o combate e seu ataque é reduzido
                        monstroAtacante.ataque += diferenca // Subtrai o valor absoluto da diferença
                        println("\n${YELLOW}O ataque falhou, pois ${monstroAtacante.nome} não é forte o suficiente para superar as defesas de ${monstroOponente.nome}. Seu monstro perde ${-diferenca} pontos de ataque durante a luta!${RESET}")
                    }
                    else -> { // Empate: Monstro do jogador atual perde 10% pontos de ataque, e monstro do oponente perde 10% pontos de defesa
                        val ataquePerdidoCalculado = (monstroAtacante.ataque * 0.1).toInt()
                        val defesaPerdidaCalculada = (monstroOponente.defesa * 0.1).toInt()
                        monstroAtacante.ataque -= ataquePerdidoCalculado
                        monstroOponente.defesa -= defesaPerdidaCalculada
                        println("\n${YELLOW}O ataque do seu monstro ${monstroAtacante.nome} é igual à defesa de ${monstroOponente.nome}! Durante a luta, seu monstro perde $ataquePerdidoCalculado pontos de ataque, e ${monstroOponente.nome} perde $defesaPerdidaCalculada pontos de defesa!${RESET}")
                    }
                }
            }

            // Verifica se o monstro atacante foi derrotado e o remove
            if (monstroAtacante.ataque <= 0 || monstroAtacante.defesa <= 0) {
                jogadorAtual.monstrosNoCampo.remove(monstroAtacante)
                println("\n${YELLOW}Seu monstro sofreu graves ferimentos durante a luta e não conseguiu sobreviver. ${monstroAtacante.nome} será removido do campo.${RESET}")
            }

            // Verifica se o monstro do oponente foi derrotado e o remove
            if (monstroOponente.ataque <= 0 || monstroOponente.defesa <= 0) {
                oponente.monstrosNoCampo.remove(monstroOponente)
                println("\n${YELLOW}Monstro sofreu graves ferimentos durante a luta e não conseguiu sobreviver. ${monstroOponente.nome} será removido do campo.${RESET}")
            }
        }
        else{
            // Oponente sofrerá ataque direto
            oponente.vida -= monstroAtacante.ataque
            println("${GREEN}${monstroAtacante.nome} realiza um ataque direto! ${oponente.nome} perde ${monstroAtacante.ataque} pontos de vida.${RESET}\n")
        }
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