package acco.isac.configurator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import acco.isac.environment.Environment;
import acco.isac.log.Logger;
import acco.isac.sensor.ParkingSensor;
import acco.isac.sensor.ParkingSensorController;
import acco.isac.sharedknowledge.R;

/**
 * 
 * Define the environment from a file. The file should contain an (m x n) matrix
 * of symbols: - s: for a parking sensor element - x: for a street element
 * 
 * @author acco
 *
 */
public class Configurator {

	private String filePath;
	private int lineNumber;
	private int lineDimension;

	public Configurator(String filePath) {

		Logger.getInstance().info("started");

		this.filePath = filePath;

		this.lineNumber = 0;
		this.lineDimension = 0;

		Logger.getInstance().info("file path setted: " + filePath);

	}

	/**
	 * Set up the environment.
	 * 
	 * @return
	 */
	public boolean load() {

		Logger.getInstance().info("trying to load " + this.filePath);

		char[][] charMatrix = this.readFromFile();

		if (lineNumber == 0 || lineDimension == -1) {
			Logger.getInstance().error("are you trying to load an empty file?");
			System.exit(1);
		}

		R.ENV_COLUMNS = lineDimension;
		R.ENV_ROWS = lineNumber;

		for (int i = 0; i < this.lineNumber; i++) {

			for (int j = 0; j < this.lineDimension; j++) {

				if (charMatrix[i][j] == 's') {

					ParkingSensor s = new ParkingSensor(i, j, "s" + i + "_" + j);
					new ParkingSensorController(s).start();
					boolean ok = Environment.getInstance().inject(s);

					if (!ok) {
						Logger.getInstance().error("failed to inject sensor " + i + "," + j);

					}

				}

			}

		}

		Logger.getInstance().info("successfully loaded!");
		return true;

	}

	/**
	 * Read the file and return a char matrix
	 * 
	 * @return the char matrix
	 */
	private char[][] readFromFile() {

		BufferedReader reader = null;

		List<String[]> temporaryStorage = new LinkedList<>();

		this.lineNumber = 0;
		this.lineDimension = -1;

		try {

			File file = new File(filePath);

			reader = new BufferedReader(new FileReader(file));

			String line = reader.readLine();

			while (line != null) {
				lineNumber++;
				String[] splittedLine = line.split(" ");

				if (lineDimension == -1) {
					// not already set
					// the first line determines the grid width
					lineDimension = splittedLine.length;
				} else if (lineDimension != splittedLine.length) {
					// malformed file
					Logger.getInstance().error("malformed file. Line " + lineNumber + " has " + splittedLine.length + " characters instead of "
							+ lineDimension);
					System.exit(1);
				}

				temporaryStorage.add(splittedLine);

				line = reader.readLine();
			}

		} catch (IOException e) {

			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		// map this strange structure to a char matrix

		char[][] matrix = new char[lineNumber + 1][lineDimension + 1];
		for (int i = 0; i < lineNumber; i++) {
			for (int j = 0; j < lineDimension; j++) {
				matrix[i][j] = temporaryStorage.get(i)[j].charAt(0);
			}
		}

		return matrix;
	}

}
