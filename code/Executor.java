/**
 * Define o contrato de resolução do tabuleiro. A classe Tango
 * trabalha apenas com este contrato, não precisando conhecer regras específicas do
 * tipo de algoritmo que realizará a resolução.
 */
public interface Executor {

    /**
     * Tenta preencher as células vazias do tabuleiro com uma solução válida.
     *
     * @param tabuleiro matriz N x N, alterada diretamente; fica preenchida quando há solução.
     * @return verdadeiro encontrou-se uma solução válida; falso caso contrário.
     */
    boolean executar(int[][] tabuleiro);
}
