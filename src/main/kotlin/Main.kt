/**
 * Códigos ANSI para adicionar cores no menu e em mensagens no terminal.
 * Utilizados para destacar informações de diferentes jogadores e estados do jogo.
 */
const val RESET = "\u001B[0m"   // Reseta a cor para o padrão
const val RED = "\u001B[31m"    // Cor vermelha para mensagens de erro
const val CYAN = "\u001B[36m"   // Cor ciano para jogador 1
const val MAGENTA = "\u001B[35m" // Cor magenta para jogador 2
const val GREEN = "\u001B[32m"  // Cor verde para destacar informações gerais
const val YELLOW = "\u001B[33m" // Cor amarela para informações de jogo e destaque

fun main() {
    // Cria uma instância de baralho e carrega as cartas a partir de um arquivo CSV
    val baralho = Baralho()

    // Carrega as cartas do arquivo CSV e verifica se o baralho foi carregado corretamente
    baralho.carregarCartasDoArquivo("src/main/kotlin/resources/cartas.csv")

    // Verifica se o baralho está vazio, exibe erro e termina a execução caso necessário
    if (baralho.cartas.isEmpty()) {
        println("\n${RED}Erro: O baralho está vazio. Verifique o arquivo CSV.${RESET}")
        return
    }

    // Embaralha o baralho para garantir que as cartas estarão em ordem aleatória
    baralho.embaralhar()

    // Cria dois jogadores para o jogo
    val jogador1 = Jogador("Jogador 1")
    val jogador2 = Jogador("Jogador 2")

    // Cria uma instância do jogo com os dois jogadores e o baralho carregado
    val jogo = Jogo(jogador1, jogador2, baralho)

    // Distribui 5 cartas iniciais para cada jogador
    println()
    jogo.distribuirCartas(jogador1)
    jogo.distribuirCartas(jogador2)

    var numTurno = 1 // Controla o número de turnos durante o jogo

    // Loop do jogo, onde os jogadores alternam turnos até que um deles perca toda a vida
    while (jogador1.temVida() && jogador2.temVida() && baralho.temCartas()) {
        // Exibe o estado do jogo antes de iniciar o turno
        exibirEstadoJogo(jogador1, jogador2, numTurno)

        // Turno do Jogador 1
        turnoJogador(jogo, jogador1, baralho, CYAN)
        if (!jogador2.temVida()) break  // Verifica se o jogador 2 perdeu

        // Turno do Jogador 2
        turnoJogador(jogo, jogador2, baralho, MAGENTA)
        if (!jogador1.temVida()) break  // Verifica se o jogador 1 perdeu

        // Incrementa o número do turno após ambos os jogadores jogarem
        numTurno++

        // Atualiza a rodada do jogo
        jogo.atualizarRodada()
    }

    // Verifica o motivo pelo qual o jogo terminou (baralho vazio ou jogador sem vida)
    if (baralho.cartas.isEmpty()) {
        println("${YELLOW}O jogo terminou, porque o baralho não tem mais cartas.${RESET}")
    }

    // Exibe o resultado final do jogo (vencedor ou empate)
    jogo.calcularVencedor()
}

/**
 * Função para exibir o estado atual do jogo, incluindo a vida e cartas dos jogadores.
 *
 * @param jogador1 O primeiro jogador.
 * @param jogador2 O segundo jogador.
 * @param numTurno Número da rodada atual.
 */
fun exibirEstadoJogo(jogador1: Jogador, jogador2: Jogador, numTurno: Int) {
    // Exibe o título e a rodada do jogo
    println("\n${MAGENTA}---------------------------------------------${RESET}")
    println("\n${GREEN}Estado do jogo - $numTurno° partida:${RESET}\n")

    // Exibe informações sobre o jogador 1
    println("${CYAN}${jogador1.nome}${RESET} - Vida: ${jogador1.vida}")
    jogador1.mostrarMao()  // Exibe as cartas na mão do jogador 1
    jogador1.mostrarMonstroTabuleiro()  // Exibe os monstros no campo do jogador 1

    // Exibe informações sobre o jogador 2
    println("\n${MAGENTA}${jogador2.nome}${RESET} - Vida: ${jogador2.vida}")
    jogador2.mostrarMao()  // Exibe as cartas na mão do jogador 2
    jogador2.mostrarMonstroTabuleiro()  // Exibe os monstros no campo do jogador 2

    println("\n${MAGENTA}---------------------------------------------${RESET}")
}

/**
 * Função para controlar o turno de um jogador, gerenciar a compra de cartas e a execução das jogadas.
 *
 * @param jogo A instância atual do jogo.
 * @param jogador O jogador que está fazendo o turno.
 * @param baralho O baralho de onde o jogador irá comprar as cartas.
 * @param cor A cor utilizada para destacar o turno do jogador.
 */
fun turnoJogador(jogo: Jogo, jogador: Jogador, baralho: Baralho, cor: String) {
    // Exibe o turno do jogador atual com a cor especificada
    println("\n$cor Turno de ${jogador.nome}:${RESET}")

    // Define se é a vez do jogador 1 ou 2
    jogo.vezJogador = jogador == jogo.jogador1

    // Se o jogador tiver cartas na mão, ele compra uma carta
    if (jogador.cartasNaMao.isNotEmpty()) {
        jogador.comprarCarta(baralho.cartas)
    } else {
        // Caso contrário, distribui 5 novas cartas
        jogo.distribuirCartas(jogador)
    }

    // O jogador realiza sua jogada
    jogador.jogar(jogo)
}
