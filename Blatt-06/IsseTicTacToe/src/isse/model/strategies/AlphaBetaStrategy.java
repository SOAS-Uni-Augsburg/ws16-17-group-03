package isse.model.strategies;

import java.util.ArrayList;
import java.util.List;

import isse.model.FieldState;
import isse.model.GameBoard;
import isse.model.Move;
import isse.model.Player;

/**
 * Takes the first free field
 * 
 * @author isse-soas
 *
 */
public class AlphaBetaStrategy extends RandomStrategy
		implements PlayerBasedStrategy {

	private int SUCHTIEFE = 9;

	private Player current, opponent;
	private GameBoard workingBoard;

	private int _counter, _sum = 0;
	private int temp = 0;

	public void setPlayer(Player player) {
		current = player;
		opponent = (player == Player.CROSSES) ? Player.NOUGHTS : Player.CROSSES;
	}

	public void setSuchtiefe(int tiefe) {
		SUCHTIEFE = tiefe;
	}

	@Override
	public Move getMove(GameBoard board) {
		_counter = -1;
		this.workingBoard = board.getCopy();
		int[] result = minimax(SUCHTIEFE, current, Integer.MIN_VALUE,
				Integer.MAX_VALUE); // depth, max turn
		_sum += _counter;
		// System.out.println("Current: " + current.name());
		// System.out.println("Sum: " + _sum + "\ncounter: " + _counter);
		return new Move(result[1], result[2]); // row, col
	}

	/**
	 * Recursive minimax at level of depth for either maximizing or minimizing
	 * player. Return int[3] of {score, row, col}
	 */
	private int[] minimax(int depth, Player player, int alpha, int beta) {
		_counter++;
		// Generate possible next moves in a List of int[2] of {row, col}.
		List<int[]> nextMoves = generateMoves();

		// mySeed is maximizing; while oppSeed is minimizing
		int score;
		int bestRow = -1;
		int bestCol = -1;

		if (nextMoves.isEmpty() || depth == 0) {
			// Gameover or depth reached, evaluate score
			score = evaluate();
			return new int[] { score, bestRow, bestCol };
		} else {
			for (int[] move : nextMoves) {
				// Try this move for the current "player"
				FieldState[][] cells = workingBoard.getFieldState();
				cells[move[0]][move[1]] = player.getFieldState();
				if (player == current) { // mySeed (computer) is maximizing
											// player
					score = minimax(depth - 1, opponent, alpha, beta)[0];
					if (score > alpha) {
						alpha = score;
						bestRow = move[0];
						bestCol = move[1];
					}
				} else { // oppSeed is minimizing player
					score = minimax(depth - 1, current, alpha, beta)[0];
					if (score < beta) {
						beta = score;
						bestRow = move[0];
						bestCol = move[1];
					}
				}
				// Undo move
				cells[move[0]][move[1]] = FieldState.EMPTY;

				// cut-off
				if (alpha >= beta)
					break;
			}

		}
		return new int[] { (player == current) ? alpha : beta, bestRow,
				bestCol };
	}

	/**
	 * Find all valid next moves. Return List of moves in int[2] of {row, col}
	 * or empty list if gameover
	 */
	private List<int[]> generateMoves() {
		List<int[]> nextMoves = new ArrayList<int[]>(); // allocate List

		// If gameover, i.e., no next move
		if (hasWon(current) || hasWon(opponent)) {
			return nextMoves; // return empty list
		}

		// Search for empty cells and add to the List
		for (int row = 0; row < 3; ++row) {
			for (int col = 0; col < 3; ++col) {
				if (workingBoard
						.getFieldState()[row][col] == FieldState.EMPTY) {
					nextMoves.add(new int[] { row, col });
				}
			}
		}
		return nextMoves;
	}

	/**
	 * The heuristic evaluation function for the current board
	 * 
	 * @Return +100, +10, +1 for EACH 3-, 2-, 1-in-a-line for computer. -100,
	 *         -10, -1 for EACH 3-, 2-, 1-in-a-line for opponent. 0 otherwise
	 */
	public int evaluate() {
		int score = 0;
		// Evaluate score for each of the 8 lines (3 rows, 3 columns, 2
		// diagonals)
		score += evaluateLine(0, 0, 0, 1, 0, 2); // row 0
		score += evaluateLine(1, 0, 1, 1, 1, 2); // row 1
		score += evaluateLine(2, 0, 2, 1, 2, 2); // row 2
		score += evaluateLine(0, 0, 1, 0, 2, 0); // col 0
		score += evaluateLine(0, 1, 1, 1, 2, 1); // col 1
		score += evaluateLine(0, 2, 1, 2, 2, 2); // col 2
		score += evaluateLine(0, 0, 1, 1, 2, 2); // diagonal
		score += evaluateLine(0, 2, 1, 1, 2, 0); // alternate diagonal
		return score;
	}

	/**
	 * The heuristic evaluation function for the given line of 3 cells
	 * 
	 * @Return +100, +10, +1 for 3-, 2-, 1-in-a-line for computer. -100, -10, -1
	 *         for 3-, 2-, 1-in-a-line for opponent. 0 otherwise
	 */
	private int evaluateLine(int row1, int col1, int row2, int col2, int row3,
			int col3) {
		int score = 0;

		// First cell
		if (workingBoard.getFieldState()[row1][col1] == current
				.getFieldState()) {
			score = 1;
		} else if (workingBoard.getFieldState()[row1][col1] == opponent
				.getFieldState()) {
			score = -1;
		}

		// Second cell
		if (workingBoard.getFieldState()[row2][col2] == current
				.getFieldState()) {
			if (score == 1) { // cell1 is mySeed
				score = 10;
			} else if (score == -1) { // cell1 is oppSeed
				return 0;
			} else { // cell1 is empty
				score = 1;
			}
		} else if (workingBoard.getFieldState()[row2][col2] == opponent
				.getFieldState()) {
			if (score == -1) { // cell1 is oppSeed
				score = -10;
			} else if (score == 1) { // cell1 is mySeed
				return 0;
			} else { // cell1 is empty
				score = -1;
			}
		}

		// Third cell
		if (workingBoard.getFieldState()[row3][col3] == current
				.getFieldState()) {
			if (score > 0) { // cell1 and/or cell2 is mySeed
				score *= 10;
			} else if (score < 0) { // cell1 and/or cell2 is oppSeed
				return 0;
			} else { // cell1 and cell2 are empty
				score = 1;
			}
		} else if (workingBoard.getFieldState()[row3][col3] == opponent
				.getFieldState()) {
			if (score < 0) { // cell1 and/or cell2 is oppSeed
				score *= 10;
			} else if (score > 1) { // cell1 and/or cell2 is mySeed
				return 0;
			} else { // cell1 and cell2 are empty
				score = -1;
			}
		}
		return score;
	}

	private int[] winningPatterns = { 0b111000000, 0b000111000, 0b000000111, // rows
			0b100100100, 0b010010010, 0b001001001, // cols
			0b100010001, 0b001010100 // diagonals
	};

	/** Returns true if thePlayer wins */
	private boolean hasWon(Player thePlayer) {
		int pattern = 0b000000000; // 9-bit pattern for the 9 cells
		for (int row = 0; row < 3; ++row) {
			for (int col = 0; col < 3; ++col) {
				if (workingBoard.getFieldState()[row][col] == thePlayer
						.getFieldState()) {
					pattern |= (1 << (row * 3 + col));
				}
			}
		}
		for (int winningPattern : winningPatterns) {
			if ((pattern & winningPattern) == winningPattern)
				return true;
		}
		return false;
	}

}
