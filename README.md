# Resolvedor de Quebra-Cabeça Tango

Trabalho Prático 2 da disciplina **Fundamentos de Projeto e Análise de Algoritmos** — Curso de Engenharia de Software, PUC Minas (2026/1).

Programa de console, em Java, que resolve um tabuleiro de **Tango** aplicando duas técnicas de busca em espaço de estados: **Força Bruta** (busca exaustiva) e **Backtracking** (busca com poda). O trabalho integra-se ao estudo de Problemas de Satisfação de Restrições (CSPs).

## O problema

O Tango é um quebra-cabeça de dedução lógica jogado em uma grade (6×6 ou 8×8) preenchida com apenas dois símbolos (Sol e Lua). Uma configuração válida deve respeitar cinco regras:

1. **Preenchimento completo** — toda célula contém um Sol ou uma Lua.
2. **Limite de adjacência** — no máximo 2 símbolos idênticos lado a lado (horizontal ou vertical).
3. **Equilíbrio** — cada linha e cada coluna contém a mesma quantidade de Sóis e de Luas.
4. **Restrição de igualdade (`=`)** — células ligadas por `=` devem conter o mesmo símbolo.
5. **Restrição de oposição (`×`)** — células ligadas por `×` devem conter símbolos opostos.

## Estratégias de resolução

A **Força Bruta** gera o espaço completo de combinações de preenchimento e valida cada configuração contra as cinco regras, evidenciando o problema da explosão combinatória.

O **Backtracking** constrói a solução de forma incremental, célula a célula, e aplica as regras como critérios de poda: assim que uma jogada viola uma restrição, o ramo é descartado antes de ser explorado, reduzindo drasticamente o espaço de busca.

## Estrutura do projeto

```
Tango.java        # main: monta os tabuleiros de exemplo, imprime e chama o solver
Validador.java    # as 5 regras: validação total e validação incremental (poda)
Solver.java       # forçaBruta() e backtracking()
```

A representação é enxuta: a grade é um `int[][]` (`1` = Sol, `0` = Lua, `-1` = vazio) e as restrições `=` e `×` são listas de coordenadas. Os tabuleiros de exemplo são definidos diretamente no código.

O projeto separa deliberadamente a **lógica de validação das regras** (`Validador`) da **mecânica de recursão** (`Solver`), conforme exigido no enunciado. Os dois algoritmos reutilizam a mesma validação; a diferença está em *quando* ela é invocada — a Força Bruta valida apenas o tabuleiro completo, enquanto o Backtracking valida cada jogada incrementalmente para podar a busca.

## Compilação e execução

```bash
javac *.java
java Tango
```

O programa imprime no terminal a configuração inicial (com lacunas e restrições) e, após a execução do algoritmo, o tabuleiro final resolvido em formato ASCII.

## Tecnologias

- Linguagem: **Java** (sem bibliotecas de terceiros para resolução de CSPs ou solvers prontos).
- Execução em console, sem interface gráfica.

## Licença

Distribuído sob os termos do arquivo [LICENSE](LICENSE).
