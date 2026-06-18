import java.util.List;

public class Validador {

    private final List<int[]> igualdades;
    private final List<int[]> oposicoes;

    private static final int VAZIO = Tango.VAZIO;
    private static final int SOL   = Tango.SOL;
    private static final int LUA   = Tango.LUA;

    public Validador(List<int[]> igualdades, List<int[]> oposicoes) {
        this.igualdades = igualdades;
        this.oposicoes  = oposicoes;
    }

    /**
     * Verdadeiro se um tabuleiro COMPLETO (sem células vazias) respeita as 5 regras.
     */
    public boolean tabuleiroCompletoValido(int[][] t) {
        // Primeiro, garante que não há nenhuma célula vazia sobrando
        for (int l = 0; l < t.length; l++) {
            for (int c = 0; c < t[l].length; c++) {
                if (t[l][c] == VAZIO) return false;
            }
        }

        // Se estiver cheio, valida contra as 3 grandes regras globais
        return respeitaAdjacencia(t) && respeitaEquilibrio(t) && respeitaRestricoes(t);
    }

    /**
     * Verdadeiro se colocar 'valor' em (linha, coluna) NÃO viola nenhuma regra.
     * Abordagem pragmática: simula a jogada e valida localmente.
     */
    public boolean jogadaValida(int[][] t, int linha, int coluna, int valor) {
        // Guarda o estado original da célula (geralmente VAZIO)
        int estadoAnterior = t[linha][coluna];

        // Faz a jogada temporária
        t[linha][coluna] = valor;

        // Executa as validações locais (focadas apenas no impacto desta peça)
        boolean valida = checarAdjacenciaLocal(t, linha, coluna)
                && checarEquilibrioLocal(t, linha, coluna)
                && checarRestricoesLocal(t, linha, coluna, valor);

        // Desfaz a jogada para não sujar o tabuleiro do Backtracking
        t[linha][coluna] = estadoAnterior;

        return valida;
    }

    // ---------------------------------------------------------------------
    // REGRAS GLOBAIS (Usadas no Tabuleiro Completo)
    // ---------------------------------------------------------------------

    /** Regra 2: nenhum trio de símbolos idênticos consecutivos (horizontal/vertical). */
    private boolean respeitaAdjacencia(int[][] t) {
        int n = t.length;
        for (int l = 0; l < n; l++) {
            for (int c = 0; c < n; c++) {
                // Horizontal (checa o trio a partir de c)
                if (c <= n - 3 && t[l][c] == t[l][c+1] && t[l][c] == t[l][c+2]) {
                    return false;
                }
                // Vertical (checa o trio a partir de l)
                if (l <= n - 3 && t[l][c] == t[l+1][c] && t[l][c] == t[l+2][c]) {
                    return false;
                }
            }
        }
        return true;
    }

    /** Regra 3: cada linha e cada coluna tem a mesma quantidade de Sóis e Luas. */
    private boolean respeitaEquilibrio(int[][] t) {
        int n = t.length;
        int metade = n / 2;

        for (int i = 0; i < n; i++) {
            int solLinha = 0, luaLinha = 0;
            int solColuna = 0, luaColuna = 0;

            for (int j = 0; j < n; j++) {
                if (t[i][j] == SOL) solLinha++;
                if (t[i][j] == LUA) luaLinha++;
                if (t[j][i] == SOL) solColuna++;
                if (t[j][i] == LUA) luaColuna++;
            }

            if (solLinha != metade || luaLinha != metade) return false;
            if (solColuna != metade || luaColuna != metade) return false;
        }
        return true;
    }

    /** Regras 4 e 5: restrições de igualdade (=) e oposição (x). */
    private boolean respeitaRestricoes(int[][] t) {
        // Valida todas as igualdades
        for (int[] par : igualdades) {
            if (t[par[0]][par[1]] != t[par[2]][par[3]]) return false;
        }
        // Valida todas as oposições
        for (int[] par : oposicoes) {
            if (t[par[0]][par[1]] == t[par[2]][par[3]]) return false;
        }
        return true;
    }

    // ---------------------------------------------------------------------
    // AUXILIARES DE VALIDAÇÃO LOCAL (Otimizações para a Poda)
    // ---------------------------------------------------------------------

    private boolean checarAdjacenciaLocal(int[][] t, int r, int c) {
        int n = t.length;

        // Análise Horizontal: verifica as 3 combinações possíveis onde (r, c) pode estar inserido
        if (c >= 2 && t[r][c] == t[r][c-1] && t[r][c] == t[r][c-2]) return false;
        if (c >= 1 && c < n - 1 && t[r][c] == t[r][c-1] && t[r][c] == t[r][c+1]) return false;
        if (c < n - 2 && t[r][c] == t[r][c+1] && t[r][c] == t[r][c+2]) return false;

        // Análise Vertical: as mesmas 3 combinações na coluna
        if (r >= 2 && t[r][c] == t[r-1][c] && t[r][c] == t[r-2][c]) return false;
        if (r >= 1 && r < n - 1 && t[r][c] == t[r-1][c] && t[r][c] == t[r+1][c]) return false;
        if (r < n - 2 && t[r][c] == t[r+1][c] && t[r][c] == t[r+2][c]) return false;

        return true;
    }

    private boolean checarEquilibrioLocal(int[][] t, int r, int c) {
        int n = t.length;
        int limiteMaximo = n / 2;

        int solLinha = 0, luaLinha = 0;
        int solColuna = 0, luaColuna = 0;

        for (int i = 0; i < n; i++) {
            if (t[r][i] == SOL) solLinha++;
            if (t[r][i] == LUA) luaLinha++;

            if (t[i][c] == SOL) solColuna++;
            if (t[i][c] == LUA) luaColuna++;
        }

        // Se estourar a cota permitida de qualquer um dos lados, a jogada é inválida
        return solLinha <= limiteMaximo && luaLinha <= limiteMaximo
                && solColuna <= limiteMaximo && luaColuna <= limiteMaximo;
    }

    private boolean checarRestricoesLocal(int[][] t, int r, int c, int valor) {
        // Checa apenas os vínculos de IGUALDADE que envolvem a célula atual
        for (int[] par : igualdades) {
            if (par[0] == r && par[1] == c) {
                int vizinho = t[par[2]][par[3]];
                if (vizinho != VAZIO && vizinho != valor) return false;
            } else if (par[2] == r && par[3] == c) {
                int vizinho = t[par[0]][par[1]];
                if (vizinho != VAZIO && vizinho != valor) return false;
            }
        }

        // Checa apenas os vínculos de OPOSIÇÃO que envolvem a célula atual
        for (int[] par : oposicoes) {
            if (par[0] == r && par[1] == c) {
                int vizinho = t[par[2]][par[3]];
                if (vizinho != VAZIO && vizinho == valor) return false;
            } else if (par[2] == r && par[3] == c) {
                int vizinho = t[par[0]][par[1]];
                if (vizinho != VAZIO && vizinho == valor) return false;
            }
        }

        return true;
    }
}