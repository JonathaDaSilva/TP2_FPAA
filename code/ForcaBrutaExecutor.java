import java.util.ArrayList;
import java.util.List;

/**
 * Resolve o tabuleiro por Força Bruta.
 * Diferente do Backtracking, aqui não há poda durante o preenchimento: o algoritmo
 * gera todas as combinações possíveis de Sol e Lua para as células vazias e só
 * confere as regras no final, quando o tabuleiro está completamente preenchido.
 */
public class ForcaBrutaExecutor implements Executor {

    private final Validador validador;

    private static final int VAZIO = Tango.VAZIO;
    private static final int SOL = Tango.SOL;
    private static final int LUA = Tango.LUA;

    public ForcaBrutaExecutor(Validador validador) {
        this.validador = validador;
    }

    /**
     * Antes de começar, anota numa lista as posições de todas as células vazias.
     * São essas posições que receberão as tentativas de Sol e Lua.
     */
    @Override
    public boolean executar(int[][] t) {
        List<int[]> vazias = new ArrayList<>();
        for (int l = 0; l < t.length; l++)
            for (int c = 0; c < t[l].length; c++)
                if (t[l][c] == VAZIO) vazias.add(new int[]{l, c});

        return forcaBrutaRecursivo(t, vazias, 0);
    }

    /**
     * Gera as combinações preenchendo as células vazias uma a uma.
     *
     * @param t      tabuleiro sendo preenchido.
     * @param vazias lista com as posições que precisam ser preenchidas.
     * @param i      qual célula vazia da lista está a ser preenchida agora.
     * @return verdadeiro se alguma combinação a partir daqui resultou em solução válida.
     */
    private boolean forcaBrutaRecursivo(int[][] t, List<int[]> vazias, int i) {
        // Condição de parada: todas as células vazias já receberam um valor.
        // Só com o tabuleiro cheio conferimos se a resolução é válida.
        if (i == vazias.size()) return validador.tabuleiroCompletoValido(t);

        int l = vazias.get(i)[0];
        int c = vazias.get(i)[1];

        // Tenta esta célula como Sol e segue preenchendo as próximas.
        t[l][c] = SOL;
        if (forcaBrutaRecursivo(t, vazias, i + 1)) return true;

        // Se não deu (houve retorno da chamada anterior), tenta a mesma célula como Lua.
        t[l][c] = LUA;
        if (forcaBrutaRecursivo(t, vazias, i + 1)) return true;

        // Nenhuma das duas opções levou a uma solução: limpa a célula e volta para o chamador.
        t[l][c] = VAZIO;
        return false;
    }
}
