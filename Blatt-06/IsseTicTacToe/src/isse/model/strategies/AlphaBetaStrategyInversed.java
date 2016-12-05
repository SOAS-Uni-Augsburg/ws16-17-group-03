package isse.model.strategies;

/**
 * Takes the first free field
 * 
 * @author isse-soas
 *
 */
public class AlphaBetaStrategyInversed extends AlphaBetaStrategy {

	/**
	 * The heuristic evaluation function for the current board
	 * 
	 * @Return +100, +10, +1 for EACH 3-, 2-, 1-in-a-line for opponent. -100,
	 *         -10, -1 for EACH 3-, 2-, 1-in-a-line for current. 0 otherwise
	 */
	@Override
	public int evaluate() {
		int score = -1 * super.evaluate();
		return score;
	}

}
