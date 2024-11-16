import java.io.File
import java.io.IOException

/**
 * Classe que representa um Baralho de cartas, responsável por gerenciar
 * as operações de carregamento, validação e manipulação de cartas.
 */
class Baralho {
    /**
     * Lista de cartas que compõem o baralho.
     */
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
                println("\nErro na leitura do arquivo.")
                null
            } catch (e: NullPointerException) {
                println("\nErro: Arquivo nulo.")
                null
            }
        } else {
            println("\nArquivo não encontrado no caminho especificado: $caminhoArquivo")
        }
        return null
    }

    /**
     * Carrega as cartas a partir de um arquivo CSV, realiza a validação dos dados
     * e armazena as cartas válidas na lista de cartas.
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
                                println("\nErro: Tipo de carta inválido na linha: $linha")
                                null
                            }
                        }
                    } else {
                        // Caso algum campo obrigatório esteja vazio ou inválido
                        println("\nErro: Linha ignorada devido a campos inválidos ou vazios: $linha")
                        null
                    }
                } else {
                    println("\nErro: Linha inválida no arquivo CSV (não possui o número correto de colunas): $linha")
                    null // Retorna null para linhas inválidas
                }
            })

            // Log final com resumo das validações
            println("\nTotal de linhas processadas: $totalLinhas")
            println("Total de cartas válidas: $linhasValidas")
            println("Total de linhas inválidas: ${totalLinhas - linhasValidas}")
        } else {
            println("\nErro: Nenhuma linha encontrada no arquivo ou arquivo vazio.")
        }
    }

    /**
     * Embaralha as cartas do baralho, alterando sua ordem aleatoriamente.
     */
    fun embaralhar() {
        cartas.shuffle()
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
