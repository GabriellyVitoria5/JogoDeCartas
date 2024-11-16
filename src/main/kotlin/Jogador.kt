/**
 * Representa um jogador no jogo, com informações sobre suas cartas, monstros e pontos de vida.
 * O jogador pode comprar, descartar e jogar cartas, além de ter suas jogadas registradas em cada rodada.
 *
 * @param nome O nome do jogador.
 */
class Jogador(
    val nome: String,
) {
    val cartasNaMao: MutableList<Carta> = mutableListOf()
    private val monstrosNoCampo: MutableList<CartaMonstro> = mutableListOf()
    var vida: Int = 10000
    val jogadasEscolhidas: MutableList<String> = mutableListOf()

    /**
     * Apresentar as opções de cartas disponíveis e capturar a escolha do jogador.
     * Retorna o índice da carta escolhida ou -1 se a escolha for inválida.
     *
     * @param opcoes Lista de strings representando as opções de cartas.
     * @param prompt Mensagem exibida solicitando que o jogador faça sua escolha.
     * @return O índice da carta escolhida ou -1 se a escolha for inválida.
     */
    private fun escolherCarta(opcoes: List<String>, prompt: String): Int {
        println("\nCartas disponíveis:")
        opcoes.forEachIndexed { index, carta -> println("Opção ${index + 1}: $carta") }
        print("\n$prompt")
        val escolha = readlnOrNull()?.toIntOrNull()
        return escolha?.takeIf { it in 1..opcoes.size }?.minus(1) ?: -1
    }

    /**
     * Permite ao jogador comprar uma carta do baralho, caso haja espaço.
     * Se o jogador já possui 10 cartas, será solicitado que ele descarte uma carta primeiro.
     *
     * @param baralho O baralho de cartas do qual o jogador vai comprar.
     */
    fun comprarCarta(baralho: MutableList<Carta>) {
        if (baralho.isNotEmpty()) {
            while (cartasNaMao.size == 10) {
                println("\n${YELLOW}$nome atingiu o limite de 10 cartas. Descarte uma carta.${RESET}")
                descartar()
            }
            cartasNaMao.add(baralho.removeAt(0))
        } else {
            println("${RED}nBaralho não possui mais cartas para comprar.${RESET}")
        }
    }

    /**
     * Permite ao jogador descartar uma carta de sua mão.
     * O jogador escolhe qual carta deseja descartar.
     */
    fun descartar() {
        if (cartasNaMao.isEmpty()) {
            println("\n${YELLOW}$nome não tem cartas na mão para descartar.${RESET}")
            return
        }

        val escolha = escolherCarta(cartasNaMao.map { it.toString() }, "Escolha uma das cartas para descartar: ")

        // Remove a carta escolhida da mão do jogador
        if (escolha != -1) {
            val cartaDescartada = cartasNaMao.removeAt(escolha)
            println("$nome descartou: ${cartaDescartada.nome}")
        } else {
            // Jogador pode tentar descartar de novo nessa rodada se inserir um valor inválido
            jogadasEscolhidas.remove("c")
            println("${RED}Escolha inválida. Não foi possível descartar, tente novamente.${RESET}")
        }
    }

    /**
     * Verifica se o jogador ainda tem pontos de vida.
     *
     * @return Retorna `true` se o jogador ainda tem vida, `false` caso contrário.
     */
    fun temVida(): Boolean {
        return vida > 0
    }

    /**
     * Controla as jogadas escolhidas pelo jogador durante sua vez no jogo.
     * O jogador pode escolher realizar ações como atacar, comprar cartas, descartar, etc.
     * O loop continua até que o jogador finalize a rodada usando a opção de passar a vez.
     *
     * @param jogo Instância do jogo, que controla o andamento da partida.
     */
    fun jogar(jogo: Jogo) {
        var fimRodada = false

        do {
            if (!jogo.jogadoresTemVida()) return  // Verifica se todos os jogadores têm vida, caso contrário, termina o jogo.

            // Exibe as cartas do jogador, monstros no campo e o menu de opções
            mostrarMao()
            mostrarMonstroTabuleiro()
            jogo.imprimirMenuDinamico(this)

            print("\n$nome, digite a opção desejada: ")
            val op = readlnOrNull() ?: ""  // Lê a opção de jogada do jogador

            // Processa a jogada escolhida pelo jogador e verifica se foi válida ou repetida
            jogo.processarJogadas(this, op)

            // Eencerra a rodada
            if (op == "f") {
                fimRodada = true
            }

            println("\n----------------------------------------------------------------------")
        } while (!fimRodada)

        limparJogadas()  // Limpa as jogadas após o fim da rodada
    }

    /**
     * Limpa as jogadas escolhidas pelo jogador após o término da rodada.
     */
    private fun limparJogadas() {
        jogadasEscolhidas.clear()
    }

    /**
     * Exibe as cartas que o jogador tem na mão, com detalhes como nome, tipo e estatísticas.
     */
    fun mostrarMao() {
        println("\n$nome tem as seguintes cartas na mão:")
        if (cartasNaMao.isEmpty()) {
            println("Nenhuma carta na mão")
            return
        }
        println("\nTipo | Nome | Descrição | Ataque | Defesa | Estado do monstro")
        for (carta in cartasNaMao) {
            println(carta)
        }
        println("---------------------------------------------")
    }

    /**
     * Exibe os monstros posicionados no tabuleiro de batalha do jogador, com suas estatísticas.
     */
    fun mostrarMonstroTabuleiro() {
        println("\nMonstros no tabuleiro de $nome")
        if (monstrosNoCampo.isEmpty()) {
            println("Nenhum monstro posicionado")
            println("---------------------------------------------")
            return
        }
        println("\nNome | Ataque | Defesa | Estado do monstro")
        monstrosNoCampo.joinToString("\n") { monstro ->
            "[ ${monstro.nome.padEnd(21)} | A:${monstro.ataque.toString().padEnd(6)} | D:${monstro.defesa.toString().padEnd(6)} | ${monstro.estado} ]"
        }.also(::println)
        println("---------------------------------------------")
    }

    /**
     * Permite ao jogador posicionar um monstro no campo de batalha.
     * O jogador deve escolher um monstro da sua mão e definir seu estado (Ataque ou Defesa).
     */
    fun posicionarMonstro() {
        if (monstrosNoCampo.size >= 5) {
            println("${RED}Número máximo de monstros no campo atingido.${RESET}")
            return
        }

        // Filtra as cartas de monstro da mão do jogador
        val cartasMonstro = cartasNaMao.filterIsInstance<CartaMonstro>()

        // Verifica se o jogador tem cartas de monstro para posicionar
        if (cartasMonstro.isEmpty()) {
            println("${YELLOW}Você não possui cartas do tipo monstro para posicionar.${RESET}")
            return
        }

        // Jogador escolhe uma carta de monstro para posicionar no campo
        var escolha = -1
        while (escolha == -1) {
            escolha = escolherCarta(
                cartasMonstro.map { it.toString() },
                "Escolha uma carta de monstro para posicionar no tabuleiro: "
            )
            if (escolha == -1) println("${RED}Escolha inválida! Por favor, selecione um número de opção válida.${RESET}")
        }

        // Adiciona o monstro no campo e remove a carta da mão
        val monstroEscolhido = cartasMonstro[escolha]
        monstrosNoCampo.add(monstroEscolhido)
        cartasNaMao.remove(monstroEscolhido)

        // Jogador escolhe o estado do monstro (Ataque ou Defesa)
        while (true) {
            print("\nDefina o estado do monstro como posição de ataque ou defesa (A ou D): ")
            when (readlnOrNull()?.lowercase()) {
                "a" -> {
                    monstroEscolhido.estado = "Ataque"
                    break
                }

                "d" -> {
                    monstroEscolhido.estado = "Defesa"
                    break
                }
                else -> println("${RED}Escolha inválida. Por favor, escolha 'A' ou 'D'.${RESET}")
            }
        }

        println("${monstroEscolhido.nome} foi posicionado em estado de ${monstroEscolhido.estado}.")
    }

    /**
     * Permite ao jogador equipar um monstro com uma carta de equipamento da sua mão.
     * O jogador escolhe um monstro e um equipamento e então aumenta os atributos do monstro com base no equipamento.
     */
    fun equiparMonstro() {
        if (monstrosNoCampo.isEmpty()) {
            println("\nVocê não possui monstros no campo para equipar.")
            return
        }

        // Filtra as cartas de equipamento da mão do jogador
        val cartasEquipamento = cartasNaMao.filterIsInstance<CartaEquipamento>()

        // Verifica se o jogador possui equipamentos para usar
        if (cartasEquipamento.isEmpty()) {
            println("${YELLOW}Você não possui cartas de equipamento na mão.${RESET}")
            return
        }

        // Jogador escolhe um monstro do campo para equipar
        var escolhaMonstro = -1
        while (escolhaMonstro == -1) {
            escolhaMonstro = escolherCarta(
                monstrosNoCampo.map { "${it.nome.padEnd(12)} - A:${it.ataque.toString().padEnd(4)} D:${it.defesa.toString().padEnd(4)}" },
                "Escolha um monstro para equipar: "
            )
            if (escolhaMonstro == -1) println("${RED}Escolha inválida! Por favor, selecione um número de opção válida.${RESET}")
        }
        val monstroEscolhido = monstrosNoCampo[escolhaMonstro]

        // Jogador escolhe um equipamento da mão
        var escolhaEquipamento = -1
        while (escolhaEquipamento == -1) {
            escolhaEquipamento = escolherCarta(
                cartasEquipamento.map { "${it.nome.padEnd(12)} - A:${it.ataque.toString().padEnd(4)} D:${it.defesa.toString().padEnd(4)}" },
                "Escolha uma carta de equipamento para usar em ${monstroEscolhido.nome}: "
            )
            if (escolhaEquipamento == -1) println("${RED}Escolha inválida! Por favor, selecione um número de opção válida.${RESET}")
        }
        val equipamentoEscolhido = cartasEquipamento[escolhaEquipamento]

        // Jogador é obridado a escolher qual atributo do monstro será aumentado (Ataque ou Defesa)
        while (true) {
            print("Escolha o atributo do monstro a ser aumentado pelo equipamento (A - Ataque, D - Defesa): ")
            when (readlnOrNull()?.lowercase()) {
                "a" -> {
                    monstroEscolhido.ataque += equipamentoEscolhido.ataque
                    println("\n${monstroEscolhido.nome} foi equipado com ${equipamentoEscolhido.nome}, recebendo +${equipamentoEscolhido.ataque} de ataque.")
                    break
                }

                "d" -> {
                    monstroEscolhido.defesa += equipamentoEscolhido.defesa
                    println("\n${monstroEscolhido.nome} foi equipado com ${equipamentoEscolhido.nome}, recebendo +${equipamentoEscolhido.defesa} de defesa.")
                    break
                }
                else -> println("${RED}Escolha inválida. Por favor, escolha 'A' ou 'D'.${RESET}")
            }
        }

        // Remove o equipamento da mão do jogador após o uso
        cartasNaMao.remove(equipamentoEscolhido)
    }

    /**
     * Permite ao jogador atacar o oponente com todos os seus monstros no tabuleiro em posição de ataque.
     * O jogador escolhe um monstro para atacar e o alvo (monstro do oponente ou ataque direto).
     */
    fun atacarOponente(oponente: Jogador) {
        // Filtra os monstros do jogador atual que estão em posição de ataque
        val monstrosEmAtaque = monstrosNoCampo.filter { it.estado == "Ataque" }.toMutableList()

        // Se não houver monstros em posição de ataque, jogador tem a chance de posicionar um monstro e tentar atacar novamente
        if (monstrosEmAtaque.isEmpty()) {
            println("${YELLOW}$nome não tem monstros em posição de ataque para realizar o ataque!${RESET}")
            jogadasEscolhidas.remove("d")
            return
        }

        println("\nVidas:\n$nome: $vida\n${oponente.nome}: ${oponente.vida}\n")

        // Loop enquanto houver monstros em ataque e o oponente ainda tiver pontos de vida
        while (monstrosEmAtaque.isNotEmpty() && oponente.temVida()) {
            // Escolhe um monstro atacante
            val atacante = escolherMonstro(monstrosEmAtaque, "\nEscolha um monstro do seu tabuleiro para atacar:")

            // Determina o alvo: se o oponente tiver monstros, escolhe um deles; caso contrário, será ataque direto
            val alvo = if (oponente.monstrosNoCampo.isNotEmpty()) {
                escolherMonstro(oponente.monstrosNoCampo, "\nEscolha um monstro do oponente para atacar:")
            } else null

            if (alvo != null) {
                // Processa o ataque contra um monstro adversário
                processarAtaque(atacante, alvo, oponente)

                // Remove o alvo se ele for destruído
                if (alvo.ataque <= 0 || alvo.defesa <= 0) {
                    oponente.monstrosNoCampo.remove(alvo)
                    println("${alvo.nome} foi destruído e removido do campo de ${oponente.nome}.")
                }
            } else {
                // Realiza ataque direto caso não haja monstros para defender
                oponente.vida -= atacante.ataque
                println("${atacante.nome} realizou um ataque direto! ${oponente.nome} perde ${atacante.ataque} pontos de vida.")
            }

            // Remove o atacante se ele for destruído
            if (atacante.ataque <= 0 || atacante.defesa <= 0) {
                monstrosNoCampo.remove(atacante)
                println("${atacante.nome} foi destruído e removido do campo.")
            }

            // Atualiza a lista de monstros disponíveis para atacar
            monstrosEmAtaque.remove(atacante)

            println("\nVidas:\n$nome: $vida\n${oponente.nome}: ${oponente.vida}")
        }

        // Verifica se o oponente perdeu todos os pontos de vida
        if (!oponente.temVida()) println("${oponente.nome} perdeu todos os pontos de vida! Partida encerrada.")
    }

    /**
     * Permite ao jogador escolher um monstro de uma lista para atacar ou alterar seu estado.
     * Exibe as opções de monstros e valida a escolha do jogador.
     */
    private fun escolherMonstro(lista: List<CartaMonstro>, mensagem: String): CartaMonstro {
        // Exibe a lista de opções de monstros para o jogador
        println(mensagem)
        lista.forEachIndexed { index, monstro ->
            println("Opção ${index + 1}: ${monstro.nome} - A:${monstro.ataque}, D:${monstro.defesa} Estado: ${monstro.estado}")
        }

        // Lê e valida a escolha do jogador
        var escolha: Int
        do {
            print("\nDigite o número do monstro: ")
            escolha = readlnOrNull()?.toIntOrNull() ?: -1
        } while (escolha !in 1..lista.size)
        return lista[escolha - 1] // Retorna o monstro escolhido
    }

    /**
     * Processa o combate entre dois monstros, calculando o dano causado e atualizando os atributos.
     * A função considera o estado do alvo (Ataque ou Defesa) e aplica as regras de combate.
     */
    private fun processarAtaque(atacante: CartaMonstro, alvo: CartaMonstro, oponente: Jogador) {
        // Calcula a diferença de ataque entre o atacante e o alvo (considerando o estado do alvo)
        val diferenca = atacante.ataque - (if (alvo.estado == "Defesa") alvo.defesa else alvo.ataque)

        // Aplica as regras de combate com base na diferença de valores
        when {
            diferenca > 0 -> {
                // O atacante vence, causando dano ao oponente
                println("\n${atacante.nome} destrói ${alvo.nome}! ${oponente.nome} perde $diferenca pontos de vida.")
                oponente.vida -= diferenca

                // Remove o monstro do oponente se ele foi derrotado
                oponente.monstrosNoCampo.remove(alvo)
                println("${alvo.nome} foi destruído e removido do campo de ${oponente.nome}.")
            }

            diferenca < 0 -> {
                // O atacante perde o combate e seu ataque é reduzido
                atacante.ataque += diferenca // Subtrai o valor absoluto da diferença
                println("\n${atacante.nome} não foi forte o suficiente e perdeu ${diferenca} pontos de ataque.")
            }

            else -> {
                // Empate: ambos perdem 10% de seus atributos principais
                atacante.ataque = (atacante.ataque * 0.9).toInt()
                alvo.defesa = (alvo.defesa * 0.9).toInt()
                println("\n${atacante.nome} e ${alvo.nome} empatam! Ambos perdem 10% de ataque e defesa, respectivamente.")
            }
        }

        // Verifica se o monstro atacante foi derrotado e o remove
        if (atacante.ataque <= 0 || atacante.defesa <= 0) {
            monstrosNoCampo.remove(atacante)
            println("${atacante.nome} foi destruído e removido do campo.")
        }

        // Verifica se o monstro do oponente foi derrotado e o remove
        if (alvo.ataque <= 0 || alvo.defesa <= 0) {
            oponente.monstrosNoCampo.remove(alvo)
            println("${alvo.nome} foi destruído e removido do campo de ${oponente.nome}.")
        }
    }

    /**
     * Permite ao jogador alterar o estado de um monstro no campo (Ataque <-> Defesa).
     * O jogador escolhe um monstro e alterna seu estado entre Ataque e Defesa.
     */
    fun alterarEstadoMonstro() {
        if (monstrosNoCampo.isEmpty()) {
            println("${YELLOW}\nNão há monstros no campo para alterar o estado.${RESET}")
            return
        }

        // Jogador escolhe um monstro para alternar o estado (Ataque <-> Defesa)
        val monstro = escolherMonstro(monstrosNoCampo, "\nEscolha um monstro para alterar o estado:")
        monstro.estado = if (monstro.estado == "Ataque") "Defesa" else "Ataque"
        println("${monstro.nome} agora está em posição de ${monstro.estado}.")
    }
}