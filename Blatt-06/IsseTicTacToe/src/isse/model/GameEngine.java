package isse.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import isse.control.ControlAction;
import isse.model.strategies.DelayedPlayStrategy;
import isse.model.strategies.InteractiveUIStrategy;
import isse.model.strategies.PlayerBasedStrategy;

/**
 * Deals with the setup of the game, connecting between player moves etc.
 * 
 * @author soas-isse
 *
 */
public class GameEngine extends Observable {
	private GameBoard board;
	public boolean surpressOutput = false;

	public GameBoard getBoard() {
		return board;
	}

	public void setBoard(GameBoard board) {
		this.board = board;
	}

	private Map<Player, PlayStrategy> strategies;

	public GameEngine() {
		board = new GameBoard();
		strategies = new HashMap<Player, PlayStrategy>(2);
	}

	public Player play() {
		Player turn = Player.CROSSES;
		Player winner = null;

		if (strategies.get(Player.CROSSES) == null
				|| strategies.get(Player.NOUGHTS) == null)
			throw new RuntimeException(
					"You forgot to register playing strategies");

		boolean terminated = false;
		int illegalMoves = 10; // to prevent endless loops
		String gameMessage = "";

		while (!terminated) {
			PlayStrategy nextStrategy = strategies.get(turn);
			setChanged();
			if (nextStrategy instanceof InteractiveUIStrategy)
				notifyObservers(ControlAction.INTERACTIVE_MODE);
			else
				notifyObservers(ControlAction.CPU_MODE);

			int countIllegals = 0;

			Move move = null;
			emitMessage("Player " + turn + "' s turn. ("
					+ nextStrategy.getClass().getSimpleName() + ")");

			while (countIllegals < illegalMoves) {
				move = nextStrategy.getMove(board);
				try {
					board.move(turn, move);
					break;
				} catch (RuntimeException re) {
					++countIllegals;
					System.out.println("Illegal Move: " + move);
				}
			}

			if (countIllegals >= illegalMoves) {
				gameMessage = "Player " + turn
						+ " exceeded illegal move threshold (" + countIllegals
						+ ")";
				System.out.println(gameMessage);
				terminated = true;
			} else {
				if (!surpressOutput) {
					System.out.println("Player " + turn + " chose " + move);
					System.out.println(board);
				}
				this.setChanged();
				this.notifyObservers(board);

				// was that a winning move?
				if (board.isWonBy(turn, move)) {
					gameMessage = "Player " + turn + " has won!";
					terminated = true;
					winner = turn;
				} else if (board.isFull()) {
					// drawn
					gameMessage = "Game is drawn.";
					terminated = true;
				}
			}
			turn = (turn == Player.CROSSES) ? Player.NOUGHTS : Player.CROSSES;
		}
		emitMessage("Game finished: " + gameMessage);
		setChanged();
		notifyObservers(ControlAction.GAME_ENDED);
		return winner;
	}

	private void emitMessage(String string) {
		if (!surpressOutput) {
			System.out.println(string);
		}
		setChanged();
		notifyObservers(string);
	}

	public void registerStrategy(Player player, PlayStrategy strategy) {
		strategies.put(player, strategy);

		if (strategy instanceof DelayedPlayStrategy) {
			strategy = ((DelayedPlayStrategy) strategy).getInnerStrategy();
		}
		if (PlayerBasedStrategy.class.isAssignableFrom(strategy.getClass())) {
			((PlayerBasedStrategy) strategy).setPlayer(player);
		}

	}
}
