/**
 * Classe que representa uma Carta de Equipamento.
 * Herda as propriedades e comportamentos da classe base [Carta].
 *
 * @param nome Nome do equipamento.
 * @param descricao Descrição do equipamento.
 * @param ataque Modificador de ataque fornecido pelo equipamento.
 * @param defesa Modificador de defesa fornecido pelo equipamento.
 */
class CartaEquipamento(
    nome: String,
    descricao: String,
    ataque: Int,
    defesa: Int
) : Carta(nome, descricao, ataque, defesa, "Equipamento")
