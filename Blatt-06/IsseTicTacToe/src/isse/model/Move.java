package isse.model;

/**
 * Just representing the tuple of a move
 * 
 * @author isse-soas
 *
 */
public class Move {

	public Move(int row, int col) {
		this.row = row;
		this.col = col;
	}

	public int row;
	public int col;

	@Override
	public String toString() {
		return String.format("[%d, %d]", row, col);
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Move)) {
			return false;
		}
		Move otherMove = (Move) other;
		if (this.col == otherMove.col && this.row == otherMove.row) {
			return true;
		} else {
			return false;
		}

	}
}
