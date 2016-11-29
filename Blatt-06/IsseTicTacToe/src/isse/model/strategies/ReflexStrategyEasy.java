package isse.model.strategies;

import java.util.List;

import isse.model.GameBoard;
import isse.model.Move;
import isse.model.Player;

/**
 * Takes the first free field
 * 
 * @author isse-soas
 *
 */
public class ReflexStrategyEasy extends RandomStrategy implements PlayerBasedStrategy {
	private int level = 0;
	private Player current, opponent;

	protected void setSchwierigkeit(int schwierigkeit) {
		level = schwierigkeit;
	}

	public void setPlayer(Player player) {
		current = player;
		opponent = (player == Player.CROSSES) ? Player.NOUGHTS : Player.CROSSES;
	}

	@Override
	public Move getMove(GameBoard board) {
		List<Move> winningMoves = board.getWinningMoves(current);
		if (!winningMoves.isEmpty()) {
			return winningMoves.get(0);
		}

		List<Move> winningOpponentMoves = board.getWinningMoves(opponent);
		if (!winningOpponentMoves.isEmpty()) {
			return winningOpponentMoves.get(0);
		}

		if (level >= 1) {
			List<Move> forkMoves = board.getForkMoves(current);
			if (!forkMoves.isEmpty()) {
				return forkMoves.get(0);
			}

			if (level >= 2) {
				List<Move> forkOpponentMoves = board.getForkMoves(opponent);
				if (!forkOpponentMoves.isEmpty()) {
					return forkOpponentMoves.get(0);
				}
			}
		}

		return super.getMove(board);
	}

}
