//Classe que representa um jogador em um jogo de cartas colecionáveis.
class Jogador(
    val nome: String,
) {
    val cartasNaMao: MutableList<Carta> = mutableListOf()        // Cartas atualmente na mão do jogador
    val monstrosNoCampo: MutableList<CartaMonstro> = mutableListOf()    // Monstros posicionados no campo de batalha
    var vida: Int = 10000                                        // Vida inicial do jogador
    val jogadasEscolhidas: MutableList<String> = mutableListOf() // Lista de jogadas escolhidas

    // Recebe uma carta na mão do jogador, caso haja espaço
    fun receberCarta(carta: Carta) {
        if (cartasNaMao.size < 10) {
            cartasNaMao.add(carta)
        } else {
            println("\n$nome já possui 10 cartas na mão. Descarte uma carta antes de receber outra.")
        }
    }

    // Verificar se o jogador ainda possui pontos de vida
    fun temVida():Boolean{
        return vida > 0
    }

    // Controlar as jogadas escolhidas pelo jogador durante a sua vez
    // FAZER VERICAÇÕES SE A OPÇÃO NÃO FOI ESCOLHIDA MAIS DE UMA VEZ, A OPÇÃO NÃO APARECE NO MENU, MAS ELA AINDA EXECUTA
    fun jogar(jogo: Jogo) {
        var fimRodada = false

        do {
            // Mostra a mão do jogador no começo de cada rodada
            println("\nCartas na mão de $nome:")
            for (carta in cartasNaMao) {
                println("- ${carta.nome}: ${carta.descricao} - A:${carta.ataque}, D: ${carta.defesa}")
            }

            // Exibe o menu de jogadas
            jogo.imprimirMenuDinamico(this)

            // Solicita a escolha do jogador
            print("\n$nome, digite a opção desejada: ")
            val op = readlnOrNull() ?: ""

            // Verifica se o jogador já realizou um ataque e tenta mudar o estado do monstro
            if ("d" in jogadasEscolhidas && op == "e") {
                println("Não é possível alterar o estado do monstro após atacar.")
            } else {
                jogo.processarJogadas(this, op)
            }

            // Verificar se a rodada terminou
            if (op == "f") {
                fimRodada = true
            }
            println("\n----------------------------------------------------------------------")

        } while (!fimRodada)

        limparJogadas() // Limpar as jogadas após a rodada terminar
    }

    // Limpa as jogadas para a próxima rodada
    private fun limparJogadas() {
        jogadasEscolhidas.clear()
    }

    fun posicionarMonstro() {
        //Filtrar cartas de monstro da mão do jogador
        val cartasMonstro = cartasNaMao.filterIsInstance<CartaMonstro>()

        if (cartasMonstro.isEmpty()) {
            println("Você não possui cartas do tipo monstro para posicionar.")
            return
        }

        println("\nEscolha uma carta para posicionar como monstro:")

        // Exibe todas as cartas da mão com índice para seleção
        cartasMonstro.forEachIndexed { index, carta ->
            println("Opção ${index + 1}: ${carta.nome} - ${carta.tipo} - ${carta.descricao}")
        }

        // Obtém a escolha do usuário
        print("\nDigite o número da carta que deseja posicionar: ")
        val escolha = readlnOrNull()?.toIntOrNull()

        if (escolha != null && escolha in 1..cartasNaMao.size) {
            val cartaEscolhida = cartasMonstro[escolha - 1]

            // Adiciona a carta de monstro no tabuleiro
            monstrosNoCampo.add(cartaEscolhida)

            // Marque a carta como posicionada
            cartaEscolhida.posicionada = true
            println("Você posicionou ${cartaEscolhida.nome} como monstro.")

            // Remove a carta da mão
            cartasNaMao.removeAt((escolha - 1))
        } else {
            println("Escolha inválida.")
        }
    }

    // Realiza um ataque contra o monstro de um jogador oponente
    fun atacar(monstroAtacante: CartaMonstro, oponente: Jogador, monstroDefensor: Carta) {
        if (monstroAtacante.estado == "ataque") {
            // Verifica se o ataque é maior que a defesa do monstro defensor
            if (monstroAtacante.ataque > monstroDefensor.defesa) {
                val dano = monstroAtacante.ataque - monstroDefensor.defesa
                oponente.vida -= dano
                println("$nome ataca ${oponente.nome} e causa $dano de dano!")
            } else {
                // Caso contrário, o jogador atacante recebe o dano
                val dano = monstroDefensor.defesa - monstroAtacante.ataque
                vida -= dano
                println("\n${oponente.nome} se defende e causa $dano de dano ao atacante!")
            }
        } else {
            println("\nO monstro não está em estado de ataque e não pode atacar.")
        }
    }
}
