package isse.model.strategies;

import java.util.List;

import isse.model.GameBoard;
import isse.model.Move;
import isse.model.PlayStrategy;

/**
 * Takes the first free field
 * 
 * @author isse-soas
 *
 */
public class RandomStrategy implements PlayStrategy {

	@Override
	public Move getMove(GameBoard board) {

		List<Move> possibleMoves = board.getPossibleMoves();
		int size = possibleMoves.size();

		if (size == 0) {
			return null;
		}
		int randomMove = (int) Math.round((Math.random() * (size - 1)));
		return possibleMoves.get(randomMove);
	}

}
