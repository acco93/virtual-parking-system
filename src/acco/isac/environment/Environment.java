package acco.isac.environment;

import acco.isac.sharedknowledge.R;

public class Environment {

	private static Environment instance = new Environment();

	private int width;
	private int height;
	private Cell[][] sensorsLayer;
	private boolean[][] carsLayer;

	/**
	 * 
	 * Create an empty environment.
	 * 
	 * @param width
	 * @param height
	 */
	public Environment() {
		this.width = R.ENV_WIDTH;
		this.height = R.ENV_HEIGHT;

		this.sensorsLayer = new Cell[this.width][this.height];
		this.carsLayer = new boolean[this.width][this.height];

		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
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

		int x = element.getPosition().getX();
		int y = element.getPosition().getY();

		if (x < 0 || x > this.width || y < 0 || y > this.height) {
			return false;
		}

		return this.sensorsLayer[x][y].inject(element);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Cell[][] getSensorsLayer() {
		return sensorsLayer;
	}

	public boolean[][] getCarsLayer() {
		return carsLayer;
	}

}
