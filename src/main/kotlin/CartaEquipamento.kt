// Classe que representa uma Carta de Equipamento, derivada da classe base Carta
class CartaEquipamento(
    nome: String,           // Nome do equipamento
    descricao: String,      // Descrição do equipamento
    ataque: Int,            // Modificador de ataque do equipamento
    defesa: Int             // Modificador de defesa do equipamento
) : Carta(nome, descricao, ataque, defesa, "Equipamento")
