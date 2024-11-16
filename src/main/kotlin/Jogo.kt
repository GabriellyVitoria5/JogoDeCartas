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

    // Controle sobre qual jogador irá jogar: true - jogador 1, false - jogador 2
    var vezJogador = true
    private var rodada = 1 // Variável para rastrear a rodada atual

    /**
     * Função para distribuir 5 cartas para um jogador.
     * Verifica se o jogador está sem cartas na mão e, se houver cartas disponíveis no baralho, realiza a distribuição.
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

    /**
     * Processa as jogadas escolhidas pelo jogador.
     * Valida as opções e executa a ação correspondente, atualizando o estado do jogo.
     * @param jogador O jogador que está realizando a jogada.
     * @param op A opção escolhida pelo jogador.
     */
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
                jogador.jogadasEscolhidas.add(op)
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

    /**
     * Atualiza o número da rodada ao final do turno de cada jogador.
     * Incrementa a variável `rodada`.
     */
    fun atualizarRodada() {
        rodada++
    }

    /**
     * Verifica se ambos os jogadores ainda possuem pontos de vida para continuar o jogo.
     * @return `true` se ambos os jogadores têm vida, caso contrário `false`.
     */
    fun jogadoresTemVida(): Boolean {
        return (jogador1.temVida() && jogador2.temVida()) // Retorna verdadeiro se ambos tiverem vida
    }

    /**
     * Calcula o vencedor do jogo com base nos pontos de vida dos jogadores.
     * Exibe o resultado indicando o vencedor ou se o jogo terminou empatado.
     */
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
