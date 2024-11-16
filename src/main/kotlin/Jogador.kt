class Jogador(
    val nome: String,
) {
    val cartasNaMao: MutableList<Carta> = mutableListOf()
    private val monstrosNoCampo: MutableList<CartaMonstro> = mutableListOf()
    var vida: Int = 10000
    val jogadasEscolhidas: MutableList<String> = mutableListOf()

    // Ajudar na validação de escolhas de cartas, dá um índice para cada item da lista e retorna o índice do item escolhido
    private fun escolherCarta(opcoes: List<String>, prompt: String): Int {
        println("\nCartas disponíveis:")
        opcoes.forEachIndexed { index, carta -> println("Opção ${index + 1}: $carta") }
        print("\n$prompt")
        val escolha = readlnOrNull()?.toIntOrNull()
        return escolha?.takeIf { it in 1..opcoes.size }?.minus(1) ?: -1
    }

    // Comprar uma carta do baralho caso haja espaço
    fun comprarCarta(baralho: MutableList<Carta>) {
        if (baralho.isNotEmpty()) {
            while (cartasNaMao.size  == 10) {
                println("\n$nome atingiu o limite de 10 cartas. Descarte uma carta.")
                descartar()
            }
            cartasNaMao.add(baralho.removeAt(0))
        } else{
            println("\nBaralho não possui mais cartas para comprar.")
        }
    }

    // Remover uma carta escolhida da mão do jogador
    fun descartar() {
        if (cartasNaMao.isEmpty()) {
            println("\n$nome não tem cartas na mão para descartar.")
            return
        }

        val escolha = escolherCarta(cartasNaMao.map { it.toString()}, "Escolha uma das cartas para descartar: ")

        // Retirar carta da não
        if (escolha != -1) {
            val cartaDescartada = cartasNaMao.removeAt(escolha)
            println("$nome descartou: ${cartaDescartada.nome}")
        } else {
            // Jogador pode tentar descartar de novo se inserir um valor inválido
            jogadasEscolhidas.remove("c")
            println("Escolha inválida. Não foi possível descartar, tente novamente.")
        }
    }

    // Verificar se o jogador ainda possui pontos de vida
    fun temVida():Boolean{
        return vida > 0
    }

    // Controlar as jogadas escolhidas pelo jogador durante a sua vez
    fun jogar(jogo: Jogo) {
        var fimRodada = false

        do {
            // Menu para de ser mostrado se um jogador estiver sem vida
            if (!jogo.jogadoresTemVida()) return

            // Exibir cartas, monstros no tabuleiro e menu de opções
            mostrarMao()
            mostrarMonstroTabuleiro()
            jogo.imprimirMenuDinamico(this)

            print("\n$nome, digite a opção desejada: ")
            val op = readlnOrNull() ?: ""

            // Jogo processa a jogada e verifica automaticamente se foi repetida ou inválida
            jogo.processarJogadas(this, op)

            // Verificar se a rodada terminou
            if (op == "f") {
                fimRodada = true
            }

            println("\n----------------------------------------------------------------------")

        } while (!fimRodada)

        limparJogadas() // Limpar as jogadas após a rodada terminar
    }

    // Limpa as jogadas escolhidas para a próxima rodada
    private fun limparJogadas() {
        jogadasEscolhidas.clear()
    }

    // Função para mostrar as cartas na mão de um jogador
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

    // Exibe monstros posicionados no tabuleiro de um jogador
    fun mostrarMonstroTabuleiro(){
        println("\nMonstros no tabuleiro de $nome")
        if(monstrosNoCampo.isEmpty()){
            println("Nenhum monstro posicionado")
            return
        }
        println("\nNome | Ataque | Defesa | Estado do monstro")
        monstrosNoCampo.joinToString("\n") { monstro ->
            "[ ${monstro.nome.padEnd(21)} | A:${monstro.ataque.toString().padEnd(6)} | D:${monstro.defesa.toString().padEnd(6)} | ${monstro.estado} ]"
        }.also(::println)
        println("---------------------------------------------")
    }

    // Jogador adiciona um monstro no campo e define o seu estado (ataque ou defesa)
    fun posicionarMonstro() {
        if (monstrosNoCampo.size >= 5) {
            println("Número máximo de monstros no campo atingido.")
            return
        }

        // Filtrar cartas de monstro da mão do jogador
        val cartasMonstro = cartasNaMao.filterIsInstance<CartaMonstro>()

        // Jogador perderá a opção 'a' de posicionar um monstro se não tiver cartas de monstro
        if (cartasMonstro.isEmpty()) {
            println("Você não possui cartas do tipo monstro para posicionar.")
            return
        }

        // Jogador é obrigado a escolher um monstro válido
        var escolha = -1
        while (escolha == -1) {
            escolha = escolherCarta(cartasMonstro.map { it.toString()}, "Escolha uma carta de monstro para posicionar no tabuleiro: ")
        }

        // Adicionar monstro nno campo e remover carta da mão
        val monstroEscolhido = cartasMonstro[escolha]
        monstrosNoCampo.add(monstroEscolhido)
        cartasNaMao.remove(monstroEscolhido)

        // Jogador é obrigado a escolher um estado válido
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
                else -> println("Escolha inválida. Por favor, escolha 'A' ou 'D'.")
            }
        }

        println("${monstroEscolhido.nome} foi posicionado em estado de ${monstroEscolhido.estado}.")
    }

    // Mudar atributos de ataque ou defesa de um monstro usando os valores de um equipamento
    fun equiparMonstro() {
        if (monstrosNoCampo.isEmpty()) {
            println("\nVocê não possui monstros no campo para equipar.")
            return
        }

        // Filtrar cartas de equipamento da mão do jogador
        val cartasEquipamento = cartasNaMao.filterIsInstance<CartaEquipamento>()

        // Jogador perderá a opção 'b' de posicionar um monstro se não tiver cartas de monstro
        if (cartasEquipamento.isEmpty()) {
            println("Você não possui cartas de equipamento na mão.")
            return
        }

        // Jogador é obrigado a escolher um monstro válido
        var escolhaMonstro = -1
        while (escolhaMonstro == -1) {
            escolhaMonstro = escolherCarta(monstrosNoCampo.map { "${it.nome} - A:${it.ataque}, D:${it.defesa}"}, "Escolha um monstro para equipar: ")
        }
        val monstroEscolhido = monstrosNoCampo[escolhaMonstro]

        // Jogador é obrigado a escolher um equipamento válido
        var escolhaEquipamento = -1
        while (escolhaEquipamento == -1) {
            escolhaEquipamento = escolherCarta(cartasEquipamento.map { "${it.nome} - ${it.descricao} - A: ${it.ataque}, D: ${it.defesa}"}, "Escolha uma carta de equipamento para usar em ${monstroEscolhido.nome}: ")
        }
        val equipamentoEscolhido = cartasEquipamento[escolhaEquipamento]

        // Jogador pe obridado a escolher um atributo válido a ser aumentado (ataque ou defesa)
        while (true) {
            print("Escolha o atributo do monstro a ser aumentado pelo equipamento (A - Ataque, D - Defesa): ")
            when (readlnOrNull()?.lowercase()) {
                "a" -> {
                    monstroEscolhido.ataque += equipamentoEscolhido.ataque
                    println("\n${monstroEscolhido.nome} foi equipado com ${equipamentoEscolhido.nome}, recebendo +${equipamentoEscolhido.ataque} de ataque.")
                    break
                }
                "b" -> {
                    monstroEscolhido.defesa += equipamentoEscolhido.defesa
                    println("\n${monstroEscolhido.nome} foi equipado com ${equipamentoEscolhido.nome}, recebendo +${equipamentoEscolhido.defesa} de defesa.")
                    break
                }
                else -> println("Escolha inválida. Por favor, escolha 'A' ou 'D'.")
            }
        }

        // Remove o equipamento da mão do jogador
        cartasNaMao.remove(equipamentoEscolhido)
    }

    fun atacarOponente(oponente: Jogador) {
        // Filtra os monstros do jogador atual que estão em posição de ataque
        val monstrosEmAtaque = monstrosNoCampo.filter { it.estado == "Ataque" }.toMutableList()

        // Se não houver monstros em posição de ataque, a jogada é invalidada
        if (monstrosEmAtaque.isEmpty()) {
            println("$nome não tem monstros em posição de ataque para realizar o ataque!")
            jogadasEscolhidas.remove("d") // Remove a opção de ataque da lista de jogadas disponíveis
            return
        }

        // Loop enquanto houver monstros em ataque e o oponente ainda tiver pontos de vida
        while (monstrosEmAtaque.isNotEmpty() && oponente.temVida()) {
            // Escolhe um monstro atacante
            val atacante = escolherMonstro(monstrosEmAtaque, "Escolha um monstro para atacar:")

            // Determina o alvo: se o oponente tiver monstros, escolhe um deles; caso contrário, será ataque direto
            val alvo = if (oponente.monstrosNoCampo.isNotEmpty()) {
                escolherMonstro(oponente.monstrosNoCampo, "Escolha um monstro do oponente para atacar:")
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
        }

        // Verifica se o oponente perdeu todos os pontos de vida
        if (!oponente.temVida()) println("${oponente.nome} perdeu todos os pontos de vida! Partida encerrada.")
    }

    private fun escolherMonstro(lista: List<CartaMonstro>, mensagem: String): CartaMonstro {
        // Exibe a lista de opções de monstros para o jogador
        println(mensagem)
        lista.forEachIndexed { index, monstro ->
            println("Opção ${index + 1}: ${monstro.nome} - A:${monstro.ataque}, D:${monstro.defesa} Estado: ${monstro.estado}")
        }

        // Lê e valida a escolha do jogador
        var escolha: Int
        do {
            print("Digite o número do monstro: ")
            escolha = readlnOrNull()?.toIntOrNull() ?: -1
        } while (escolha !in 1..lista.size)
        return lista[escolha - 1] // Retorna o monstro escolhido
    }

    private fun processarAtaque(atacante: CartaMonstro, alvo: CartaMonstro, oponente: Jogador) {
        // Calcula a diferença de ataque entre o atacante e o alvo (considerando o estado do alvo)
        val diferenca = atacante.ataque - (if (alvo.estado == "Defesa") alvo.defesa else alvo.ataque)

        // Aplica as regras de combate com base na diferença de valores
        when {
            diferenca > 0 -> {
                // O atacante vence, causando dano ao oponente
                println("${atacante.nome} destrói ${alvo.nome}! ${oponente.nome} perde $diferenca pontos de vida.")
                oponente.vida -= diferenca

                // Remove o monstro do oponente se ele foi derrotado
                oponente.monstrosNoCampo.remove(alvo)
                println("${alvo.nome} foi destruído e removido do campo de ${oponente.nome}.")
            }

            diferenca < 0 -> {
                // O atacante perde o combate e seu ataque é reduzido
                atacante.ataque += diferenca // Subtrai o valor absoluto da diferença
                println("${atacante.nome} não foi forte o suficiente e perdeu ${-diferenca} pontos de ataque.")
            }

            else -> {
                // Empate: ambos perdem 10% de seus atributos principais
                atacante.ataque = (atacante.ataque * 0.9).toInt()
                alvo.defesa = (alvo.defesa * 0.9).toInt()
                println("${atacante.nome} e ${alvo.nome} empatam! Ambos perdem 10% de ataque e defesa, respectivamente.")
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

    fun alterarEstadoMonstro() {
        // Verifica se há monstros no campo para alterar estado
        if (monstrosNoCampo.isEmpty()) {
            println("\nNão há monstros no campo para alterar o estado.")
            return
        }

        // Permite ao jogador escolher um monstro e alternar seu estado (Ataque <-> Defesa)
        val monstro = escolherMonstro(monstrosNoCampo, "Escolha um monstro para alterar o estado:")
        monstro.estado = if (monstro.estado == "Ataque") "Defesa" else "Ataque"
        println("${monstro.nome} agora está em posição de ${monstro.estado}.")
    }
}