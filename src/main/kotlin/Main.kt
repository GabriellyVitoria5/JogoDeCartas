fun main() {
    val baralho = Baralho()
    baralho.carregarCartasDoArquivo("src/main/kotlin/cartas.csv")
    baralho.embaralhar()

    // imprime as cartas
    for (carta in baralho.cartas) {
        println(carta.nome)
        println(carta.descricao)
        println("Ataque: ${carta.ataque}")
        println("Defesa: ${carta.defesa}")
        println("Tipo: ${carta.tipo}")
        println("------------------")
    }
}