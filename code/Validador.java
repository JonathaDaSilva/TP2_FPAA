import java.util.List;

/**
 * Guarda as cinco regras do Tango e responde se um tabuleiro (ou uma jogada)
 * as respeita. Esta classe, utilizada por injeção nos algoritmos de resolução,
 * julga estados do tabuleiro e verifica comprimento de regras.
 */
public class Validador {

    private final List<int[]> igualdades;
    private final List<int[]> oposicoes;

    private static final int VAZIO = Tango.VAZIO;
    private static final int SOL = Tango.SOL;
    private static final int LUA = Tango.LUA;

    public Validador(List<int[]> igualdades, List<int[]> oposicoes) {
        this.igualdades = igualdades;
        this.oposicoes = oposicoes;
    }

    /**
     * Confere um tabuleiro já totalmente preenchido contra todas as regras.
     * Primeiro garante que não sobrou nenhuma célula vazia; depois verifica
     * adjacência, equilíbrio e as restrições de igualdade e oposição.
     *
     * @return verdadeiro apenas se o tabuleiro estiver cheio e respeitar tudo.
     */
    public boolean tabuleiroCompletoValido(int[][] t) {
        for (int l = 0; l < t.length; l++)
            for (int c = 0; c < t[l].length; c++)
                if (t[l][c] == VAZIO) return false;

        return respeitaAdjacencia(t) && respeitaEquilibrio(t) && respeitaRestricoes(t);
    }

    /**
     * Verifica se colocar um valor numa célula é uma jogada permitida, olhando
     * apenas o impacto local dessa peça (e não o tabuleiro inteiro).
     * A jogada é simulada, avaliada e desfeita, para não bagunçar o tabuleiro
     * que o algoritmo está a montar.
     *
     * @param t     tabuleiro atual.
     * @param l     linha da célula.
     * @param c     coluna da célula.
     * @param valor símbolo que se quer colocar (Sol ou Lua).
     * @return verdadeiro se a jogada não viola nenhuma regra perto dessa célula.
     */
    public boolean jogadaValida(int[][] t, int l, int c, int valor) {
        int anterior = t[l][c];
        t[l][c] = valor;

        boolean valida = checarAdjacenciaLocal(t, l, c)
                && checarEquilibrioLocal(t, l, c)
                && checarRestricoesLocal(t, l, c, valor);

        t[l][c] = anterior;
        return valida;
    }

    // ------------------------------------------------------------------
    // Verificações sobre o tabuleiro completo
    // ------------------------------------------------------------------

    /** Regra 2: não pode haver três símbolos iguais seguidos, na linha ou na coluna. */
    private boolean respeitaAdjacencia(int[][] t) {
        int n = t.length;
        for (int l = 0; l < n; l++) {
            for (int c = 0; c < n; c++) {
                if (c <= n - 3 && t[l][c] == t[l][c+1] && t[l][c] == t[l][c+2]) return false;
                if (l <= n - 3 && t[l][c] == t[l+1][c] && t[l][c] == t[l+2][c]) return false;
            }
        }
        return true;
    }

    /** Regra 3: cada linha e cada coluna devem ter a mesma quantidade de Sóis e de Luas (n/2 de cada). */
    private boolean respeitaEquilibrio(int[][] t) {
        int n = t.length;
        int metade = n / 2;

        // Um único laço serve para a linha i e para a coluna i ao mesmo tempo.
        for (int i = 0; i < n; i++) {
            int solL = 0, luaL = 0, solC = 0, luaC = 0;
            for (int j = 0; j < n; j++) {
                if (t[i][j] == SOL) solL++; // contagem da linha i
                if (t[i][j] == LUA) luaL++;
                if (t[j][i] == SOL) solC++; // contagem da coluna i
                if (t[j][i] == LUA) luaC++;
            }
            if (solL != metade || luaL != metade) return false;
            if (solC != metade || luaC != metade) return false;
        }
        return true;
    }

    /** Regras 4 e 5: confere todas as restrições de igualdade e de oposição. */
    private boolean respeitaRestricoes(int[][] t) {
        // Igualdade: as duas células ligadas precisam ter o mesmo símbolo.
        for (int[] p : igualdades)
            if (t[p[0]][p[1]] != t[p[2]][p[3]]) return false;
        // Oposição: as duas células ligadas precisam ter símbolos diferentes.
        for (int[] p : oposicoes)
            if (t[p[0]][p[1]] == t[p[2]][p[3]]) return false;
        return true;
    }

    // ------------------------------------------------------------------
    // Verificações locais (poda) olham só o efeito de uma célula recém-preenchida.
    // ------------------------------------------------------------------

    /**
     * Versão local da Regra 2. Verifica apenas se a célula (r, c) recém-preenchida
     * formou três iguais seguidos. Para isso, olha as três janelas de tamanho 3 que
     * passam por ela, tanto na horizontal quanto na vertical.
     */
    private boolean checarAdjacenciaLocal(int[][] t, int r, int c) {
        int n = t.length;
        // Horizontal: célula como terceira, do meio, ou primeira de um trio.
        if (c >= 2 && t[r][c] == t[r][c-1] && t[r][c] == t[r][c-2]) return false;
        if (c >= 1 && c < n-1 && t[r][c] == t[r][c-1] && t[r][c] == t[r][c+1]) return false;
        if (c < n-2 && t[r][c] == t[r][c+1] && t[r][c] == t[r][c+2]) return false;
        // Vertical: as mesmas três posições, agora na coluna.
        if (r >= 2 && t[r][c] == t[r-1][c] && t[r][c] == t[r-2][c]) return false;
        if (r >= 1 && r < n-1 && t[r][c] == t[r-1][c] && t[r][c] == t[r+1][c]) return false;
        if (r < n-2 && t[r][c] == t[r+1][c] && t[r][c] == t[r+2][c]) return false;
        return true;
    }

    /**
     * Versão local da Regra 3. Como o tabuleiro continua montado, não dá
     * para exigir exatamente n/2; o que se faz é impedir que a linha ou a coluna
     * passem de n/2 de um mesmo símbolo. Se já passou, esse caminho não tem futuro.
     */
    private boolean checarEquilibrioLocal(int[][] t, int r, int c) {
        int n = t.length;
        int max = n / 2;
        int solL = 0, luaL = 0, solC = 0, luaC = 0;

        for (int i = 0; i < n; i++) {
            if (t[r][i] == SOL) solL++; // contagem da linha r
            if (t[r][i] == LUA) luaL++;
            if (t[i][c] == SOL) solC++; // contagem da coluna c
            if (t[i][c] == LUA) luaC++;
        }

        return solL <= max && luaL <= max && solC <= max && luaC <= max;
    }

    /**
     * Versão local das Regras 4 e 5. Confere apenas as restrições que tocam a
     * célula (r, c), e somente quando a célula vizinha já tem um valor; se o vizinho
     * continua vazio, não há como saber se a regra será quebrada.
     */
    private boolean checarRestricoesLocal(int[][] t, int r, int c, int valor) {
        // Igualdade: se a célula está numa restrição, o vizinho já preenchido
        // precisa ter o mesmo valor que estamos tentando colocar.
        for (int[] p : igualdades) {
            if (p[0] == r && p[1] == c) {
                int viz = t[p[2]][p[3]];
                if (viz != VAZIO && viz != valor) return false;
            } else if (p[2] == r && p[3] == c) {
                int viz = t[p[0]][p[1]];
                if (viz != VAZIO && viz != valor) return false;
            }
        }
        // Oposição: aqui o vizinho já preenchido precisa ter valor diferente.
        for (int[] p : oposicoes) {
            if (p[0] == r && p[1] == c) {
                int viz = t[p[2]][p[3]];
                if (viz != VAZIO && viz == valor) return false;
            } else if (p[2] == r && p[3] == c) {
                int viz = t[p[0]][p[1]];
                if (viz != VAZIO && viz == valor) return false;
            }
        }
        return true;
    }
}
