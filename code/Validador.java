import java.util.List;

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

    public boolean tabuleiroCompletoValido(int[][] t) {
        for (int l = 0; l < t.length; l++)
            for (int c = 0; c < t[l].length; c++)
                if (t[l][c] == VAZIO) return false;

        return respeitaAdjacencia(t) && respeitaEquilibrio(t) && respeitaRestricoes(t);
    }

    public boolean jogadaValida(int[][] t, int l, int c, int valor) {
        int anterior = t[l][c];
        t[l][c] = valor;

        boolean valida = checarAdjacenciaLocal(t, l, c)
                && checarEquilibrioLocal(t, l, c)
                && checarRestricoesLocal(t, l, c, valor);

        t[l][c] = anterior;
        return valida;
    }

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

    private boolean respeitaEquilibrio(int[][] t) {
        int n = t.length;
        int metade = n / 2;

        for (int i = 0; i < n; i++) {
            int solL = 0, luaL = 0, solC = 0, luaC = 0;
            for (int j = 0; j < n; j++) {
                if (t[i][j] == SOL) solL++;
                if (t[i][j] == LUA) luaL++;
                if (t[j][i] == SOL) solC++;
                if (t[j][i] == LUA) luaC++;
            }
            if (solL != metade || luaL != metade) return false;
            if (solC != metade || luaC != metade) return false;
        }
        return true;
    }

    private boolean respeitaRestricoes(int[][] t) {
        for (int[] p : igualdades)
            if (t[p[0]][p[1]] != t[p[2]][p[3]]) return false;
        for (int[] p : oposicoes)
            if (t[p[0]][p[1]] == t[p[2]][p[3]]) return false;
        return true;
    }

    private boolean checarAdjacenciaLocal(int[][] t, int r, int c) {
        int n = t.length;
        if (c >= 2 && t[r][c] == t[r][c-1] && t[r][c] == t[r][c-2]) return false;
        if (c >= 1 && c < n-1 && t[r][c] == t[r][c-1] && t[r][c] == t[r][c+1]) return false;
        if (c < n-2 && t[r][c] == t[r][c+1] && t[r][c] == t[r][c+2]) return false;
        if (r >= 2 && t[r][c] == t[r-1][c] && t[r][c] == t[r-2][c]) return false;
        if (r >= 1 && r < n-1 && t[r][c] == t[r-1][c] && t[r][c] == t[r+1][c]) return false;
        if (r < n-2 && t[r][c] == t[r+1][c] && t[r][c] == t[r+2][c]) return false;
        return true;
    }

    private boolean checarEquilibrioLocal(int[][] t, int r, int c) {
        int n = t.length;
        int max = n / 2;
        int solL = 0, luaL = 0, solC = 0, luaC = 0;

        for (int i = 0; i < n; i++) {
            if (t[r][i] == SOL) solL++;
            if (t[r][i] == LUA) luaL++;
            if (t[i][c] == SOL) solC++;
            if (t[i][c] == LUA) luaC++;
        }

        return solL <= max && luaL <= max && solC <= max && luaC <= max;
    }

    private boolean checarRestricoesLocal(int[][] t, int r, int c, int valor) {
        for (int[] p : igualdades) {
            if (p[0] == r && p[1] == c) {
                int viz = t[p[2]][p[3]];
                if (viz != VAZIO && viz != valor) return false;
            } else if (p[2] == r && p[3] == c) {
                int viz = t[p[0]][p[1]];
                if (viz != VAZIO && viz != valor) return false;
            }
        }
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
