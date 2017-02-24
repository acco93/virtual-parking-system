package acco.isac.environment;

import java.util.Arrays;

import acco.isac.sensor.ISensor;

public class Environment {

	private int width;
	private int height;
	private Cell[][] grid;

	/**
	 * 
	 * Create an empty environment.
	 * 
	 * @param width
	 * @param height
	 */
	public Environment(int width, int height) {
		this.width = width;
		this.height = height;

		this.grid = new Cell[this.width][this.height];

		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				this.grid[i][j] = new Cell();
			}
		}

	}

	/**
	 * Inject a sensor in position (x,y)
	 * 
	 * @param sensor
	 *            the sensor
	 * @param x
	 *            x-value
	 * @param y
	 *            y-value
	 * @return true if the operation successfully completes, false otherwise.
	 */
	public boolean inject(ISensor sensor, int x, int y) {

		if (x < 0 || x > this.width || y < 0 || y > this.height) {
			return false;
		}

		return this.grid[x][y].inject(sensor);
	}

	@Override
	public String toString() {
		return "Environment [width=" + width + ", height=" + height + ", grid=" + Arrays.deepToString(grid) + "]";
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Cell[][] getGrid() {
		return grid;
	}

	
	
}
