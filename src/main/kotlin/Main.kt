//Função principal que executa o fluxo do jogo de cartas colecionáveis
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

    // Inicia a partida com os dois jogadores e o baralho definidos
    jogo.iniciarJogo()

    // Imprime o resultado do jogo
    jogo.calcularVencedor()



//    // Exibe a vida dos jogadores após o turno de ataque
//    println("\n${jogador1.nome} vida: ${jogador1.vida}")
//    println("${jogador2.nome} vida: ${jogador2.vida}")
//
//    // Exibe as cartas na mão de cada jogador
//    jogo.mostrarMao(jogador1)
//    jogo.mostrarMao(jogador2)
//
//    // Posiciona uma carta monstro para cada jogador no campo
//    jogador1.posicionarMonstro(jogador1.cartasNaMao[0], "ataque")
//    jogador2.posicionarMonstro(jogador2.cartasNaMao[0], "defesa")
//
//    // Simula um turno de ataque entre os jogadores
//    jogo.turno()

}
