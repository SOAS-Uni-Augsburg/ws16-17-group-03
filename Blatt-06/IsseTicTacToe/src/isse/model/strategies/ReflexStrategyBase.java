package isse.model.strategies;

import java.util.ArrayList;
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
public abstract class ReflexStrategyBase extends RandomStrategy
		implements PlayerBasedStrategy {
	private int level;
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
		List<Move> winningMoves = getWinningMoves(board, current);
		if (!winningMoves.isEmpty()) {
			return winningMoves.get(0);
		}

		List<Move> winningOpponentMoves = getWinningMoves(board, opponent);
		if (!winningOpponentMoves.isEmpty()) {
			return winningOpponentMoves.get(0);
		}

		if (level >= 1) {
			List<Move> forkMoves = getForkMoves(board, current);
			if (!forkMoves.isEmpty()) {
				return forkMoves.get(0);
			}

			if (level >= 2) {
				List<Move> forkOpponentMoves = getForkMoves(board, opponent);
				if (!forkOpponentMoves.isEmpty()) {
					return forkOpponentMoves.get(0);
				}
			}
		}

		return super.getMove(board); // random move
	}

	/**
	 * Get the Winning moves for this Player
	 * 
	 * @param player
	 * @return the winning moves
	 */
	public List<Move> getWinningMoves(GameBoard board, Player player) {
		List<Move> winning = new ArrayList<Move>();

		List<Move> possibleMoves = new ArrayList<>(board.getPossibleMoves());
		for (Move move : possibleMoves) {

			board.move(player, move);
			if (board.isWonBy(player, move)) {
				winning.add(move);
			}
			board.undoMove(player, move);
		}

		return winning;
	}

	/**
	 * Get the Forking moves for this Player
	 * 
	 * @param player
	 * @return the forking moves
	 */
	public List<Move> getForkMoves(GameBoard board, Player player) {

		List<Move> forking = new ArrayList<Move>();
		List<Move> possibleMoves = new ArrayList<>(board.getPossibleMoves());
		for (Move move : possibleMoves) {
			board.move(player, move);
			List<Move> winningMoves = getWinningMoves(board, player);
			if (winningMoves.size() > 1) {
				forking.add(move);
			}
			board.undoMove(player, move);
		}
		return forking;

	}

}
