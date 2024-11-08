// Classe base que representa uma carta genérica
open class Carta(
    val nome: String,
    val descricao: String,
    val ataque: Int,
    val defesa: Int,
    val tipo: String
) {
    // Retorna uma representação em string da carta
    override fun toString(): String {
        return """
            |Nome: $nome
            |Descrição: $descricao
            |Ataque: $ataque
            |Defesa: $defesa
            |Tipo: $tipo
        """.trimMargin()
    }
}