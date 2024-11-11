import java.io.File
import java.io.IOException

class Baralho {
    val cartas: MutableList<Carta> = mutableListOf()

    // Lê o conteúdo de um arquivo e retorna as linhas como uma lista de strings
    private fun lerArquivo(caminhoArquivo: String): List<String>? {
        val arquivo = File(caminhoArquivo)

        // Verifica se o arquivo existe e é válido antes de tentar lê-lo
        if (arquivo.exists() && arquivo.isFile) {
            return try {
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

    // Carrega as cartas do arquivo CSV e armazena-as na lista de cartas
    fun carregarCartasDoArquivo(caminhoArquivo: String) {
        val linhasDoArquivo = lerArquivo(caminhoArquivo)

        if (!linhasDoArquivo.isNullOrEmpty()) {
            cartas.clear() // Limpa a lista atual de cartas antes de carregar novas
            cartas.addAll(linhasDoArquivo.mapNotNull { linha ->
                val partes = linha.split(";")

                // Verifica se a linha contém as partes necessárias para uma carta válida
                if (partes.size >= 5) {
                    val nome = partes[0]
                    val descricao = partes[1]
                    val ataque = partes[2].toIntOrNull() ?: 0 // Converte ataque para Int, com 0 como valor padrão
                    val defesa = partes[3].toIntOrNull() ?: 0 // Converte defesa para Int, com 0 como valor padrão
                    val tipo = partes[4]

                    // Cria a carta correta com base no tipo
                    when (tipo) {
                        "monstro" -> CartaMonstro(nome, descricao, ataque, defesa)
                        "equipamento" -> CartaEquipamento(nome, descricao, ataque, defesa)
                        else -> null
                    }
                } else {
                    println("\nLinha inválida no arquivo CSV: $linha")
                    null // Retorna null para linhas inválidas
                }
            })
            println("\nCartas carregadas com sucesso! Total: ${cartas.size}")
        } else {
            println("\nErro: Nenhuma linha encontrada no arquivo ou arquivo vazio.")
        }
    }

    // Embaralha as cartas do baralho
    fun embaralhar() {
        cartas.shuffle()
    }

    // Verifica se ainda há cartas no baralho disponíveis para comprar
    fun temCartas(): Boolean{
        return cartas.isNotEmpty()
    }
}
