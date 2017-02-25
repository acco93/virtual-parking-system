package acco.isac.configurator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import acco.isac.environment.Environment;
import acco.isac.sensor.ParkingSensor;
import acco.isac.sensor.ParkingSensorController;
import acco.isac.sharedknowledge.R;

public class Configurator {

	private String filePath;

	public Configurator(String filePath) {

		this.filePath = filePath;

	}

	public boolean load() {

		List<String[]> charMatrix = this.readFromFile();

		R.ENV_HEIGHT = charMatrix.size();
		R.ENV_WIDTH = charMatrix.get(0).length;

		for (int i = 0; i < charMatrix.size(); i++) {

			String[] row = charMatrix.get(i);

			for (int j = 0; j < row.length; j++) {

				if (row[j].equals("s")) {

					ParkingSensor s = new ParkingSensor(j, i, "s" + j + i);
					new ParkingSensorController(s, 1000).start();
					Environment.getInstance().inject(s);

				}

			}

		}

		return true;

	}

	private List<String[]> readFromFile() {
		BufferedReader reader = null;

		List<String[]> temporaryStorage = new LinkedList<>();

		try {

			File file = new File(filePath);

			reader = new BufferedReader(new FileReader(file));

			String line = reader.readLine();

			while (line != null) {
				String[] splittedLine = line.split(" ");

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

		return temporaryStorage;
	}

}
