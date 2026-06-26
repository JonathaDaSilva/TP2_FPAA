import java.util.ArrayList;
import java.util.List;

public class Solver {

    private final Validador validador;

    private static final int VAZIO = Tango.VAZIO;
    private static final int SOL = Tango.SOL;
    private static final int LUA = Tango.LUA;

    public Solver(Validador validador) {
        this.validador = validador;
    }

    public boolean forcaBruta(int[][] t) {
        List<int[]> vazias = new ArrayList<>();
        for (int l = 0; l < t.length; l++)
            for (int c = 0; c < t[l].length; c++)
                if (t[l][c] == VAZIO) vazias.add(new int[]{l, c});

        return forcaBrutaRecursivo(t, vazias, 0);
    }

    private boolean forcaBrutaRecursivo(int[][] t, List<int[]> vazias, int i) {
        if (i == vazias.size()) return validador.tabuleiroCompletoValido(t);

        int l = vazias.get(i)[0];
        int c = vazias.get(i)[1];

        t[l][c] = SOL;
        if (forcaBrutaRecursivo(t, vazias, i + 1)) return true;

        t[l][c] = LUA;
        if (forcaBrutaRecursivo(t, vazias, i + 1)) return true;

        t[l][c] = VAZIO;
        return false;
    }

    public boolean backtracking(int[][] t) {
        return backtrackingRecursivo(t, 0);
    }

    private boolean backtrackingRecursivo(int[][] t, int i) {
        int n = t.length;
        if (i == n * n) return validador.tabuleiroCompletoValido(t);

        int l = i / n;
        int c = i % n;

        if (t[l][c] != VAZIO) return backtrackingRecursivo(t, i + 1);

        if (validador.jogadaValida(t, l, c, SOL)) {
            t[l][c] = SOL;
            if (backtrackingRecursivo(t, i + 1)) return true;
            t[l][c] = VAZIO;
        }

        if (validador.jogadaValida(t, l, c, LUA)) {
            t[l][c] = LUA;
            if (backtrackingRecursivo(t, i + 1)) return true;
            t[l][c] = VAZIO;
        }

        return false;
    }
}
