/**
 * Tango.java
 * Ponto de entrada do programa.
 *
 * Responsabilidades (parte "neutra", não-algorítmica):
 *   - montar os tabuleiros de exemplo;
 *   - imprimir o estado inicial;
 *   - chamar o solver escolhido;
 *   - imprimir o resultado final.
 *
 * A lógica de resolução fica em Solver.java; as regras, em Validador.java.
 */
import java.util.ArrayList;
import java.util.List;

public class Tango {

    // Convenção de valores das células:
    public static final int VAZIO = -1;
    public static final int LUA   = 0;
    public static final int SOL   = 1;

    public static void main(String[] args) {
        int[][] tabuleiro = {
            { VAZIO, VAZIO,   SOL, VAZIO, VAZIO, VAZIO },
            { VAZIO, VAZIO, VAZIO, VAZIO,   LUA, VAZIO },
            { VAZIO,   SOL, VAZIO, VAZIO, VAZIO, VAZIO },
            { VAZIO, VAZIO, VAZIO, VAZIO, VAZIO, VAZIO },
            { VAZIO,   LUA, VAZIO, VAZIO, VAZIO,   SOL },
            { VAZIO, VAZIO, VAZIO, VAZIO, VAZIO, VAZIO },
        };

        // ----- Restrições entre células adjacentes -----
        // Cada restrição liga duas células vizinhas. Formato sugerido:
        //   {linhaA, colA, linhaB, colB}
        // Mantidas em duas listas: uma para "=" (igual) e outra para "x" (oposto).
        List<int[]> igualdades = new ArrayList<>();
        List<int[]> oposicoes  = new ArrayList<>();
        // Exemplos (ajuste conforme o tabuleiro):
        // igualdades.add(new int[]{0, 0, 0, 1});   // (0,0) = (0,1)
        // oposicoes.add (new int[]{1, 2, 2, 2});    // (1,2) x (2,2)

        Validador validador = new Validador(igualdades, oposicoes);
        Solver solver = new Solver(validador);

        System.out.println("=== Tabuleiro inicial ===");
        imprimir(tabuleiro);

        // Escolha do algoritmo: troque para forcaBruta para comparar.
        boolean resolvido = solver.backtracking(tabuleiro);
        // boolean resolvido = solver.forcaBruta(tabuleiro);

        System.out.println("\n=== Resultado ===");
        if (resolvido) {
            imprimir(tabuleiro);
        } else {
            System.out.println("Nenhuma solução encontrada.");
        }
    }

    /** Impressão ASCII do tabuleiro (Sol = S, Lua = L, vazio = .). */
    public static void imprimir(int[][] t) {
        for (int[] linha : t) {
            StringBuilder sb = new StringBuilder();
            for (int v : linha) {
                char c = (v == SOL) ? 'S' : (v == LUA) ? 'L' : '.';
                sb.append(c).append(' ');
            }
            System.out.println(sb.toString().trim());
        }
    }
}
