class CartaMonstro(
    nome: String,
    descricao: String,
    ataque: Int,
    defesa: Int
) : Carta(nome, descricao, ataque, defesa, "Monstro") {

    var estado: String = "não definido" // Estado será definido ("ataque" ou "defesa") quando o monstro for posicionado
    var posicionada: Boolean = false // Indica se a carta está em campo
    
}