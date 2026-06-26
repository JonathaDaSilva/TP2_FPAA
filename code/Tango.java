import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Tango {

    public static final int VAZIO = -1;
    public static final int LUA = 0;
    public static final int SOL = 1;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 1. Leitura do tamanho do tabuleiro
        System.out.print("Digite o tamanho do tabuleiro (N): ");
        int n = lerInteiro(scanner);
        if (n <= 0 || n % 2 != 0) {
            System.out.println("Erro: N deve ser um inteiro positivo e par (cada linha/coluna precisa de n/2 Sóis e n/2 Luas).");
            scanner.close();
            return;
        }

        // 2. Leitura da matriz do tabuleiro
        int[][] tabuleiro = new int[n][n];
        System.out.println("Digite as linhas da matriz (use -1 para Vazio, 0 para Lua, 1 para Sol):");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int v = lerInteiro(scanner);
                if (v != VAZIO && v != LUA && v != SOL) {
                    System.out.println("Erro: valor inválido em (" + i + "," + j + "). Use -1, 0 ou 1.");
                    scanner.close();
                    return;
                }
                tabuleiro[i][j] = v;
            }
        }

        // 3. Leitura das restrições de IGUALDADE (=)
        List<int[]> igualdades = new ArrayList<>();
        System.out.print("Digite a quantidade de restrições de IGUALDADE (=): ");
        int qtdIgualdades = lerInteiro(scanner);
        if (qtdIgualdades > 0) {
            System.out.println("Digite as igualdades no formato 'linhaA colA linhaB colB':");
            for (int i = 0; i < qtdIgualdades; i++) {
                int[] r = {lerInteiro(scanner), lerInteiro(scanner), lerInteiro(scanner), lerInteiro(scanner)};
                if (!coordsValidas(r, n)) {
                    System.out.println("Erro: restrição de igualdade #" + (i + 1) + " tem coordenadas fora do tabuleiro.");
                    scanner.close();
                    return;
                }
                igualdades.add(r);
            }
        }

        // 4. Leitura das restrições de OPOSIÇÃO (x)
        List<int[]> oposicoes = new ArrayList<>();
        System.out.print("Digite a quantidade de restrições de OPOSIÇÃO (x): ");
        int qtdOposicoes = lerInteiro(scanner);
        if (qtdOposicoes > 0) {
            System.out.println("Digite as oposições no formato 'linhaA colA linhaB colB':");
            for (int i = 0; i < qtdOposicoes; i++) {
                int[] r = {lerInteiro(scanner), lerInteiro(scanner), lerInteiro(scanner), lerInteiro(scanner)};
                if (!coordsValidas(r, n)) {
                    System.out.println("Erro: restrição de oposição #" + (i + 1) + " tem coordenadas fora do tabuleiro.");
                    scanner.close();
                    return;
                }
                oposicoes.add(r);
            }
        }

        // ----- Processamento e Execução -----
        Validador validador = new Validador(igualdades, oposicoes);
        Solver solver = new Solver(validador);

        System.out.println("Deseja que seja resolvido por qual método?");
        System.out.println("[1] Backtracking");
        System.out.println("[2] Força Bruta");
        int opcao = lerInteiro(scanner);

        scanner.close();

        System.out.println("\n=== Tabuleiro inicial ===");
        imprimir(tabuleiro);
        imprimirRestricoes(igualdades, oposicoes);

        long tempoInicial = System.nanoTime();
        boolean resolvido = false;
        if (opcao == 1) {
            resolvido = solver.backtracking(tabuleiro);
        } else if (opcao == 2) {
            resolvido = solver.forcaBruta(tabuleiro);
        } else {
            System.out.println("Opção inválida.");
        }
        long tempoFinal = System.nanoTime();

        long duracaoNs = tempoFinal - tempoInicial;
        double duracaoMs = duracaoNs / 1_000_000.0;

        System.out.println("\n=== Resultado ===");
        if (resolvido) {
            imprimir(tabuleiro);
            System.out.printf("%nResolvido em: %.3f ms (%d ns)%n", duracaoMs, duracaoNs);
        } else {
            System.out.println("Nenhuma solução encontrada.");
        }
    }

    /** Lê um inteiro do scanner, encerrando o programa se a entrada não for numérica. */
    private static int lerInteiro(Scanner scanner) {
        if (!scanner.hasNextInt()) {
            System.out.println("Erro: entrada inválida (esperado um número inteiro).");
            scanner.close();
            System.exit(1);
        }
        return scanner.nextInt();
    }

    /** Verdadeiro se as duas células {lA,cA,lB,cB} estão dentro do tabuleiro n x n. */
    private static boolean coordsValidas(int[] r, int n) {
        for (int v : r) {
            if (v < 0 || v >= n) return false;
        }
        return true;
    }

    /** Lista as restrições = e x lidas, junto ao tabuleiro inicial. */
    public static void imprimirRestricoes(List<int[]> igualdades, List<int[]> oposicoes) {
        if (igualdades.isEmpty() && oposicoes.isEmpty()) {
            System.out.println("(sem restrições = / x)");
            return;
        }
        StringBuilder sb = new StringBuilder("Restrições: ");
        for (int[] r : igualdades) {
            sb.append("(").append(r[0]).append(",").append(r[1]).append(")=(")
              .append(r[2]).append(",").append(r[3]).append(") | ");
        }
        for (int[] r : oposicoes) {
            sb.append("(").append(r[0]).append(",").append(r[1]).append(")x(")
              .append(r[2]).append(",").append(r[3]).append(") | ");
        }
        System.out.println(sb.toString().trim());
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