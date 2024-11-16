import java.io.File
import java.io.IOException

/**
 * Classe que representa um Baralho de cartas, responsável por gerenciar as operações de carregamento, validação e manipulação de cartas.
 */
class Baralho {
    val cartas: MutableList<Carta> = mutableListOf()

    /**
     * Lê o conteúdo de um arquivo e retorna suas linhas como uma lista de strings.
     *
     * @param caminhoArquivo Caminho absoluto ou relativo do arquivo a ser lido.
     * @return Lista de linhas do arquivo ou `null` em caso de erro.
     */
    private fun lerArquivo(caminhoArquivo: String): List<String>? {
        val arquivo = File(caminhoArquivo)

        // Verifica se o arquivo existe e é válido antes de tentar lê-lo
        if (arquivo.exists() && arquivo.isFile) {
            return try {
                // Lê e retorna as linhas do arquivo
                arquivo.bufferedReader().readLines()
            } catch (e: IOException) {
                println("\n${RED}Erro na leitura do arquivo.${RESET}")
                null
            } catch (e: NullPointerException) {
                println("\n${RED}Erro: Arquivo nulo.${RESET}")
                null
            }
        } else {
            println("\n${RED}Arquivo não encontrado no caminho especificado: $caminhoArquivo${RESET}")
        }
        return null
    }

    /**
     * Carrega as cartas a partir de um arquivo CSV, realiza a validação dos dados e armazena as cartas válidas na lista de cartas.
     *
     * Cada linha do arquivo deve conter:
     * - Nome da carta
     * - Descrição da carta
     * - Ataque (número inteiro)
     * - Defesa (número inteiro)
     * - Tipo da carta ("monstro" ou "equipamento")
     *
     * @param caminhoArquivo Caminho absoluto ou relativo do arquivo CSV a ser lido.
     */
    fun carregarCartasDoArquivo(caminhoArquivo: String) {
        val linhasDoArquivo = lerArquivo(caminhoArquivo)

        if (!linhasDoArquivo.isNullOrEmpty()) {
            cartas.clear() // Limpa a lista atual de cartas antes de carregar novas

            // Contadores para validação e estatísticas
            var totalLinhas = 0
            var linhasValidas = 0

            cartas.addAll(linhasDoArquivo.mapNotNull { linha ->
                totalLinhas++
                val partes = linha.split(";")

                // Verifica se a linha contém os elementos necessários para criar uma carta
                if (partes.size >= 5) {
                    val nome = partes[0].trim()
                    val descricao = partes[1].trim()
                    val ataque = partes[2].trim().toIntOrNull() // Tenta converter ataque para Int
                    val defesa = partes[3].trim().toIntOrNull() // Tenta converter defesa para Int
                    val tipo = partes[4].trim()

                    // Validação dos campos para garantir que não estejam vazios ou inválidos
                    if (nome.isNotBlank() && descricao.isNotBlank() && ataque != null && defesa != null && tipo.isNotBlank()) {
                        linhasValidas++
                        // Cria a carta adequada com base no tipo especificado
                        when (tipo) {
                            "monstro" -> CartaMonstro(nome, descricao, ataque, defesa)
                            "equipamento" -> CartaEquipamento(nome, descricao, ataque, defesa)
                            else -> {
                                null
                            }
                        }
                    } else {
                        null // Caso algum campo obrigatório esteja vazio ou inválido
                    }
                } else {
                    null // Retorna null para linhas inválidas
                }
            })

            // Resumo das validações
            println("\nTotal de linhas processadas: $totalLinhas")
            println("${GREEN}Total de cartas válidas: $linhasValidas${RESET}")
            println("${YELLOW}Total de linhas inválidas: ${totalLinhas - linhasValidas}${RESET}")
        } else {
            println("\n${RED}Erro: Nenhuma linha encontrada no arquivo ou arquivo vazio.${RESET}")
        }
    }

    /**
     * Embaralha as cartas do baralho, alterando sua ordem aleatoriamente.
     */
    fun embaralhar() {
        cartas.shuffle()
        println("\nAs cartas foram embaralhadas.")
    }

    /**
     * Verifica se o baralho contém cartas disponíveis para serem compradas.
     *
     * @return `true` se houver cartas no baralho, `false` caso contrário.
     */
    fun temCartas(): Boolean {
        return cartas.isNotEmpty()
    }
}
