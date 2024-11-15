class Jogador(
    val nome: String,
) {
    val cartasNaMao: MutableList<Carta> = mutableListOf()
    val monstrosNoCampo: MutableList<CartaMonstro> = mutableListOf()
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

        // Exibe cartas da mão que podem ser usadas como equipamento
        val cartasEquipamento = cartasNaMao.filterIsInstance<CartaEquipamento>()

        // Jogador perderá a opção 'b' de posicionar um monstro se não tiver cartas de monstro
        if (cartasEquipamento.isEmpty()) {
            println("Você não possui cartas de equipamento na mão.")
            return
        }

        // Exibe monstros no campo para o jogador escolher
        println("\nEscolha um monstro para equipar:")
        monstrosNoCampo.forEachIndexed { index, monstro ->
            println("Opção ${index + 1}: ${monstro.nome} - A:${monstro.ataque}, D:${monstro.defesa}")
        }

        // Jogador escolhe um monstro para equipar
        // Loop enquanto jogador não escolher um monstro válido
        var monstroEscolhido: CartaMonstro? = null
        while (monstroEscolhido == null) {

            // Obtém a escolha do jogador
            print("\nDigite o número do monstro que deseja alterar o estado: ")
            val escolha = readlnOrNull()?.toIntOrNull()

            if (escolha != null && escolha in 1..monstrosNoCampo.size) {
                monstroEscolhido = monstrosNoCampo[escolha - 1]
            } else {
                println("Escolha inválida. Tente novamente.")
            }
        }

        // Exibe cartas de equipamento disponíveis para usar
        println("\nEscolha uma carta de equipamento para usar:")
        cartasEquipamento.forEachIndexed { index, equipamento ->
            println("Opção ${index + 1}: ${equipamento.nome} - ${equipamento.descricao} - A: ${equipamento.ataque}, D: ${equipamento.defesa})")
        }

        // Loop enquanto o jogador não escolher uma carta de equipamento válido
        var equipamentoEscolhido: CartaEquipamento? = null
        while (equipamentoEscolhido == null) {

            // Obtém a escolha do jogador
            print("\nDigite o número da carta de equipamento que deseja usar: ")
            val escolhaEquipamento = readlnOrNull()?.toIntOrNull()

            if (escolhaEquipamento != null && escolhaEquipamento in 1..cartasEquipamento.size) {
                equipamentoEscolhido = cartasEquipamento[escolhaEquipamento - 1]
            } else {
                println("Escolha inválida. Tente novamente.")
            }
        }

        // Pergunta ao jogador se deseja aplicar o bônus no ataque ou na defesa
        var escolhaAtributo: String? = null
        while (escolhaAtributo == null) {
            print("Escolha o atributo do monstro a ser aumentado pelo equipamento (1 para Ataque e 2 para Defesa): ")

            val escolha = readlnOrNull()?.toIntOrNull()
            when (escolha) {
                1 -> {
                    monstroEscolhido.ataque += equipamentoEscolhido.ataque
                    println("${monstroEscolhido.nome} foi equipado com ${equipamentoEscolhido.nome}, recebendo +${equipamentoEscolhido.ataque} de ataque")
                    escolhaAtributo = "Ataque"
                }
                2 -> {
                    monstroEscolhido.defesa += equipamentoEscolhido.defesa
                    println("${monstroEscolhido.nome} foi equipado com ${equipamentoEscolhido.nome}, recebendo +${equipamentoEscolhido.defesa} de defesa")
                    escolhaAtributo = "Defesa"
                }
                else -> println("Escolha inválida. Tente novamente.")
            }
        }

        // Remove o equipamento da mão do jogador
        cartasNaMao.remove(equipamentoEscolhido)

    }


    // Monstros de um jogador realizam ataques contra monstros do oponente
    fun atacarOponente(oponente: Jogador) {

        // Filtra os monstros do jogador atual que está em estado de ataque
        val monstrosEmAtaque = monstrosNoCampo.filter { it.estado == "Ataque" }.toMutableList()

        // Jogador atual não perde a chance de atacar se não tiver monstros em ataque
        if (monstrosEmAtaque.isEmpty()){
            println("$nome não tem monstros em posição de ataque para realizar o ataque! Posicione um monstro em estado de ataque ou troque o estado de um dos seus monstros para ataque e tente novamente")
            jogadasEscolhidas.remove("d")
            return
        }

        // Jogador pode atacar com todos os monstros em estado de ataque
        while(monstrosEmAtaque.isNotEmpty()){

            // Loop de ataque termina quando oponente perde toda a vida
            if(oponente.temVida()){

                // ** Regras de ataque **
                //
                // Oponente tem monstros no campo:
                // - Ataque x Ataque
                //      .oponente tem menos ataque: monstro do oponente morre e a diferença de pontos de ataque entre eles é retirada da vida do oponente
                //      .oponente tem mais ataque: monstro do jogador atual perde pontos de ataque com base na diferença de pontos de ataque entre eles
                //      .oponente tem ataque igual: valor da defesa irá desempatar, o monstro com menos defesa perderá 10% dos seus pontos de ataque, mas se defesa também for igual, nada acontece
                //          OBS: se o monstro do jogador chegar em 0 de ataque ao atacar um monstro de ataque alto, o monstro morre, mas o jogador atual não perde vida
                // - Ataque x Defesa
                //      .oponente tem menos defesa que o ataque do atacante: monstro do oponente morre e a diferença de pontos entre ataque do atacante e defesa do defensor é retirada da vida do oponente
                //      .oponente tem mais defesa que o ataque do atacante: monstro do jogador atual perde pontos de ataque com base na diferença de pontos do ataque do atacante e defesa do defensor
                //      .oponente tem defesa igual ao ataque do atacante: monstro atacante perde 10% de pontos de ataque, e monstro defensor perde 10% de pontos de defesa
                //          OBS: se o monstro do jogador chegar em 0 de ataque ao atacar um monstro de defesa alta, o monstro morre, mas o jogador atual não perde vida
                //
                // Oponente não tem monstros no campo:
                // - Ataque direto: oponente perde em vida a quantidade de pontos ataque que o monstro do jogador atual tem
                //
                // OBS: monstro morre se seu ataque ou defesa chegar a 0

                // Jogador atual escolhe um dos seus monstros para atacar
                println("\nEscolha um monstro para atacar o oponente:")
                for ((index, monstro) in monstrosEmAtaque.withIndex()) {
                    println("Opção ${index + 1}: ${monstro.nome} - A:${monstro.ataque}, D:${monstro.defesa}")
                }

                // Garantir que o jogador escolha um monstro válido
                var monstroAtacanteEscolhido: CartaMonstro? = null
                while (monstroAtacanteEscolhido == null) {

                    print("\nDigite o número do monstro que deseja usar para atacar: ")
                    val escolha = readlnOrNull()?.toIntOrNull()

                    if (escolha != null && escolha in 1..monstrosEmAtaque.size) {
                        monstroAtacanteEscolhido = monstrosEmAtaque[escolha - 1]
                    } else {
                        println("Escolha inválida. Tente novamente.")
                    }
                }

                // Escolher um dos monstros do oponente para atacar se ele tiver monstros no campo
                if (oponente.monstrosNoCampo.isNotEmpty()) {

                    // Mostra os monstros em campo do oponente
                    println("\n${oponente.nome} tem os seguintes monstros no campo:")
                    for ((index, monstroOponente) in oponente.monstrosNoCampo.withIndex()) {
                        println("Opção ${index + 1}: ${monstroOponente.nome} - A: ${monstroOponente.ataque}, D: ${monstroOponente.defesa}, Estado: ${monstroOponente.estado})")
                    }

                    // Escolher do oponente para receber o ataque
                    var monstroOponente: CartaMonstro? = null
                    while (monstroOponente == null) {
                        print("\nEscolha o número do monstro do oponente para atacar: ")
                        val escolhaMonstroOponente = readlnOrNull()?.toIntOrNull()

                        if (escolhaMonstroOponente != null && escolhaMonstroOponente in 1..oponente.monstrosNoCampo.size) {
                            monstroOponente = oponente.monstrosNoCampo[escolhaMonstroOponente - 1]
                        } else {
                            println("Escolha inválida! Por favor, escolha um número válido.")
                        }
                    }

                    // Implementando as regras do ataque
                    print("\n${monstroAtacanteEscolhido.nome} ataca ${monstroOponente.nome}!!")

                    // Ataque X Ataque
                    if(monstroOponente.estado == "Ataque"){

                        // Monstro do jogador atual derrota monstro do oponente
                        if (monstroAtacanteEscolhido.ataque > monstroOponente.ataque) {
                            val diferenca = monstroAtacanteEscolhido.ataque - monstroOponente.ataque
                            oponente.monstrosNoCampo.remove(monstroOponente)
                            oponente.vida -= diferenca
                            println("\nO ataque foi um sucesso! ${monstroOponente.nome} foi destruído! ${oponente.nome} perde $diferenca pontos de vida durante o ataque.")
                        } else if (monstroAtacanteEscolhido.ataque < monstroOponente.ataque) {

                            // Monstro do jogador atual perde pontos de ataque
                            val diferenca = monstroOponente.ataque - monstroAtacanteEscolhido.ataque
                            monstroAtacanteEscolhido.ataque -= diferenca
                            println("\nO ataque falhou, pois seu monstro não é forte o suficiente para superar o ataque de ${monstroOponente.nome}. Seu monstro perde $diferenca pontos de ataque ao receber um golpe de ${monstroOponente.nome}!")
                        } else {

                            // Defesa irá desemparar ou a luta não resultará em nada
                            val ataquePerdidoCalculado = (monstroOponente.ataque * 0.1).toInt()
                            if (monstroAtacanteEscolhido.defesa > monstroOponente.defesa) {
                                monstroOponente.ataque -= ataquePerdidoCalculado
                                println("\nOs dois monstros são igualmente fortes! Mas seu monstro ${monstroAtacanteEscolhido.nome} tinha mais defesa para resistir a luta e consegue tirar $ataquePerdidoCalculado pontos de ataque de ataque")
                            } else if(monstroAtacanteEscolhido.defesa < monstroOponente.defesa) {
                                monstroAtacanteEscolhido.ataque -= ataquePerdidoCalculado
                                println("\nOs dois monstros são igualmente fortes! Mas ${monstroOponente.nome} tinha mais defesa para resistir ao ataque e conseguiu tirar $ataquePerdidoCalculado pontos de ataque do seu monstro ${monstroAtacanteEscolhido.nome}!")
                            }
                            else{
                                println("\nOs dois monstros são igualmente fortes! Suas defesas também são as mesmas! O ataque não surtiu efeito em nenhum dos lados.")
                            }
                        }

                    }
                    // Ataque X Defesa
                    else if (monstroOponente.estado == "Defesa") {

                        // Monstro do jogador atual derrota monstro do oponente
                        if (monstroAtacanteEscolhido.ataque > monstroOponente.defesa) {
                            val diferenca = monstroAtacanteEscolhido.ataque - monstroOponente.defesa
                            oponente.monstrosNoCampo.remove(monstroOponente)
                            oponente.vida -= diferenca
                            println("\n O ataque de ${monstroOponente.nome} superou as defesas do monstro, e ${monstroOponente.nome} foi destruído! Oponente perde $diferenca pontos de vida durante o ataque.")
                        } else if (monstroAtacanteEscolhido.ataque < monstroOponente.defesa) {

                            // Monstro do jogador atual perde pontos de ataque
                            val diferenca = monstroOponente.defesa - monstroAtacanteEscolhido.ataque
                            monstroAtacanteEscolhido.ataque -= diferenca
                            println("\nSeu monstro ${monstroAtacanteEscolhido.nome} não é forte o suficiente para superar as defesas de ${monstroOponente.nome} e perde $diferenca pontos de ataque durante a luta!")
                        } else {
                            // Monstro do jogador atual perde 10% pontos de ataque, e monstro do oponente perde 10% pontos de defesa
                            val ataquePerdidoCalculado = (monstroAtacanteEscolhido.ataque * 0.1).toInt()
                            val defesaPerdidaCalculada = (monstroOponente.defesa * 0.1).toInt()
                            monstroAtacanteEscolhido.ataque -= ataquePerdidoCalculado
                            monstroOponente.defesa -= defesaPerdidaCalculada
                            println("\nO ataque do seu monstro ${monstroAtacanteEscolhido.nome} é igual à defesa de ${monstroOponente.nome}! Durante a luta, seu monstro perde $ataquePerdidoCalculado pontos de ataque, e monstro do oponente perde pontos de defesa!")
                        }
                    }

                    // Verificar se algum monstro perdeu todos os pontos de ataque ou defesa para retirar do campo
                    if (monstroAtacanteEscolhido.ataque <= 0 || monstroAtacanteEscolhido.defesa <= 0){
                        println("\nSeu monstro sofreu graves ferimentos durante a luta e não conseguiu sobreviver. ${monstroAtacanteEscolhido.nome} será removido do campo.")
                        monstrosNoCampo.remove(monstroAtacanteEscolhido)
                    }
                    if (monstroOponente.ataque <= 0 || monstroOponente.defesa <= 0){
                        println("\nO monstro sofreu graves ferimentos durante a luta e não conseguiu sobreviver. ${monstroOponente.nome} será removido do campo.")
                    }

                } else {

                    // Jogador atual realizar um ataque direto ao oponente
                    println("${monstroAtacanteEscolhido.nome} realiza um ataque direto com ${monstroAtacanteEscolhido.ataque} pontos de ataque!")
                    oponente.vida -= monstroAtacanteEscolhido.ataque
                    println("${oponente.nome} perde ${monstroAtacanteEscolhido.ataque} pontos de vida.")
                }


                // Remove o monstro da lista para o jogador não poder atacar com ele novamente
                monstrosEmAtaque.remove(monstroAtacanteEscolhido)

            }
            else{
                println("${oponente.nome} perdeu todos os pontos de vida! Partida encerrada.")
                return
            }

        }
    }

    // Altera o estado de um monstro escolhido
    fun alterarEstadoMonstro(){
        if (monstrosNoCampo.isEmpty()) {
            println("\nNão há monstros no campo para alterar o estado.")
            return
        }

        // Exibe monstros no campo com seus respectivos estados
        println("\nEscolha um monstro para alterar o estado:")
        monstrosNoCampo.forEachIndexed { index, monstro ->
            println("Opção ${index + 1}: ${monstro.nome} - A:${monstro.ataque}, D:${monstro.defesa} Estado atual: - ${monstro.estado}")
        }

        // Loop enquanto jogador não escolher um monstro válido
        var monstroEscolhido: CartaMonstro? = null
        while (monstroEscolhido == null) {

            // Obtém a escolha do jogador
            print("\nDigite o número do monstro que deseja alterar o estado: ")
            val escolha = readlnOrNull()?.toIntOrNull()

            if (escolha != null && escolha in 1..monstrosNoCampo.size) {
                monstroEscolhido = monstrosNoCampo[escolha - 1]
            } else {
                println("Escolha inválida. Tente novamente.")
            }
        }

        // Pergunta o novo estado do monstro
        var estadoAlterado = false
        while (!estadoAlterado) {
            print("\nDeseja mudar o estado para ataque ou defesa? (A/D): ")
            val novoEstado = readlnOrNull()?.lowercase()

            when (novoEstado) {
                "a" -> {
                    monstroEscolhido.estado = "Ataque"
                    estadoAlterado = true
                    println("${monstroEscolhido.nome} agora está em posição de ataque.")
                }
                "d" -> {
                    monstroEscolhido.estado = "Defesa"
                    estadoAlterado = true
                    println("${monstroEscolhido.nome} agora está em posição de defesa.")
                }
                else -> {
                    println("Escolha inválida. Por favor, escolha 'A' ou 'D'.")
                }
            }
        }
    }
}