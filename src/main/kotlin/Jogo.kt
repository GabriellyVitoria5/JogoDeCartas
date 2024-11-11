class Jogo(
    private val jogador1: Jogador,
    private val jogador2: Jogador,
    private val baralho: Baralho
) {

    // Distribui 5 cartas iniciais para cada jogador no começo da partida
    fun distribuirCartasIniciais() {
        if (baralho.cartas.size < 10) {
            println("\nErro: O baralho não tem cartas suficientes para distribuir.")
            return
        }

        for (i in 1..5) {
            jogador1.comprarCarta(baralho.cartas)
            jogador2.comprarCarta(baralho.cartas)
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
                jogador.jogadasEscolhidas.add("a")
            }
            "b" -> {
                println("${jogador.nome} escolheu equipar um monstro com uma carta de equipamento.")
                //equiparMonstro()
                jogador.jogadasEscolhidas.add("b")
            }
            "c" -> {
                println("${jogador.nome} escolheu descartar uma carta da mão.")
                jogador.descartar()
                jogador.jogadasEscolhidas.add("c")
            }
            "d" -> {
                println("${jogador.nome} escolheu realizar um ataque contra o oponente.")
                //atacarOponente()
                jogador.jogadasEscolhidas.add("d")
            }
            "e" -> {
                println("${jogador.nome} escolheu alterar o estado de um monstro (ataque/defesa).")
                //atacarOponente()
                jogador.jogadasEscolhidas.add("e")
            }
            "f" -> {
                println("${jogador.nome} passou a vez.")
            }
            else -> println("Opção inválida! Por favor, escolha uma opção válida.")
        }
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

    // Executa um turno no jogo, onde o jogador 1 ataca o jogador 2
    fun turno() {
        if (jogador1.monstrosNoCampo.isNotEmpty() && jogador2.monstrosNoCampo.isNotEmpty()) {
            val monstroAtacante = jogador1.monstrosNoCampo[0]
            val monstroDefensor = jogador2.monstrosNoCampo[0]
            jogador1.atacar(monstroAtacante, jogador2, monstroDefensor) // Jogador 1 ataca Jogador 2
        } else {
            println("\nUm dos jogadores não possui monstros no campo para atacar.")
        }
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
