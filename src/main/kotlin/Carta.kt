open class Carta(
    val nome: String,
    val descricao: String,
    var ataque: Int,
    var defesa: Int,
    val tipo: String
) {
    // Representação personalizada de uma carta
    override fun toString(): String {
        return "- $tipo $nome: $descricao - A:$ataque, D:$defesa"
    }
}