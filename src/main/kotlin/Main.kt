// códigos ANSI para adicionar cores no menu
const val RESET = "\u001B[0m"
const val RED = "\u001B[31m" // cor para jogador 1
const val BLUE = "\u001B[34m" // cor para jogador 2
const val GREEN = "\u001B[32m"

fun main() {
    val baralho = Baralho()

    // Carrega as cartas do arquivo CSV e verifica se o baralho foi carregado corretamente
    baralho.carregarCartasDoArquivo("src/main/kotlin/resources/cartas.csv")
    if (baralho.cartas.isEmpty()) {
        println("\nErro: O baralho está vazio. Verifique o arquivo CSV.")
        return
    }

    // Embaralha o baralho para iniciar o jogo com cartas em ordem aleatória
    baralho.embaralhar()

    // Cria dois jogadores para o jogo
    val jogador1 = Jogador("Jogador 1")
    val jogador2 = Jogador("Jogador 2")

    // Cria uma instância do jogo com os jogadores e o baralho carregado
    val jogo = Jogo(jogador1, jogador2, baralho)

    // Distribui cartas iniciais para cada jogador
    jogo.distribuirCartasIniciais()

    var numTurno = 1 // Controle no número de turnos

    // Loop do jogo, onde os jogadores alternam turnos até que um deles perca toda a vida
    while (jogador1.temVida() && jogador2.temVida()) {
        // Exibe o estado do jogo para ambos os jogadores antes de iniciar o turno
        println("\n---------------------------------------------")
        println("\n${GREEN}Estado do jogo - $numTurno° partida:${RESET}\n")

        println("${jogador1.nome} - Vida: ${jogador1.vida}")
        jogo.mostrarMao(jogador1)
        jogador1.mostrarMonstroTabuleiro()

        println("\n${jogador2.nome} - Vida: ${jogador2.vida}")
        jogo.mostrarMao(jogador2)
        jogador2.mostrarMonstroTabuleiro()

        println("\n---------------------------------------------${RESET}")

        // Inicia o turno para o jogador 1 - compra uma carta e começa a jogar se jogadores tiverem vida
        println("\n${RED}Turno de ${jogador1.nome}:${RESET}")
        jogador1.comprarCarta(baralho.cartas)
        jogador1.jogar(jogo)
        if (!jogador2.temVida()) break

        // Inicia o turno para o jogador 2 - compra uma carta e começa a jogar se jogadores tiverem vida
        println("\n${BLUE}Turno de ${jogador2.nome}:${RESET}")
        jogador2.comprarCarta(baralho.cartas)
        jogador2.jogar(jogo)
        if (!jogador1.temVida()) break

        numTurno++
    }

    // Exibe o resultado do jogo ao final
    jogo.calcularVencedor()
}
