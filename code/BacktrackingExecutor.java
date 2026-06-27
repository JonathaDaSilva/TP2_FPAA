public class BacktrackingExecutor implements Executor {

    private final Validador validador;

    private static final int VAZIO = Tango.VAZIO;
    private static final int SOL   = Tango.SOL;
    private static final int LUA   = Tango.LUA;

    public BacktrackingExecutor(Validador validador) {
        this.validador = validador;
    }

    @Override
    public boolean executar(int[][] tabuleiro) {
        return backtrackingRecursivo(tabuleiro, 0);
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
