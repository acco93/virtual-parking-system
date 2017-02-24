package acco.isac.environment;

/**
 * 
 * An immutable 2d-position class.
 * 
 * @author acco
 *
 */
public class Position {

	private int x;
	private int y;

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

}
