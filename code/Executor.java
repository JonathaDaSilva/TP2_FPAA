/**
 * Define uma estratégia de resolução do tabuleiro.
 * O projeto oferece duas estratégias: Força Bruta e Backtracking. A classe Tango
 * trabalha apenas com este contrato, sem precisar conhecer qual técnica está
 * a resolver o tabuleiro. Cada estratégia recebe um Validador (responsável pelas
 * regras e podas) e preenche o próprio tabuleiro recebido.
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
