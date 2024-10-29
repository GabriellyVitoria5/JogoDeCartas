import java.io.File
import java.io.IOException

class Baralho {
    val cartas: MutableList<Carta> = mutableListOf()

    // ler as cartas do baralaho que estão dentro do arquivo
    fun lerArquivo(caminhoArquivo:String):List<String>?{
        val arquivo = File(caminhoArquivo)

        if(arquivo.exists() && arquivo.isFile){
            try {
                return arquivo.bufferedReader().readLines()
            }catch (e: IOException){
                println("Erro na leitura do arquivo")
            }catch (e:NullPointerException){
                println("Erro: arquivo nulo")
            }
        }
        return null
    }

    // criar uma lista de cartas a partir do arquivo CSV
    // FALTA A VERIFICAR SE A LINHA TEM CAMPOS EM BRANCO
    fun carregarCartasDoArquivo(caminhoArquivo: String) {
        val linhasDoArquivo = lerArquivo(caminhoArquivo)

        if (linhasDoArquivo != null) {
            // Limpa a lista de cartas antes de adicionar as novas
            cartas.clear()

            // Adiciona as cartas à lista cartas
            cartas.addAll(linhasDoArquivo.mapNotNull { linha ->
                val partes = linha.split(";")

                // Verifica se a linha tem as informações necessárias
                if (partes.size >= 5) {
                    val nome = partes[0]
                    val descricao = partes[1]
                    val ataque = partes[2].toIntOrNull() ?: 0
                    val defesa = partes[3].toIntOrNull() ?: 0
                    val tipo = partes[4]

                    Carta(nome, descricao, ataque, defesa, tipo)
                } else {
                    null // Se a linha não tiver as informações necessárias, retorna null
                }
            })
        }
    }

    fun embaralhar() {
        cartas.shuffle()
    }
}