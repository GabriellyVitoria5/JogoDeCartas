class Jogo(
    private val jogador1: Jogador,
    private val jogador2: Jogador,
    private val baralho: Baralho
) {

    var vezJogador = true // controle sobre qual jogador irá jogar: true - jogador 1, false - jogador 2

    // Distribui 5 cartas para um jogador no começo da partida ou quando jogador não tiver mais cartas na mão
    fun distribuirCartas(jogador: Jogador) {
        val maxCartasPorJogador = 5

        if(jogador.cartasNaMao.isEmpty()){

            if (baralho.cartas.isEmpty()) {
                println("O baralho está sem cartas para comprar.")
                return
            }

            println("Distribuindo 5 cartas para ${jogador.nome}...")
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

    // Imprimir menu dinâmico com base nas jogadas escolhidas pelo jogador
    fun imprimirMenuDinamico(jogador: Jogador) {
        println("\n~ Menu de jogadas ~")
        println("\nEscolha uma ou mais ações, ou digite 'f' para finalizar a rodada:")
        if ("a" !in jogador.jogadasEscolhidas) println("a) Posicionar um novo monstro no tabuleiro")
        if ("b" !in jogador.jogadasEscolhidas) println("b) Equipar um monstro com uma carta de equipamento")
        if ("c" !in jogador.jogadasEscolhidas) println("c) Descartar uma carta da mão")
        if ("d" !in jogador.jogadasEscolhidas) println("d) Atacar oponente")
        if ("d" !in jogador.jogadasEscolhidas && "e" !in jogador.jogadasEscolhidas) println("e) Alterar o estado de um monstro (ataque/defesa)")
        println("f) Passar a vez")
    }

    // Processar a jogada escolhida pelo jogador e chamar métodos para cada escolha
    // TODO terminar de implementar os métodos
    fun processarJogadas(jogador: Jogador, op: String) {
        when (op.lowercase()) {
            "a" -> {
                println("${jogador.nome} escolheu posicionar um novo monstro no tabuleiro.")
                jogador.posicionarMonstro()
            }
            "b" -> {
                println("${jogador.nome} escolheu equipar um monstro com uma carta de equipamento.")
                jogador.equiparMonstro()
            }
            "c" -> {
                println("${jogador.nome} escolheu descartar uma carta da mão.")
                jogador.descartar()
            }
            "d" -> {
                println("${jogador.nome} escolheu realizar um ataque contra o oponente.")
                //jogador atual precisa passar como parâmetro o oponente que deseja atacar
                if (vezJogador){
                    jogador.atacarOponente(jogador2) // jogador 1 ataca jogador 2
                }
                else{
                    jogador.atacarOponente(jogador1) // jogador 2 ataca jogador 1
                }

            }
            "e" -> {
                println("${jogador.nome} escolheu alterar o estado de um monstro (ataque/defesa).")
                jogador.alterarEstadoMonstro()
            }
            "f" -> {
                println("${jogador.nome} passou a vez.")
            }
            else -> println("Opção inválida! Por favor, escolha uma opção válida.")
        }
    }

    // Verificar se os dois jodares tem vida para continuar a jogar
    fun jogadoresTemVida(): Boolean{
        return (jogador1.temVida() && jogador2.temVida())
    }

    // Controlar o fluxo do jogo
    fun iniciarJogo() {
        while (baralho.temCartas() && (jogador1.temVida() && jogador2.temVida())) {
            println("\n----------------------------------------------------------------------")
            println("\nRodada de ${jogador1.nome}:")
            jogador1.jogar(this)
            if (!jogador2.temVida()) break

            println("\n----------------------------------------------------------------------")
            println("\nRodada de ${jogador2.nome}:")
            jogador2.jogar(this)

            jogador2.vida = 0 // TODO só um controle para acabar com o loop, apagar depois
        }
    }

    // Mostra as cartas na mão de um jogador
    fun mostrarMao(jogador: Jogador) {
        println("\n${jogador.nome} tem as seguintes cartas na mão:")
        println("\nTipo | Nome | Descrição | Ataque | Defesa | Estado do monstro")
        for (carta in jogador.cartasNaMao) {
            println(carta)
        }
        println("---------------------------------------------")
    }

    // Calcular o vencedor do jogo pela quantidade de vida dos jogadores
    fun calcularVencedor() {
        val vencedor: (Jogador, Jogador) -> String = { jogador1, jogador2 ->
            when {
                jogador1.vida > jogador2.vida -> "${jogador1.nome} venceu!"
                jogador2.vida > jogador1.vida -> "${jogador2.nome} venceu!"
                else -> "O jogo terminou em empate!"
            }
        }
        println("\nResultado do jogo: ${vencedor(jogador1, jogador2)}")
    }
}
