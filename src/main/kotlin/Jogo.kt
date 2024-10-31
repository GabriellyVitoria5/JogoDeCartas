// Classe que representa um jogo de cartas colecionáveis entre dois jogadores
class Jogo(
    private val jogador1: Jogador,
    private val jogador2: Jogador,
    private val baralho: Baralho
) {
    // Distribui cartas iniciais para os dois jogadores
    fun distribuirCartasIniciais() {
        // Verifica se o baralho possui cartas suficientes
        if (baralho.cartas.size < 10) {
            println("\nErro: O baralho não tem cartas suficientes para distribuir.")
            return
        }

        // Cada jogador recebe 5 cartas
        for (i in 1..5) {
            jogador1.receberCarta(baralho.cartas.removeAt(0))
            jogador2.receberCarta(baralho.cartas.removeAt(0))
        }
    }

    // Mostra as cartas na mão de um jogador
    fun mostrarMao(jogador: Jogador) {
        println("\n${jogador.nome} tem as seguintes cartas na mão:")
        for (carta in jogador.cartasNaMao) {
            println("\n" + carta)
            println("---------------------------------------------")
        }
    }

    // Executa um turno no jogo, onde o jogador 1 ataca o jogador 2
    fun turno() {
        // Verifica se ambos os jogadores têm monstros no campo
        if (jogador1.monstrosNoCampo.isNotEmpty() && jogador2.monstrosNoCampo.isNotEmpty()) {
            val monstroAtacante = jogador1.monstrosNoCampo[0] // Primeiro monstro do jogador 1
            val monstroDefensor = jogador2.monstrosNoCampo[0] // Primeiro monstro do jogador 2
            jogador1.atacar(monstroAtacante, jogador2, monstroDefensor) // Jogador 1 ataca Jogador 2
        } else {
            println("\nUm dos jogadores não possui monstros no campo para atacar.")
        }
    }
}
