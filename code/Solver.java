import java.util.ArrayList;
import java.util.List;

public class Solver {

    private final Validador validador;

    // Reusa a convenção única definida em Tango (VAZIO = -1, LUA = 0, SOL = 1).
    private static final int VAZIO = Tango.VAZIO;
    private static final int SOL   = Tango.SOL;
    private static final int LUA   = Tango.LUA;

    public Solver(Validador validador) {
        this.validador = validador;
    }

    /**
     * FORÇA BRUTA (busca exaustiva).
     */
    public boolean forcaBruta(int[][] t) {
        // Passo 1: Localizar todas as posições vazias no tabuleiro.
        // Guardamos as coordenadas [linha, coluna] em uma lista para facilitar.
        List<int[]> celulasVazias = new ArrayList<>();

        for (int linha = 0; linha < t.length; linha++) {
            for (int coluna = 0; coluna < t[linha].length; coluna++) {
                if (t[linha][coluna] == VAZIO) {
                    celulasVazias.add(new int[]{linha, coluna});
                }
            }
        }

        // Passos 2, 3 e 4: Iniciar a recursão que vai gerar as combinações.
        return forcaBrutaRecursivo(t, celulasVazias, 0);
    }

    /**
     * Método auxiliar recursivo que testa as combinações de SOL e LUA.
     * * @param t             O tabuleiro atual.
     * @param celulasVazias A lista de coordenadas das células que precisam ser preenchidas.
     * @param indiceAtual   Qual célula vazia da lista estamos preenchendo agora.
     * @return true se encontrou uma configuração válida, false caso contrário.
     */
    private boolean forcaBrutaRecursivo(int[][] t, List<int[]> celulasVazias, int indiceAtual) {

        // Passo 3: Condição de parada (Caso Base).
        // Se o índice atual for igual ao tamanho da lista, significa que preenchemos todas as células.
        if (indiceAtual == celulasVazias.size()) {
            // Tabuleiro está cheio. Perguntamos ao validador se a solução é válida.
            return validador.tabuleiroCompletoValido(t);
        }

        // Pegar as coordenadas da próxima célula vazia que vamos testar.
        int[] posicao = celulasVazias.get(indiceAtual);
        int linha = posicao[0];
        int coluna = posicao[1];

        // Passo 2: Atribuir SOL e aprofundar na recursão.
        t[linha][coluna] = SOL;
        if (forcaBrutaRecursivo(t, celulasVazias, indiceAtual + 1)) {
            return true; // Passo 4: Parar na primeira solução válida.
        }

        // Passo 2: Se SOL não resultou em um tabuleiro válido, tentamos LUA.
        t[linha][coluna] = LUA;
        if (forcaBrutaRecursivo(t, celulasVazias, indiceAtual + 1)) {
            return true; // Passo 4: Parar na primeira solução válida.
        }

        // Se nem SOL nem LUA funcionaram para este ramo, "limpamos" a célula
        // para não poluir o tabuleiro de uma tentativa que falhou (Backtracking básico).
        t[linha][coluna] = VAZIO;

        // Retornamos falso para avisar a chamada anterior que este caminho não tem solução.
        return false;
    }

    /**
     * BACKTRACKING (busca com poda).
     * Ideia: preencher célula a célula; antes de descer na recursão, usar
     * validador.jogadaValida(...) para descartar ramos inválidos imediatamente.
     *
     * Roteiro sugerido:
     *   - Caso base: passou da última célula => tabuleiro completo e válido => true.
     *   - Se a célula atual já tem dica, avançar para a próxima.
     *   - Senão, tentar SOL e LUA: para cada valor que passe em jogadaValida(...),
     *     atribuir, recursão para a próxima célula; se falhar, desfazer (backtrack).
     *
     * Dica: percorrer as células por índice linear de 0 a n*n-1, convertendo com
     *       linha = i / n  e  coluna = i % n. Um método auxiliar recursivo ajuda.
     *
     * @return true se encontrou solução (deixando-a em 't').
     */
    public boolean backtracking(int[][] t) {
        // TODO: implementar a recursão com poda (pode chamar um auxiliar privado).
        throw new UnsupportedOperationException("Implementar backtracking");
    }
}
