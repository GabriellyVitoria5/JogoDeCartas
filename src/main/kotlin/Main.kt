// Função principal que executa o fluxo do jogo de cartas colecionáveis
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

    // Loop do jogo, onde os jogadores alternam turnos até que um deles perca toda a vida
    while (jogador1.temVida() && jogador2.temVida()) {
        // Exibe o estado do jogo para ambos os jogadores antes de iniciar o turno
        println("\n---------------------------------------------------")
        println("\nEstado atual do jogo\n:")
        println("${jogador1.nome} - Vida: ${jogador1.vida}")
        jogo.mostrarMao(jogador1)

        println("\n${jogador2.nome} - Vida: ${jogador2.vida}")
        jogo.mostrarMao(jogador2)

        println("\n---------------------------------------------------")

        // Inicia o turno para o jogador 1
        println("\nTurno de ${jogador1.nome}:")
        jogador1.jogar(jogo)
        if (!jogador2.temVida()) break // Verifica se o jogador 2 ainda tem vida após o turno

        // Inicia o turno para o jogador 2
        println("\nTurno de ${jogador2.nome}:")
        jogador2.jogar(jogo)
        if (!jogador1.temVida()) break // Verifica se o jogador 1 ainda tem vida após o turno
    }

    // Exibe o resultado do jogo ao final
    jogo.calcularVencedor()
}
