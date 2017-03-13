package isac.environment.configurator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import isac.core.log.Logger;
import isac.environment.Environment;
import isac.environment.resources.ER;
import isac.environment.sensor.ParkingSensor;
import isac.environment.sensor.ParkingSensorController;

/**
 * 
 * Define the environment from a file. The file should contain an (m x n) matrix
 * of symbols: s: for a parking sensor element, x: for a street element
 * 
 * @author acco
 *
 */
public class Configurator {

	private String filePath;
	private int lineNumber;
	private int lineDimension;

	public Configurator(String filePath, String momIp) {

		Logger.getInstance().info("started");

		this.filePath = filePath;
		ER.MOM_IP = momIp;

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

		Character[][] charMatrix = this.readFromFile();

		if (charMatrix == null) {
			return false;
		}

		if (lineNumber == 0 || lineDimension == -1) {
			Logger.getInstance().error("are you trying to load an empty file?");
			return false;
		}

		ER.ENV_COLUMNS = lineDimension;
		ER.ENV_ROWS = lineNumber;

		char letter = 'A';
		int inc = 0;

		for (int i = 0; i < this.lineNumber; i++) {

			for (int j = 0; j < this.lineDimension; j++) {

				if (charMatrix[i][j] == 's') {
					inc++;
					if (inc == 10) {
						letter++;
						inc = 0;
					}
					ParkingSensor s = new ParkingSensor(i, j, Character.toString(letter) + "" + inc);

					boolean ok = Environment.getInstance().inject(s);

					if (!ok) {
						Logger.getInstance().error("failed to inject sensor " + i + "," + j);
					} else {
						new ParkingSensorController(s).start();
					}

				}

			}

		}

		Logger.getInstance().info("successfully loaded!");
		Logger.getInstance().info("working ...");
		return true;

	}

	/**
	 * Read the file and return a char matrix
	 * 
	 * @return the char matrix
	 */
	private Character[][] readFromFile() {

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
					Logger.getInstance().error("malformed file. Line " + lineNumber + " has " + splittedLine.length
							+ " characters instead of " + lineDimension);
					return null;
				}

				temporaryStorage.add(splittedLine);

				line = reader.readLine();
			}

		} catch (IOException e) {

			Logger.getInstance().error(e.getMessage());
			return null;
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {

				Logger.getInstance().error(e.getMessage());

			}
		}

		// map this strange structure to a char matrix

		Character[][] matrix = new Character[lineNumber + 1][lineDimension + 1];
		for (int i = 0; i < lineNumber; i++) {
			for (int j = 0; j < lineDimension; j++) {
				matrix[i][j] = temporaryStorage.get(i)[j].charAt(0);
			}
		}

		return matrix;
	}

}
