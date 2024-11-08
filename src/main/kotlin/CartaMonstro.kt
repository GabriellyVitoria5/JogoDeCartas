// Classe que representa uma carta de monstro
class CartaMonstro(
    nome: String,
    descricao: String,
    ataque: Int,
    defesa: Int
) : Carta(nome, descricao, ataque, defesa, "Monstro") {

    // Estado começa como nulo, mas será definido ("ataque" ou "defesa") quando o monstro for posicionado
    var estado: String? = null

    var posicionada: Boolean = false // Indica se a carta está em campo

    // Sobrescreve a representação em string para incluir o estado
    override fun toString(): String {
        return """
            |Nome: $nome
            |Descrição: $descricao
            |Ataque: $ataque
            |Defesa: $defesa
            |Tipo: $tipo
            |Estado: $estado
        """.trimMargin()
    }
}