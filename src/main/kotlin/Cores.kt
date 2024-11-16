/**
 * Códigos ANSI para adicionar cores no menu e em mensagens no terminal.
 * Utilizados para destacar informações de diferentes jogadores e estados do jogo.
 */
const val RESET = "\u001B[0m"   // Reseta a cor para o padrão
const val RED = "\u001B[31m"    // Mensagens de erro
const val CYAN = "\u001B[36m"   // Cor para jogador 1
const val MAGENTA = "\u001B[35m" // Cor para jogador 2
const val GREEN = "\u001B[32m"  // Destacar informações gerais
const val YELLOW = "\u001B[33m" // Informações de jogo e destaque