package isse.model.strategies;

import java.util.ArrayList;
import java.util.List;

import isse.model.FieldState;
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
		List<Move> triedMoves = new ArrayList<Move>();
		int i, j;
		int maxIndex = (board.getSize() - 1);
		int maxTries = (board.getSize()) * (board.getSize());
		do {
			Move actMove;
			do {
				i = (int) Math.round((Math.random() * maxIndex));
				j = (int) Math.round((Math.random() * maxIndex));
				actMove = new Move(i, j);
			} while (triedMoves.contains(actMove));

			triedMoves.add(actMove);

			if (board.read(i, j) == FieldState.EMPTY) {
				return actMove;
			}
		} while (triedMoves.size() < maxTries);
		return null;
	}

}
