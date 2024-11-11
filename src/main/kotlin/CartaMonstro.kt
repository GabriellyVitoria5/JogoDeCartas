class CartaMonstro(
    nome: String,
    descricao: String,
    ataque: Int,
    defesa: Int
) : Carta(nome, descricao, ataque, defesa, "Monstro") {

    var estado: String? = null // Estado começa como nulo, mas será definido ("ataque" ou "defesa") quando o monstro for posicionado
    var posicionada: Boolean = false // Indica se a carta está em campo

    // Incluir estado do monstro
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