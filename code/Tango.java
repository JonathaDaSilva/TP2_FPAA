import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Tango {

    public static final int VAZIO = -1;
    public static final int LUA   = 0;
    public static final int SOL   = 1;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 1. Leitura do tamanho do tabuleiro
        System.out.print("Digite o tamanho do tabuleiro (N): ");
        int n = scanner.nextInt();

        // 2. Leitura da matriz do tabuleiro
        int[][] tabuleiro = new int[n][n];
        System.out.println("Digite as linhas da matriz (use -1 para Vazio, 0 para Lua, 1 para Sol):");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tabuleiro[i][j] = scanner.nextInt();
            }
        }

        // 3. Leitura das restrições de IGUALDADE (=)
        List<int[]> igualdades = new ArrayList<>();
        System.out.print("Digite a quantidade de restrições de IGUALDADE (=): ");
        int qtdIgualdades = scanner.nextInt();
        if (qtdIgualdades > 0) {
            System.out.println("Digite as igualdades no formato 'linhaA colA linhaB colB':");
            for (int i = 0; i < qtdIgualdades; i++) {
                igualdades.add(new int[]{ scanner.nextInt(), scanner.nextInt(), scanner.nextInt(), scanner.nextInt() });
            }
        }

        // 4. Leitura das restrições de OPOSIÇÃO (x)
        List<int[]> oposicoes = new ArrayList<>();
        System.out.print("Digite a quantidade de restrições de OPOSIÇÃO (x): ");
        int qtdOposicoes = scanner.nextInt();
        if (qtdOposicoes > 0) {
            System.out.println("Digite as oposições no formato 'linhaA colA linhaB colB':");
            for (int i = 0; i < qtdOposicoes; i++) {
                oposicoes.add(new int[]{ scanner.nextInt(), scanner.nextInt(), scanner.nextInt(), scanner.nextInt() });
            }
        }

        scanner.close();

        // ----- Processamento e Execução -----
        Validador validador = new Validador(igualdades, oposicoes);
        Solver solver = new Solver(validador);

        System.out.println("\n=== Tabuleiro inicial ===");
        imprimir(tabuleiro);

        long tempoInicial = System.currentTimeMillis();
        boolean resolvido = solver.backtracking(tabuleiro);
        long tempoFinal = System.currentTimeMillis();

        System.out.println("\n=== Resultado ===");
        if (resolvido) {
            imprimir(tabuleiro);
            System.out.println("\nResolvido em: " + (tempoFinal - tempoInicial) + " ms");
        } else {
            System.out.println("Nenhuma solução encontrada.");
        }
    }

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