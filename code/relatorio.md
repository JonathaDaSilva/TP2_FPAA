# Relatório de Implementação — Resolvedor de Tango

Documento de apoio para o grupo. Descreve o que está implementado, como a lógica
funciona, como compilar e executar, exemplos e observações técnicas relevantes
para o relatório final (modelagem, estratégias, complexidade).

Trabalho Prático 2 — Fundamentos de Projeto e Análise de Algoritmos — PUC Minas.

---

## 1. Visão geral

O programa lê a configuração de um tabuleiro de Tango pelo console (dicas iniciais
e restrições `=` / `×`) e o resolve por duas técnicas: **Força Bruta** (busca
exaustiva) e **Backtracking** (busca com poda). Opera em modo texto, sem interface
gráfica, e imprime o tabuleiro inicial e a solução em ASCII.

As cinco regras do Tango:

1. **Preenchimento completo** — toda célula contém um Sol ou uma Lua.
2. **Limite de adjacência** — no máximo 2 símbolos idênticos lado a lado (horizontal/vertical).
3. **Equilíbrio** — cada linha e cada coluna têm a mesma quantidade de Sóis e Luas.
4. **Igualdade (`=`)** — células ligadas por `=` têm o mesmo símbolo.
5. **Oposição (`×`)** — células ligadas por `×` têm símbolos opostos.

---

## 2. Estrutura dos arquivos

```
code/
├── Tango.java                # Ponto de entrada: leitura via console, impressão, medição de tempo
├── Validador.java            # As 5 regras (validação global e validação local/poda)
├── Executor.java             # Contrato comum das estratégias de resolução
├── BacktrackingExecutor.java # Estratégia de Backtracking (busca com poda)
└── ForcaBrutaExecutor.java   # Estratégia de Força Bruta (busca exaustiva)
```

A arquitetura separa deliberadamente a **validação das regras** (`Validador`) da
**mecânica de recursão** (os executores), conforme exige o enunciado. A interface
`Executor` define o contrato comum (`executar(int[][])`), e `Tango` escolhe a
implementação conforme o método pedido pelo usuário, sem conhecer os detalhes de cada
algoritmo. Os dois executores reutilizam o mesmo `Validador`; a diferença está em
*quando* a validação é chamada.

---

## 3. Modelagem do problema

- **Tabuleiro:** matriz `int[][]`.
- **Convenção de valores** (definida em `Tango` e reutilizada pelos demais):
  `VAZIO = -1`, `LUA = 0`, `SOL = 1`.
- **Restrições:** cada restrição liga duas células (no jogo, sempre vizinhas) e é
  representada por um vetor `{linhaA, colA, linhaB, colB}`. Ficam em duas listas
  separadas: `List<int[]> igualdades` (para `=`) e `List<int[]> oposicoes` (para `×`).

A escolha de `int[][]` dá acesso O(1) a cada célula e mantém o código simples. Separar
as restrições em duas listas evita um campo extra de "tipo" e deixa a iteração de cada
regra direta.

---

## 4. Validação das regras — `Validador.java`

A classe oferece **dois níveis** de verificação:

### 4.1. Validação global (tabuleiro completo)

`tabuleiroCompletoValido(int[][] t)` — usada pela Força Bruta e como conferência
final do Backtracking. Verifica que não há células vazias e então aplica:

- `respeitaAdjacencia` — varre a grade e rejeita qualquer trio idêntico consecutivo
  (horizontal e vertical).
- `respeitaEquilibrio` — exige exatamente `n/2` Sóis e `n/2` Luas em cada linha e coluna.
- `respeitaRestricoes` — confere todas as igualdades e oposições.

### 4.2. Validação local / incremental (poda)

`jogadaValida(int[][] t, int linha, int coluna, int valor)` — usada pelo Backtracking.
Simula a jogada, valida apenas o **impacto local** e desfaz:

- `checarAdjacenciaLocal` — verifica as 3 janelas horizontais e 3 verticais que
  contêm a célula recém-preenchida.
- `checarEquilibrioLocal` — exige que a linha e a coluna **não ultrapassem** `n/2`
  de cada símbolo (note o `≤`, não `=`: é uma poda, não a validação final).
- `checarRestricoesLocal` — confere apenas as restrições que tocam a célula, e só
  quando o vizinho já estiver preenchido.

**Ponto-chave:** a versão local usa `≤ n/2` (poda) e a global usa `= n/2` (validação).
Num tabuleiro completo de `n` células por linha, `sol ≤ n/2` e `lua ≤ n/2` com
`sol + lua = n` forçam `sol = lua = n/2`. Por isso a poda local, somada ao tabuleiro
cheio, garante o equilíbrio exato — sem precisar reavaliar a grade inteira a cada passo.

---

## 5. Estratégias de resolução — `Executor` e implementações

Ambas as estratégias implementam a interface `Executor` (método `executar(int[][])`)
e recebem o `Validador` no construtor.

### 5.1. Força Bruta — `ForcaBrutaExecutor`

1. Coleta todas as posições vazias numa lista.
2. Por recursão (`forcaBrutaRecursivo`), atribui SOL e depois LUA a cada vazia,
   gerando todas as `2^k` combinações (`k` = número de vazias).
3. **Só valida no nó folha**, quando o tabuleiro está completo
   (`tabuleiroCompletoValido`). Não há poda durante a construção.
4. Para na primeira solução válida.

Essa ausência de poda durante a construção é exatamente o que caracteriza a busca
exaustiva pura.

### 5.2. Backtracking — `BacktrackingExecutor`

- Percorre as células por **índice linear** `0 .. n²-1`, convertendo com
  `linha = i / n` e `coluna = i % n`.
- **Caso base** (`i == n*n`): retorna `tabuleiroCompletoValido(t)` — validação completa
  única que confirma a solução e protege contra dicas iniciais inconsistentes.
- Se a célula já tem dica, avança para a próxima.
- Caso contrário, tenta SOL e LUA: para cada valor que passa em `jogadaValida` (poda),
  atribui, recursa e, se falhar, **desfaz** (`= VAZIO`) e tenta o outro.

O contraste com a Força Bruta é direto: a poda corta ramos inviáveis *antes* de
expandi-los, evitando a explosão combinatória.

---

## 6. Como compilar e executar

Dentro da pasta `code/`:

```bash
javac *.java
java Tango
```

O programa pergunta interativamente:

1. **Tamanho do tabuleiro (N)** — ex.: `6`.
2. **As N×N células**, linha a linha, usando `-1` (Vazio), `0` (Lua), `1` (Sol).
3. **Quantidade de restrições de igualdade (`=`)** e, em seguida, cada uma no
   formato `linhaA colA linhaB colB`.
4. **Quantidade de restrições de oposição (`×`)** e, em seguida, cada uma no mesmo formato.

Ao final, imprime o tabuleiro inicial (seguido da lista de restrições `=` / `×`),
a solução e o tempo de resolução em milissegundos e nanossegundos.

A entrada é validada à medida que é lida; o programa exibe uma mensagem de erro e
encerra caso encontre: `N` não positivo ou ímpar, valor de célula fora de `-1`/`0`/`1`,
coordenada de restrição fora do tabuleiro, ou um campo que não seja um número inteiro.

> Ao final da leitura, o programa exibe um menu para escolher o método de resolução:
> `[1] Backtracking` ou `[2] Força Bruta`. Para demonstrar a Força Bruta, escolha `2`
> e use uma entrada com **poucas células vazias** (veja a observação de complexidade na
> seção 8).

### Exemplo de entrada (6×6)

```
6
-1 -1  1 -1 -1 -1
-1 -1 -1 -1  0 -1
-1  1 -1 -1 -1 -1
-1 -1 -1 -1 -1 -1
-1  0 -1 -1 -1  1
-1 -1 -1 -1 -1 -1
1
0 0 0 1
1
2 0 2 1
```

(Tabuleiro com dicas, 1 igualdade entre `(0,0)` e `(0,1)`, e 1 oposição entre `(2,0)` e `(2,1)`.)

---

## 7. Exemplo de execução

Saída do Backtracking para a entrada acima (símbolos `S` = Sol, `L` = Lua):

```
=== Tabuleiro inicial ===
. . S . . .
. . . . L .
. S . . . .
. . . . . .
. L . . . S
. . . . . .
Restrições: (0,0)=(0,1) | (2,0)x(2,1) |

=== Resultado ===
L L S S L S
S S L S L L
L S S L S L
S L L S L S
L L S L S S
S S L L S L

Resolvido em: 0,080 ms (80300 ns)
```

A solução respeita as cinco regras: nenhum trio idêntico, 3 Sóis e 3 Luas por linha
e coluna, e as restrições `=` em `(0,0)/(0,1)` e `×` em `(2,0)/(2,1)`.

---

## 8. Análise de complexidade (observações)

- **Força Bruta:** `O(2^k)` combinações, `k` = número de células vazias, e `O(n²)`
  por validação no nó folha. Cresce de forma explosiva.
- **Backtracking:** mesmo pior caso teórico, mas a poda elimina a grande maioria dos
  ramos, tornando a busca viável na prática.

Medições feitas com a lógica do projeto (grade 6×6) ilustram o contraste:

| Cenário                         | Tempo aproximado |
|---------------------------------|------------------|
| Backtracking (grade quase vazia)| sub-milissegundo |
| Força Bruta — 8 vazias          | ~0,2 ms          |
| Força Bruta — 12 vazias         | ~3 ms            |
| Força Bruta — grade cheia (32 vazias) | inviável (2³²) |

Sugestão para o relatório: meça alguns pontos com `System.nanoTime()` (já presente no
`main`, com saída em ms e ns) e monte uma tabela própria, comparando os dois algoritmos
para o mesmo tabuleiro.

---

## 9. Observações técnicas e validação da lógica

- A lógica dos dois algoritmos foi conferida produzindo soluções válidas para
  entradas com e sem restrições `=` / `×`.
- **Safeguard de dicas inconsistentes:** o caso base do Backtracking faz a validação
  completa. Sem isso, uma entrada com dicas iniciais já inválidas (ex.: um trio
  `S S S` numa linha totalmente pré-preenchida, que a poda local não percorre) faria
  o algoritmo retornar uma "solução" inválida. Com a validação no caso base, o
  programa corretamente identifica que não há solução. A Força Bruta já era robusta
  a isso, pois valida o tabuleiro completo sempre.
- Colocar essa validação **no caso base** (e não nos ramos de sucesso) é o que mantém
  a eficiência: uma única validação completa ao encontrar a solução, em vez de uma
  por nível ao desempilhar a recursão.
- **Limite prático:** a Força Bruta só é demonstrável com poucas lacunas; para grades
  muito vazias, use o Backtracking.

---

## 10. Resumo para o grupo

O núcleo está implementado e validado: modelagem em `int[][]`, as 5 regras isoladas
no `Validador` (com versões global e local), e os dois algoritmos nas estratégias
`ForcaBrutaExecutor` e `BacktrackingExecutor`, escolhidas pela interface `Executor`. A
entrada é interativa via console. Falta principalmente: capturar telas de execução
para o relatório, montar a tabela de tempos da análise de complexidade e revisar o
texto das seções da documentação final.
