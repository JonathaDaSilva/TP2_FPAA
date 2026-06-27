/**
 * Resolve o tabuleiro por Backtracking.
 * A ideia é preencher as células uma a uma e, antes de colocar cada símbolo,
 * perguntar ao Validador se aquela jogada já viola alguma regra. Se viola, o
 * caminho é abandonado é podado, evitando explorar combinações que com
 * certeza não levam a uma solução.
 */
public class BacktrackingExecutor implements Executor {

    private final Validador validador;

    private static final int VAZIO = Tango.VAZIO;
    private static final int SOL = Tango.SOL;
    private static final int LUA = Tango.LUA;

    public BacktrackingExecutor(Validador validador) {
        this.validador = validador;
    }

    /** Inicia a resolução a partir da primeira célula (posição 0). */
    @Override
    public boolean executar(int[][] tabuleiro) {
        return backtrackingRecursivo(tabuleiro, 0);
    }

    /**
     * Preenche o tabuleiro célula por célula, de forma recursiva.
     * As células são percorridas como se a matriz fosse uma única sequência:
     * a posição i vira linha = i / n e coluna = i % n.
     *
     * @param t tabuleiro sendo preenchido.
     * @param i posição atual na sequência de células (de 0 até n*n).
     * @return verdadeiro se a partir daqui foi possível completar uma solução válida.
     */
    private boolean backtrackingRecursivo(int[][] t, int i) {
        int n = t.length;

        // Condição de parada: todas as células visitadas.
        // Aqui o tabuleiro está cheio, então fazemos a conferência final completa.
        // Essa conferência também protege contra dicas iniciais já inválidas, que a
        // verificação por jogada não percorre.
        if (i == n * n) return validador.tabuleiroCompletoValido(t);

        int l = i / n;
        int c = i % n;

        // Se a célula já veio preenchida como dica, não há o que alterar, segue a exeução em nova chamada.
        if (t[l][c] != VAZIO) return backtrackingRecursivo(t, i + 1);

        // Tenta colocar um Sol, mas só se essa jogada não quebrar nenhuma regra.
        if (validador.jogadaValida(t, l, c, SOL)) {
            t[l][c] = SOL;
            if (backtrackingRecursivo(t, i + 1)) return true;
            t[l][c] = VAZIO; // desfaz caso dê errado.
        }

        // Tenta colocar uma Lua, sob a mesma condição.
        if (validador.jogadaValida(t, l, c, LUA)) {
            t[l][c] = LUA;
            if (backtrackingRecursivo(t, i + 1)) return true;
            t[l][c] = VAZIO; // desfaz.
        }

        // Nenhuma combinação funcionou: avisa quem chamou para mudar a escolha anterior.
        return false;
    }
}
