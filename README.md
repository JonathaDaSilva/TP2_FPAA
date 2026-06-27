# Resolvedor de Quebra-Cabeça Tango

Trabalho Prático 2 da disciplina **Fundamentos de Projeto e Análise de Algoritmos** — Curso de Engenharia de Software, PUC Minas (2026/1).

Programa de console, em Java, que resolve um tabuleiro de **Tango** aplicando duas técnicas de busca em espaço de estados: **Força Bruta** (busca exaustiva) e **Backtracking** (busca com poda). O trabalho integra-se ao estudo de Problemas de Satisfação de Restrições (CSPs).

## O problema

O Tango é um quebra-cabeça de dedução lógica jogado em uma grade de tamanho par (por exemplo 6×6 ou 8×8) preenchida com apenas dois símbolos (Sol e Lua). Uma configuração válida deve respeitar cinco regras:

1. **Preenchimento completo** — toda célula contém um Sol ou uma Lua.
2. **Limite de adjacência** — no máximo 2 símbolos idênticos lado a lado (horizontal ou vertical).
3. **Equilíbrio** — cada linha e cada coluna contém a mesma quantidade de Sóis e de Luas.
4. **Restrição de igualdade (`=`)** — células ligadas por `=` devem conter o mesmo símbolo.
5. **Restrição de oposição (`×`)** — células ligadas por `×` devem conter símbolos opostos.

## Estratégias de resolução

A **Força Bruta** gera o espaço completo de combinações de preenchimento e valida cada configuração contra as cinco regras apenas quando o tabuleiro está completo, evidenciando o problema da explosão combinatória.

O **Backtracking** constrói a solução de forma incremental, célula a célula, e aplica as regras como critérios de poda: assim que uma jogada viola uma restrição, o ramo é descartado antes de ser explorado, reduzindo drasticamente o espaço de busca.

## Estrutura do projeto

Todo o código-fonte fica na pasta `code/`:

```
code/
├── Tango.java                # main: lê a entrada via console, imprime e mede o tempo
├── Validador.java            # as 5 regras: validação total e validação incremental (poda)
├── Executor.java             # contrato comum das estratégias de resolução
├── BacktrackingExecutor.java # estratégia de Backtracking (busca com poda)
├── ForcaBrutaExecutor.java   # estratégia de Força Bruta (busca exaustiva)
└── scripts/                  # arquivos de entrada prontos para teste (ver abaixo)
```

A representação é enxuta: a grade é um `int[][]` (`1` = Sol, `0` = Lua, `-1` = vazio) e as restrições `=` e `×` são listas de coordenadas. A configuração do tabuleiro é informada pelo usuário no console (ou redirecionada de um arquivo de entrada).

O projeto separa a **lógica de validação das regras** (`Validador`) da **mecânica de recursão** (os executores). A interface `Executor` define o contrato comum (`executar(int[][])`), e a classe `Tango` escolhe a implementação conforme o método pedido pelo usuário. Os dois executores reutilizam o mesmo `Validador`; a diferença está em *quando* a validação é invocada — a Força Bruta valida apenas o tabuleiro completo, enquanto o Backtracking valida cada jogada incrementalmente para podar a busca.

## Compilação e execução

A partir da pasta `code/`:

```bash
cd code
javac *.java
java Tango
```

O programa é **interativo** e pergunta, nesta ordem:

1. **Tamanho do tabuleiro (N)** — inteiro positivo e par.
2. **As N×N células**, linha a linha, usando `-1` (Vazio), `0` (Lua), `1` (Sol).
3. **Quantidade de restrições de igualdade (`=`)** e, em seguida, cada uma no formato `linhaA colA linhaB colB`.
4. **Quantidade de restrições de oposição (`×`)** e, em seguida, cada uma no mesmo formato.
5. **Método de resolução** — `[1] Backtracking` ou `[2] Força Bruta`.

Ao final, imprime no terminal a configuração inicial (com lacunas e a lista de restrições), o tabuleiro final resolvido em formato ASCII e o tempo de resolução (em milissegundos e nanossegundos).

A entrada é validada durante a leitura: o programa exibe uma mensagem de erro e encerra caso `N` seja não positivo ou ímpar, uma célula tenha valor fora de `-1`/`0`/`1`, uma coordenada de restrição fique fora do tabuleiro, ou um campo não seja um número inteiro.

## Exemplos prontos (`code/scripts/`)

A pasta `code/scripts/` contém arquivos de entrada já formatados para alimentar o programa via redirecionamento, sem precisar digitar tudo manualmente. A última linha de cada arquivo já indica o método de resolução.

| Arquivo                     | Tabuleiro | Método       |
|-----------------------------|-----------|--------------|
| `caso1_backtracking.txt`    | 6×6       | Backtracking |
| `caso1_forcabruta.txt`      | 6×6 (poucas lacunas) | Força Bruta |
| `caso2_backtracking.txt`    | 6×6       | Backtracking |
| `caso3_backtracking.txt`    | 8×8       | Backtracking |

Para executar um exemplo, redirecione o arquivo para a entrada padrão:

```bash
java Tango < scripts/caso1_backtracking.txt
```

> Observação: a Força Bruta só é prática com poucas lacunas (como em `caso1_forcabruta.txt`), pois o número de combinações dobra a cada célula vazia. Para tabuleiros muito vazios, use o Backtracking.

## Tecnologias

- Linguagem: **Java** (sem bibliotecas de terceiros para resolução de CSPs ou solvers prontos).
- Execução em console, sem interface gráfica.

## Licença

Distribuído sob os termos do arquivo [LICENSE](LICENSE).
