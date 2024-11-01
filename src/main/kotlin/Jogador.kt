//Classe que representa um jogador em um jogo de cartas colecionáveis.
class Jogador(
    val nome: String,
) {
    val cartasNaMao: MutableList<Carta> = mutableListOf()     // Cartas atualmente na mão do jogador
    val monstrosNoCampo: MutableList<Carta> = mutableListOf() // Monstros posicionados no campo de batalha
    var vida: Int = 10000                                     // Vida inicial do jogador

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

    // Posiciona um monstro no campo de batalha em um estado especificado
    fun posicionarMonstro(carta: Carta, estado: String) {
        if (monstrosNoCampo.size < 5) {
            carta.estado = estado // Define o estado inicial do monstro
            monstrosNoCampo.add(carta)
            cartasNaMao.remove(carta) // Remove a carta da mão do jogador
            println("\n$nome posicionou ${carta.nome} no campo em estado de $estado.")
        } else {
            println("\nLimite de monstros no campo atingido!")
        }
    }

    // Realiza um ataque contra o monstro de um jogador oponente
    fun atacar(monstroAtacante: Carta, oponente: Jogador, monstroDefensor: Carta) {
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
