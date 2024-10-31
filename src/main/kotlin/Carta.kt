// Classe que representa uma carta em um jogo de cartas colecionáveis
class Carta(
    val nome: String,                // Nome da carta
    private val descricao: String,   // Descrição da carta
    val ataque: Int,                 // Valor de ataque
    val defesa: Int,                 // Valor de defesa
    private val tipo: String         // Tipo da carta
) {
    // Estado inicial do monstro: pode ser "ataque" ou "defesa"
    var estado: String = "ataque"

    // Retorna uma representação em string da carta
    override fun toString(): String {
        return """
            |Nome: $nome
            |Descrição: $descricao
            |Ataque: $ataque
            |Defesa: $defesa
            |Tipo: $tipo
            |Estado: $estado
        """.trimMargin() // Remove margens para melhor formatação
    }
}
