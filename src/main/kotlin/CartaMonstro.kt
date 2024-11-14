// Classe que representa uma Carta de Monstro, derivada da classe base Carta
class CartaMonstro(
    nome: String,            // Nome do monstro
    descricao: String,       // Descrição do monstro
    ataque: Int,             // Pontos de ataque do monstro
    defesa: Int              // Pontos de defesa do monstro
) : Carta(nome, descricao, ataque, defesa, "Monstro") {

    // Estado da carta em campo: "ataque" ou "defesa", padrão "não definido" até a posição ser atribuída
    var estado: String = "não definido"

    // Indica se a carta está posicionada no campo de batalha
    var posicionada: Boolean = false
}
