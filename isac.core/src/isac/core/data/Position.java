package isac.core.data;

/**
 * 
 * An immutable 2d-position class.
 * 
 * @author acco
 *
 */
public class Position {

	private int row;
	private int column;

	public Position(int row, int column) {
		this.row = row;
		this.column = column;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	@Override
	public String toString() {
		return "Position [row=" + row + ", column=" + column + "]";
	}

}
