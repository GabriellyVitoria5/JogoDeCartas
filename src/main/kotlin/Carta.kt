open class Carta(
    val nome: String,
    val descricao: String,
    val ataque: Int,
    val defesa: Int,
    val tipo: String
) {
    // Representação personalizada de uma carta
    override fun toString(): String {
        return """
            |Nome: $nome
            |Descrição: $descricao
            |Ataque: $ataque
            |Defesa: $defesa
            |Tipo: $tipo
        """.trimMargin() // Remove margens para melhor formatação
    }
}