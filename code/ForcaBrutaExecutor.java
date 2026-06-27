import java.util.ArrayList;
import java.util.List;

public class ForcaBrutaExecutor implements Executor {

    private final Validador validador;

    private static final int VAZIO = Tango.VAZIO;
    private static final int SOL   = Tango.SOL;
    private static final int LUA   = Tango.LUA;

    public ForcaBrutaExecutor(Validador validador) {
        this.validador = validador;
    }

    @Override
    public boolean executar(int[][] t) {
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
}
