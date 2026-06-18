import java.util.ArrayList;
import java.util.List;

public class Solver {

    private final Validador validador;

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
     *
     * @param t             O tabuleiro atual.
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
     */
    public boolean backtracking(int[][] t) {
        // Inicia a recursão a partir do índice linear 0
        return backtrackingRecursivo(t, 0);
    }

    /**
     * Método auxiliar recursivo usando índice linear.
     */
    private boolean backtrackingRecursivo(int[][] t, int indiceLinear) {
        int n = t.length;
        int totalCelulas = n * n;

        // 1. Caso base: passou da última célula (índice igual ao total de células).
        // Validação completa uma única vez: garante a corretude do tabuleiro cheio
        // (inclusive contra dicas iniciais inconsistentes, que a poda local não cobre).
        if (indiceLinear == totalCelulas) {
            return validador.tabuleiroCompletoValido(t);
        }

        // Conversão do índice linear para coordenadas da matriz (Linha e Coluna)
        int linha = indiceLinear / n;
        int coluna = indiceLinear % n;

        // 2. Se a célula atual já tem dica (não está vazia), avançamos para a próxima
        if (t[linha][coluna] != VAZIO) {
            return backtrackingRecursivo(t, indiceLinear + 1);
        }

        // 3. Senão, tentar SOL e LUA:

        // --- Tentativa com SOL ---
        if (validador.jogadaValida(t, linha, coluna, SOL)) {
            t[linha][coluna] = SOL; // Atribui

            if (backtrackingRecursivo(t, indiceLinear + 1)) {
                return true;
            }

            t[linha][coluna] = VAZIO;
        }

        // --- Tentativa com LUA ---
        if (validador.jogadaValida(t, linha, coluna, LUA)) {
            t[linha][coluna] = LUA;

            if (backtrackingRecursivo(t, indiceLinear + 1)) {
                return true;
            }

            t[linha][coluna] = VAZIO;
        }

        // Se nem SOL nem LUA foram válidos ou se ambos falharam nos passos seguintes,
        // retornamos false para forçar o nível anterior a mudar sua escolha.
        return false;
    }
}
