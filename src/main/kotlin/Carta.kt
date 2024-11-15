// Classe base para representar uma Carta genérica
open class Carta(
    val nome: String,         // Nome da carta
    val descricao: String,    // Descrição da carta
    var ataque: Int,          // Pontos de ataque da carta
    var defesa: Int,          // Pontos de defesa da carta
    private val tipo: String  // Tipo da carta (ex.: monstro, equipamento)
) {
    /**
     * Representação personalizada de uma carta em forma de string.
     * Exemplo de saída: "- Monstro Dragão: Um dragão imponente!       ; A:3000 D:2500"
     *
     * @return String representando os detalhes da carta.
     */
    override fun toString(): String {
        return "- ${tipo.padEnd(6)} ${nome.padEnd(12)}: ${descricao.padEnd(25)} - A:${ataque.toString().padEnd(4)} D:${defesa.toString().padEnd(4)}"
    }
}
