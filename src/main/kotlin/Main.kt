// Códigos ANSI para adicionar cores no menu
const val RESET = "\u001B[0m"
const val RED = "\u001B[31m"    // Cor para jogador 1
const val BLUE = "\u001B[34m"   // Cor para jogador 2
const val GREEN = "\u001B[32m"  // Cor para destacar informações gerais

fun main() {
    // Cria uma instância de baralho e carrega as cartas a partir de um arquivo CSV
    val baralho = Baralho()

    // Carrega as cartas do arquivo CSV e verifica se o baralho foi carregado corretamente
    baralho.carregarCartasDoArquivo("src/main/kotlin/resources/cartas.csv")

    // Verifica se o baralho está vazio, exibe erro e termina a execução caso necessário
    if (baralho.cartas.isEmpty()) {
        println("\nErro: O baralho está vazio. Verifique o arquivo CSV.")
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
        turnoJogador(jogo, jogador1, baralho, RED)
        if (!jogador2.temVida()) break  // Verifica se o jogador 2 perdeu

        // Turno do Jogador 2
        turnoJogador(jogo, jogador2, baralho, BLUE)
        if (!jogador1.temVida()) break  // Verifica se o jogador 1 perdeu

        // Incrementa o número do turno após ambos os jogadores jogarem
        numTurno++

        // Atualiza a rodada do jogo
        jogo.atualizarRodada()

        // Limpa a mão do jogador 1 ao final do turno
        jogador1.cartasNaMao.clear()
    }

    // Verifica o motivo pelo qual o jogo terminou (baralho vazio ou jogador sem vida)
    if (baralho.cartas.isEmpty()) {
        println("O jogo terminou, porque o baralho não tem mais cartas.")
    }

    // Exibe o resultado final do jogo (vencedor ou empate)
    jogo.calcularVencedor()
}

// Função para exibir o estado do jogo
fun exibirEstadoJogo(jogador1: Jogador, jogador2: Jogador, numTurno: Int) {
    println("\n---------------------------------------------")
    println("\n${GREEN}Estado do jogo - $numTurno° partida:${RESET}\n")

    // Exibe informações sobre o jogador 1
    println("${jogador1.nome} - Vida: ${jogador1.vida}")
    jogador1.mostrarMao()
    jogador1.mostrarMonstroTabuleiro()

    // Exibe informações sobre o jogador 2
    println("\n${jogador2.nome} - Vida: ${jogador2.vida}")
    jogador2.mostrarMao()
    jogador2.mostrarMonstroTabuleiro()

    println("\n---------------------------------------------${RESET}")
}

// Função para controlar o turno de um jogador
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
