import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Tango {

    public static final int VAZIO = -1;
    public static final int LUA = 0;
    public static final int SOL = 1;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite o tamanho do tabuleiro (N): ");
        int n = scanner.nextInt();

        int[][] tabuleiro = new int[n][n];
        System.out.println("Digite as linhas da matriz (use -1 para Vazio, 0 para Lua, 1 para Sol):");
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tabuleiro[i][j] = scanner.nextInt();

        List<int[]> igualdades = new ArrayList<>();
        System.out.print("Digite a quantidade de restrições de IGUALDADE (=): ");
        int qtdIgualdades = scanner.nextInt();
        for (int i = 0; i < qtdIgualdades; i++)
            igualdades.add(new int[]{scanner.nextInt(), scanner.nextInt(), scanner.nextInt(), scanner.nextInt()});

        List<int[]> oposicoes = new ArrayList<>();
        System.out.print("Digite a quantidade de restrições de OPOSIÇÃO (x): ");
        int qtdOposicoes = scanner.nextInt();
        for (int i = 0; i < qtdOposicoes; i++)
            oposicoes.add(new int[]{scanner.nextInt(), scanner.nextInt(), scanner.nextInt(), scanner.nextInt()});

        Solver solver = new Solver(new Validador(igualdades, oposicoes));

        System.out.println("Deseja que seja resolvido por qual método?");
        System.out.println("[1] Backtracking");
        System.out.println("[2] Força Bruta");
        int opcao = scanner.nextInt();
        scanner.close();

        System.out.println("\n=== Tabuleiro inicial ===");
        imprimir(tabuleiro);

        long inicio = System.nanoTime();
        boolean resolvido = false;
        if (opcao == 1) resolvido = solver.backtracking(tabuleiro);
        else if (opcao == 2) resolvido = solver.forcaBruta(tabuleiro);
        else System.out.println("Opção inválida.");
        long fim = System.nanoTime();

        long ns = fim - inicio;
        System.out.println("\n=== Resultado ===");
        if (resolvido) {
            imprimir(tabuleiro);
            System.out.printf("%nResolvido em: %.3f ms (%d ns)%n", ns / 1_000_000.0, ns);
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
