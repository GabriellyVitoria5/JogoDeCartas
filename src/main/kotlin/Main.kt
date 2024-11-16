fun main() {
    println("\n${GREEN}----- Batalha de Cartas Colecionáveis do Seu José -----${RESET}\n")
    println("Montando o baralho do jogo...")

    // Cria uma instância de baralho e verifica se as cartas do baralho foram carregadas corretamente do arquivo CSV
    val baralho = Baralho()
    baralho.carregarCartasDoArquivo("src/main/kotlin/resources/cartas.csv")

    // Se o baralho está vazio, exibe erro e termina a execução
    if (baralho.cartas.isEmpty()) {
        println("\n${RED}Erro: O baralho está vazio. Verifique o arquivo CSV.${RESET}")
        return
    }

    // Embaralha o baralho para garantir que as cartas estarão em ordem aleatória
    baralho.embaralhar()

    println("${GREEN}Baralho está pronto! O jogo pode começar!!${RESET}")

    // Solicita nomes dos jogadores
    print("\nDigite o nome do primeiro jogador: ")
    val nomeJogador1 = readlnOrNull()?.takeIf { it.isNotBlank() } ?: "Jogador 1"
    print("Digite o nome do segundo: ")
    val nomeJogador2 = readlnOrNull()?.takeIf { it.isNotBlank() } ?: "Jogador 2"

    // Cria dois jogadores para o jogo
    val jogador1 = Jogador(nomeJogador1)
    val jogador2 = Jogador(nomeJogador2)

    // Cria uma instância do jogo com os dois jogadores e o baralho carregado
    val jogo = Jogo(jogador1, jogador2, baralho)

    // Iniciar jogo
    jogo.iniciarJogo()

    // Exibe o resultado final do jogo (vencedor ou empate)
    jogo.calcularVencedor()
}