/**
 * Classe que representa uma Carta de Monstro.
 * Herda as propriedades e comportamentos da classe base [Carta].
 *
 * @param nome Nome do monstro.
 * @param descricao Descrição do monstro.
 * @param ataque Pontos de ataque do monstro.
 * @param defesa Pontos de defesa do monstro.
 */
class CartaMonstro(
    nome: String,
    descricao: String,
    ataque: Int,
    defesa: Int
) : Carta(nome, descricao, ataque, defesa, "Monstro") {

    /**
     * Estado atual da carta no campo.
     *
     * Valores possíveis:
     * - "ataque": indica que o monstro está em posição de ataque.
     * - "defesa": indica que o monstro está em posição de defesa.
     * - "não definido" (padrão): indica que a posição ainda não foi atribuída.
     */
    var estado: String = "não definido"
}
