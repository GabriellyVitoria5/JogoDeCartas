open class Carta(
    val nome: String,
    val descricao: String,
    val ataque: Int,
    val defesa: Int,
    val tipo: String
) {
    // Representação personalizada de uma carta
    override fun toString(): String {
        return "- $tipo $nome: $descricao - A:$ataque, D:$defesa"
    }
}