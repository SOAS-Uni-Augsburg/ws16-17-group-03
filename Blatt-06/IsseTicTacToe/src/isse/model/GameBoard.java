package isse.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a board for given n at a current state
 * 
 * @author isse-soas
 *
 */
public class GameBoard {

	private FieldState[][] board;
	private List<Move> possibleMoves;

	public GameBoard(int n) {
		possibleMoves = new ArrayList<Move>();
		board = new FieldState[n][n];
		for (int i = 0; i < n; ++i) {
			for (int j = 0; j < n; ++j) {
				board[i][j] = FieldState.EMPTY;
				possibleMoves.add(new Move(i, j));
			}
		}
	}

	/**
	 * Default constructor for standard tic tac toe
	 */
	public GameBoard() {
		this(3);
	}

	public FieldState[][] getFieldState() {
		return board;
	}

	public void move(Player player, int row, int col) {
		if (board[row][col] != FieldState.EMPTY)
			throw new RuntimeException("Invalid move, field already taken!");
		possibleMoves.remove(new Move(row, col));
		board[row][col] = player.getFieldState();
	}

	public void undoMove(Player player, int row, int col) {
		if (board[row][col] != player.getFieldState())
			throw new RuntimeException("Invalid move, wrond field state!");
		possibleMoves.add(new Move(row, col));
		board[row][col] = FieldState.EMPTY;
	}

	/**
	 * Read method for play strategies
	 * 
	 * @return
	 */
	public int getSize() {
		return board.length;
	}

	public FieldState read(int row, int col) {
		if (row < 0 || row >= board.length || col < 0 || col >= board.length)
			return null;
		else
			return board[row][col];
	}

	/**
	 * Pretty printing the game board
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(board.length * board.length * 4);
		sb.append('-');
		for (int i = 0; i < board.length; ++i) {
			sb.append("----");
		}
		sb.append("\n");

		for (int i = 0; i < board.length; ++i) {
			for (int j = 0; j < board.length; ++j) {
				if (j == 0)
					sb.append('|');
				sb.append(' ');
				sb.append(board[i][j]);
				sb.append(" |");
			}
			sb.append("\n");
		}
		sb.append('-');
		for (int i = 0; i < board.length; ++i) {
			sb.append("----");
		}
		return sb.toString();
	}

	public void move(Player turn, Move move) {
		move(turn, move.row, move.col);
	}

	public void undoMove(Player turn, Move move) {
		undoMove(turn, move.row, move.col);
	}

	public boolean isFull() {
		for (int i = 0; i < board.length; ++i) {
			for (int j = 0; j < board.length; ++j)
				if (board[i][j] == FieldState.EMPTY)
					return false;
		}
		return true;
	}

	public List<int[]> getLinesFromWinningMove(Move lastMove) {
		List<int[]> winningMoves = new ArrayList<int[]>();
		Player turn = Player.valueOf(board[lastMove.row][lastMove.col].name());

		// check row
		boolean seenDifferent = false;
		for (int col = 0; col < board.length && !seenDifferent; ++col) {
			seenDifferent = seenDifferent
					|| (board[lastMove.row][col] != turn.getFieldState());
			if (!seenDifferent)
				winningMoves.add(new int[] { lastMove.row, col });
		}

		// check col
		seenDifferent = false;
		for (int row = 0; row < board.length && !seenDifferent; ++row) {
			seenDifferent = seenDifferent
					|| (board[row][lastMove.col] != turn.getFieldState());
			if (!seenDifferent)
				winningMoves.add(new int[] { row, lastMove.col });
		}

		// check diagonal (only if lastMove was actually on one of the
		// diagonals)
		if (lastMove.row == lastMove.col) { // main diagonal
			seenDifferent = false;
			for (int d = 0; d < board.length && !seenDifferent; ++d) {
				seenDifferent = seenDifferent
						|| (board[d][d] != turn.getFieldState());
				if (!seenDifferent)
					winningMoves.add(new int[] { d, d });
			}

		}

		// secondary diagonal
		if (lastMove.row + lastMove.col == board.length - 1) {
			seenDifferent = false;
			for (int d = 0; d < board.length && !seenDifferent; ++d) {
				seenDifferent = seenDifferent
						|| (board[d][(board.length - 1) - d] != turn
								.getFieldState());
				if (!seenDifferent)
					winningMoves.add(new int[] { d, (board.length - 1) - d });
			}

		}

		return winningMoves;
	}

	/**
	 * Need to check only the last move
	 * 
	 * @param turn
	 * @param lastMove
	 * @return
	 */
	public boolean isWonBy(Player turn, Move lastMove) {
		// check row
		boolean seenDifferent = false;
		for (int col = 0; col < board.length && !seenDifferent; ++col) {
			seenDifferent = seenDifferent
					|| (board[lastMove.row][col] != turn.getFieldState());
		}
		if (!seenDifferent)
			return true;

		// check col
		seenDifferent = false;
		for (int row = 0; row < board.length && !seenDifferent; ++row) {
			seenDifferent = seenDifferent
					|| (board[row][lastMove.col] != turn.getFieldState());
		}
		if (!seenDifferent)
			return true;

		// check diagonal (only if lastMove was actually on one of the
		// diagonals)
		if (lastMove.row == lastMove.col) { // main diagonal
			seenDifferent = false;
			for (int d = 0; d < board.length && !seenDifferent; ++d) {
				seenDifferent = seenDifferent
						|| (board[d][d] != turn.getFieldState());
			}
			if (!seenDifferent)
				return true;
		}

		// secondary diagonal
		if (lastMove.row + lastMove.col == board.length - 1) {
			seenDifferent = false;
			for (int d = 0; d < board.length && !seenDifferent; ++d) {
				seenDifferent = seenDifferent
						|| (board[d][(board.length - 1) - d] != turn
								.getFieldState());
			}
			if (!seenDifferent)
				return true;
		}

		return false;
	}

	public GameBoard getCopy() {
		int size = this.getSize();
		GameBoard copy = new GameBoard(size);

		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				copy.board[i][j] = this.board[i][j];
			}
		}
		copy.possibleMoves = new ArrayList<>(this.possibleMoves);
		return copy;
	}

	public List<Move> getPossibleMoves() {
		return possibleMoves;
	}
}
