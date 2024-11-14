class Jogador(
    val nome: String,
) {
    val cartasNaMao: MutableList<Carta> = mutableListOf()
    val monstrosNoCampo: MutableList<CartaMonstro> = mutableListOf()
    var vida: Int = 10000
    val jogadasEscolhidas: MutableList<String> = mutableListOf()

    // Aducionar uma carta na mão do jogador, caso haja espaço
    fun comprarCarta(baralho: MutableList<Carta>) {
        if(baralho.isNotEmpty()){
            if (cartasNaMao.size < 10) {
                cartasNaMao.add(baralho.removeAt(0))
            } else {
                // Não sai do loop até descartar para jogador não ter mais que 10 cartas na mão
                while (cartasNaMao.size == 10) {
                    println("\n$nome atingiu o limite de 10 cartas na mão. Descarte uma carta antes de receber outra.")
                    descartar()
                }
                cartasNaMao.add(baralho.removeAt(0))
            }
        } else{
            println("\nBaralho não possui mais cartas para comprar.")
        }
    }

    // Remover uma carta escolhida da mão do jogador
    fun descartar(){
        if (cartasNaMao.size == 0) {
            println("\n$nome não tem cartas na mão para descartar.")
            return
        }

        println("\nEscolha uma carta para descartar:")
        cartasNaMao.forEachIndexed { index, carta ->
            println("Opção ${index + 1}: $carta")
        }

        // Jogador escolhe uma carta para descartar
        print("\nDigite o número da carta que deseja descartar: ")
        val escolha = readlnOrNull()?.toIntOrNull()

        // Retirar carta da não
        if (escolha != null && escolha in 1..cartasNaMao.size) {
            val cartaDescartada = cartasNaMao.removeAt(escolha - 1)
            println("$nome descartou a carta: ${cartaDescartada.nome}")
        } else {
            // Jogador atual pode tentar descartar de novo se inserir um valor inválido
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
            // Imprime as cartas da mão do jogador no começo de cada rodada
            jogo.mostrarMao(this)

            // Imprime monstros posicionados no tabuleiro do jogador
            mostrarMonstroTabuleiro()

            // Exibe o menu de jogadas
            jogo.imprimirMenuDinamico(this)

            // Solicita a escolha do jogador
            print("\n$nome, digite a opção desejada: ")
            val op = readlnOrNull() ?: ""

            if (op in jogadasEscolhidas) {
                println("Você já escolheu essa opção. Por favor, escolha outra.")

            } else{
                // Guardar a jogada escolhida
                jogadasEscolhidas.add(op)

                // Verifica se o jogador já realizou um ataque e tenta mudar o estado do monstro
                if ("d" in jogadasEscolhidas && op == "e") {
                    println("Não é possível alterar o estado do monstro após atacar.")
                } else {
                    jogo.processarJogadas(this, op)
                }

                // Verificar se a rodada terminou
                if (op == "f") {
                    fimRodada = true
                }
            }

            println("\n----------------------------------------------------------------------")

        } while (!fimRodada)

        limparJogadas() // Limpar as jogadas após a rodada terminar
    }

    // Limpa as jogadas para a próxima rodada
    private fun limparJogadas() {
        jogadasEscolhidas.clear()
    }

    // Exibe monstros posicionados no tabuleiro de um jogador
    fun mostrarMonstroTabuleiro(){
        println("\nMonstros no tabuleiro de $nome")

        if(monstrosNoCampo.isEmpty()){
            println("Nenhum monstro posicionado")
            return
        }

        println("\nNome | Ataque | Defesa | Estado do monstro")
        for (monstro in monstrosNoCampo) {
            println("[ ${monstro.nome} - A:${monstro.ataque}, D:${monstro.defesa} - ${monstro.estado} ]")
        }
        println("---------------------------------------------")
    }

    // Jogador adiciona um monstro no campo e define o seu estado (ataque ou defesa)
    fun posicionarMonstro() {
        if(monstrosNoCampo.size < 5){
            // Filtrar cartas de monstro da mão do jogador
            val cartasMonstro = cartasNaMao.filterIsInstance<CartaMonstro>()

            // Jogador perderá a opção 'a' de posicionar um monstro se não tiver cartas de monstro
            if (cartasMonstro.isEmpty()) {
                println("Você não possui cartas do tipo monstro para posicionar.")
                return
            }

            // Loop enquanto jogador não escolher um monstro para posicionar
            val numCartasMao = cartasNaMao.size
            while (cartasNaMao.size == numCartasMao) {

                // Exibe todas as cartas da mão com índice para seleção
                println("\nEscolha uma carta de monstro para posicionar no tabuleiro:")
                cartasMonstro.forEachIndexed { index, carta ->
                    println("Opção ${index + 1}: ${carta.nome} - ${carta.descricao} - A:${carta.ataque}, D:${carta.defesa}")
                }

                // Obtém a escolha do jogador
                print("\nDigite o número da carta que deseja posicionar: ")
                val escolha = readlnOrNull()?.toIntOrNull()

                if (escolha != null && escolha in 1..cartasMonstro.size) {
                    val cartaEscolhida = cartasMonstro[escolha - 1]

                    // Adiciona a carta de monstro no tabuleiro
                    monstrosNoCampo.add(cartaEscolhida)

                    // Marque a carta como posicionada
                    cartaEscolhida.posicionada = true
                    println("Você posicionou ${cartaEscolhida.nome} como monstro.")

                    // Remove a carta da mão
                    cartasNaMao.remove(cartaEscolhida)

                    // Loop enquanto jogador não escolher o estado do monstro
                    var estadoDefinido = false
                    while (!estadoDefinido) {

                        // Obtém a escolha
                        print("\nDefina o estado do monstro como pocsição de ataque ou defesa (A ou D): ")
                        val estado = readlnOrNull()?.lowercase()

                        // Define o estado e sai do loop
                        when (estado) {
                            "a" -> {
                                cartaEscolhida.estado = "Ataque"
                                estadoDefinido = true
                                println("${cartaEscolhida.nome} foi posicionado em posição de ataque.")
                            }
                            "d" -> {
                                cartaEscolhida.estado = "Defesa"
                                estadoDefinido = true
                                println("${cartaEscolhida.nome} foi posicionado em posição de defesa.")
                            }
                            else -> {
                                println("Escolha inválida. Por favor, escolha 'A' ou 'D'.")
                            }
                        }
                    }
                } else {
                    println("Escolha inválida.")
                }
            }

        } else{
            println("Número máximo de monstros no campo atingido")
        }
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
            print("Escolha o atributo do monstro a ser aumentado pelo equipamento (1 para Ataque e 2 para Defesa):")

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
        val qtdmonstrosEmAtaque = monstrosNoCampo.filter { it.estado ==  "Ataque"}

        // Jogador atual não perde a chance de atacar se não tiver monstros em ataque
        if (qtdmonstrosEmAtaque.isEmpty()){
            println("$nome não tem monstros em posição de ataque para realizar o ataque! Posicione um monstro em estado de ataque ou troque o estado de um dos seus monstros para ataque e tente novamente")
            jogadasEscolhidas.remove("d")
            return
        }

        println("\n$nome ataca ${oponente.nome}")

        for (monstro in qtdmonstrosEmAtaque) {
            println("monstro atacou")
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
