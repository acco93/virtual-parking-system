package acco.isac.environment;

import acco.isac.sharedknowledge.R;

public class Environment {

	private static Environment instance = new Environment();

	private int columns;
	private int rows;
	private Cell[][] sensorsLayer;
	private boolean[][] carsLayer;

	/**
	 * 
	 * Create an empty environment.
	 * 
	 * @param columns
	 * @param rows
	 */
	public Environment() {
		this.columns = R.ENV_COLUMNS;
		this.rows = R.ENV_ROWS;

		System.out.println(this.columns+" "+this.rows);
		
		this.sensorsLayer = new Cell[this.rows][this.columns];
		this.carsLayer = new boolean[this.rows][this.columns];

		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.columns; j++) {
				this.sensorsLayer[i][j] = new Cell();
				this.carsLayer[i][j] = false;
			}
		}

	}

	public static Environment getInstance() {
		return Environment.instance;
	}

	/**
	 * Inject an element in position (x,y)
	 * 
	 * @param element
	 *            the element
	 * @param x
	 *            x-value
	 * @param y
	 *            y-value
	 * @return true if the operation successfully completes, false otherwise.
	 */
	public boolean inject(IEnvironmentElement element) {

		int row = element.getPosition().getRow();
		int column = element.getPosition().getColumn();

		if (row < 0 || row > this.rows-1 || column < 0 || column > this.columns-1) {
			return false;
		}

		return this.sensorsLayer[row][column].inject(element);
	}

	public int getColumns() {
		return columns;
	}

	public int getRows() {
		return rows;
	}

	public Cell[][] getSensorsLayer() {
		return sensorsLayer;
	}

	public boolean[][] getCarsLayer() {
		return carsLayer;
	}

}
