// Códigos ANSI para adicionar cores no menu
const val RESET = "\u001B[0m"
const val RED = "\u001B[31m" // Cor para jogador 1
const val BLUE = "\u001B[34m" // Cor para jogador 2
const val GREEN = "\u001B[32m" // Cor para destacar informações gerais

fun main() {
    // Cria uma instância de baralho e carrega as cartas a partir de um arquivo CSV
    val baralho = Baralho()

    // Carrega as cartas do arquivo CSV e verifica se o baralho foi carregado corretamente
    baralho.carregarCartasDoArquivo("src/main/kotlin/resources/cartas.csv")

    // Se o baralho estiver vazio, exibe erro e termina a execução
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
        // Exibe o estado do jogo para ambos os jogadores antes de iniciar o turno
        println("\n---------------------------------------------")
        println("\n${GREEN}Estado do jogo - $numTurno° partida:${RESET}\n")

        // Exibe informações sobre o jogador 1: nome, vida, cartas e monstros no tabuleiro
        println("${jogador1.nome} - Vida: ${jogador1.vida}")
        jogo.mostrarMao(jogador1)
        jogador1.mostrarMonstroTabuleiro()

        // Exibe informações sobre o jogador 2: nome, vida, cartas e monstros no tabuleiro
        println("\n${jogador2.nome} - Vida: ${jogador2.vida}")
        jogo.mostrarMao(jogador2)
        jogador2.mostrarMonstroTabuleiro()

        println("\n---------------------------------------------${RESET}")

        // Inicia o turno do jogador 1
        // Se o jogador tiver cartas, ele compra uma; se não tiver, distribui 5 novas cartas
        println("\n${RED}Turno de ${jogador1.nome}:${RESET}")
        jogo.vezJogador = true
        if (jogador1.cartasNaMao.isNotEmpty()) {
            jogador1.comprarCarta(baralho.cartas) // Compra uma carta se houver cartas
        } else {
            jogo.distribuirCartas(jogador1) // Distribui 5 novas cartas se não tiver cartas
        }
        jogador1.jogar(jogo) // Jogador 1 realiza sua jogada

        // Se o jogador 2 perder toda a vida, o jogo termina
        if (!jogador2.temVida()) break

        // Inicia o turno do jogador 2
        // Se o jogador tiver cartas, ele compra uma; se não, distribui 5 novas cartas
        println("\n${BLUE}Turno de ${jogador2.nome}:${RESET}")
        jogo.vezJogador = false
        if (jogador2.cartasNaMao.isNotEmpty()) {
            jogador2.comprarCarta(baralho.cartas) // Compra uma carta se houver cartas
        } else {
            jogo.distribuirCartas(jogador2) // Distribui 5 novas cartas se não tiver cartas
        }
        jogador2.jogar(jogo) // Jogador 2 realiza sua jogada

        // Se o jogador 1 perder toda a vida, o jogo termina
        if (!jogador1.temVida()) break

        // Incrementa o número do turno após ambos os jogadores jogarem
        numTurno++
    }

    // Verifica o motivo pelo qual o jogo terminou (baralho vazio ou jogador sem vida)
    if (baralho.cartas.isEmpty()) {
        println("O jogo terminou, porque o baralho não tem mais cartas.")
    }

    // Exibe o resultado final do jogo (vencedor ou empate)
    jogo.calcularVencedor()
}
