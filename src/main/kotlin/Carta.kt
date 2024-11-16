/**
 * Classe abstrata base que representa uma Carta genérica.
 *
 * @property nome Nome da carta.
 * @property descricao Descrição da carta.
 * @property ataque Pontos de ataque da carta.
 * @property defesa Pontos de defesa da carta.
 * @property tipo Tipo da carta (ex.: monstro, equipamento).
 */
abstract class Carta(
    val nome: String,
    val descricao: String,
    var ataque: Int,
    var defesa: Int,
    private val tipo: String
) {
    /**
     * Gera uma representação em texto formatada da carta.
     * Exemplo de saída: - Monstro Dragão: Um dragão imponente - A:3000 D:2500
     *
     * @return Uma string contendo os detalhes da carta.
     */
    override fun toString(): String {
        return "- ${tipo.padEnd(6)} ${nome.padEnd(12)}: ${descricao.padEnd(25)} - A:${ataque.toString().padEnd(4)} D:${defesa.toString().padEnd(4)}"
    }
}
