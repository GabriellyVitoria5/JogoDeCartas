# Batalha de Cartas Colecionáveis do Seu José

## Integrantes

- Gabrielle Fonseca;
- Gabrielly Vitória.

# Manual do Jogo

**Número de jogadores:** 2

## Introdução
Neste jogo de cartas colecionáveis, inspirado em títulos como *Magic: The Gathering* e *Yu-Gi-Oh*, cada jogador compartilha de um mesmo baralho com cartas representando monstros ou equipamentos, e os utiliza para batalhar com o objetivo de derrotar o oponente. O jogo foca em estratégia, onde monstros podem ser posicionados em um tabuleiro e equipados com itens, permitindo ataques entre os adversários. O primeiro jogador a reduzir os pontos de vida do oponente a zero ou que tenha mais pontos ao final do baralho vence.

## Objetivo do jogo
Reduzir os pontos de vida do oponente a zero atacando com monstros ou causar mais dano que ele até que o baralho se esgote.

## Componentes

- **Cartas:** As cartas do jogo são separadas em cartas de monstros e cartas de equipamento, contendo as seguintes características: nome, descrição, valor do ataque, valor da defesa e tipo da carta. Elas podem ser encontradas no arquivo `cartas.csv` com um total de 224 cartas. Por conta disso, os jogadores podem criar o seu próprio baralho personalizado, desde que não adicionem cartas de novos tipos e informem a quantidade e tipo dos valores das características mencionados antes corretamente, adicionando as cartas corretas no arquivo indicado.
  
- **Tabuleiro:** Cada jogador pode posicionar até 5 monstros no tabuleiro.

## Preparação do jogo

1. O baralho deve ser embaralhado, e então cada jogador vai receber 5 cartas aleatórias para formar sua mão inicial.
2. Os dois jogadores começam com 10.000 pontos de vida.
3. Escolha aleatoriamente quem começará a jogar. A partida será jogada em turnos alternados, um jogador precisa terminar suas jogadas e passar a vez para dar início ao turno do próximo jogador, e assim por diante.

## Como jogar

O jogo é dividido em **rodadas**, onde cada jogador realiza as ações possíveis e tenta causar dano ao oponente. Uma rodada se estrutura da seguinte forma:

## **1. Compra de Cartas:**
- No início de cada rodada, o jogador compra 1 carta do baralho.
- Caso um jogador tenha **mais de 10 cartas na mão**, ele deverá descartar cartas até atingir o limite de 10.
- Se o jogador estiver sem cartas na mão, no começo do seu turno ele deve receber 5 cartas

## **2. Ações do Jogador**
Durante o seu turno, o jogador pode realizar as seguintes ações, uma vez por rodada:

### **a) Pocionar Monstros no Tabuleiro**
O jogador pode colocar um monstro da sua mão no tabuleiro, escolhendo seu estado de ataque ou defesa. Cada jogador pode ter até **5 monstros** no tabuleiro ao mesmo tempo.

- Monstros em ataque podem atacar no próximo turno.
- Monstros em defesa não podem atacar, mas podem bloquear ataques inimigos.
- Caso o limite de 5 monstros seja atingido, novos monstros só podem ser posicionados quando algum for destruído.

### b) Equipar Monstros
O jogador pode equipar **1 monstro** com 1 carta de equipamento, que aumenta um de seus atributos, **ataque ou defesa**. Apenas um equipamento pode ser usado por rodada.

### c) Descartar Cartas
O jogador pode optar por descartar **1 carta** da mão para se reorganizar ou preparar uma nova estratégia.

### d) Realizar Ataques
A partir da **segunda rodada**, o jogador pode atacar com **todos os monstros em posição de ataque que estão no tabuleiro**, mas cada um só pode atacar **uma vez**. O jogador deve escolher 1 monstro em estado de ataque para atacar um monstro do oponente.

### e) Alterar o Estado de Monstros
O jogador pode mudar o estado de um monstro de ataque para defesa ou vice-versa. Um monstro que tenha atacado não pode alterar seu estado na mesma rodada.

## Regras de ataque

### 1. Oponente tem monstros no campo

#### a) Ataque x Ataque

1. **Oponente tem menos ataque**:
   - O monstro do oponente é destruído.
   - A **diferença de pontos de ataque** entre os monstros é subtraída dos pontos de vida do oponente.

2. **Oponente tem mais ataque**:
   - O monstro do jogador atual **perde pontos de ataque** com base na **diferença de ataque** entre os dois monstros.

3. **Oponente tem ataque igual**:
   - O valor da **defesa** de cada monstro desempatará o confronto.
     - O monstro com menos defesa **perde 10% de seus pontos de ataque**.
     - **Se ambos tiverem a mesma defesa**, nada acontece.

#### b) Ataque x Defesa

1. **Oponente tem menos defesa**:
   - O monstro do oponente é destruído.
   - A **diferença entre o ataque do atacante e a defesa do defensor** é subtraída dos pontos de vida do oponente.

2. **Oponente tem mais defesa**:
   - O monstro atacante **perde pontos de ataque** com base na **diferença entre o ataque do atacante e a defesa do defensor**.

3. **Oponente tem defesa igual ao ataque do atacante**:
   - O monstro atacante **perde 10% de seus pontos de ataque**.
   - O monstro defensor **perde 10% de seus pontos de defesa**.

### 2. Oponente não tem monstros no campo

- **Ataque Direto**:
  - Se o oponente não tiver monstros no campo, o jogador pode realizar um **ataque direto**.
  - O oponente perde pontos de vida equivalentes ao **valor de ataque do monstro atacante**.

### 3. Considerações Importantes

- **Monstros são destruídos** se seus pontos de ataque ou defesa chegarem a **0**.
- Se o ataque do **monstro atacante** chegar a 0 devido à perda de pontos de ataque na letra a) item 2 e 3 ou letra b) item 2 e 3, o monstro é destruído, mas o jogador **não** perde pontos de vida.

## Condições de Vitória

O jogo termina quando:

- **Um jogador atinge 0 pontos de vida**, declarando o outro jogador como vencedor.

- **O baralho fica sem cartas**: Se nenhum dos jogadores atingir 0 pontos de vida, vence o jogador com mais pontos ao término da rodada quando as cartas do baralho acabaram.
