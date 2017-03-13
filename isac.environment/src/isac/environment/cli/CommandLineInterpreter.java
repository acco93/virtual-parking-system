package isac.environment.cli;

import java.util.Scanner;

import isac.core.log.Logger;
import isac.environment.Environment;
import isac.environment.sensor.ParkingSensor;
import isac.environment.sensor.ParkingSensorController;

/**
 * 
 * Simple command line interpreter.
 * 
 * @author acco
 *
 */
public class CommandLineInterpreter extends Thread {

	private Scanner scanner;

	public CommandLineInterpreter() {

		scanner = new Scanner(System.in);
		Logger.getInstance().info("started");
	}

	@Override
	public void run() {
		while (true) {

			Logger.getInstance().info("Type a command:");
			String command = scanner.nextLine();

			String[] array = command.split(" ");

			/*
			 * Add command
			 */
			if (array.length == 4 && array[0].equals("add")) {

				try {

					int row = Integer.parseInt(array[2]);
					int column = Integer.parseInt(array[3]);

					this.injectNewSensor(array[1], row, column);

				} catch (NumberFormatException e) {
					Logger.getInstance().error("command signature: add String int int");
				}

			} else {
				Logger.getInstance().error("Unsupported command");
			}

		}
	}

	/*
	 * Add a new sensor in the required position.
	 */
	private void injectNewSensor(String uniqueName, int row, int column) {

		ParkingSensor s = new ParkingSensor(row, column, uniqueName);

		boolean ok = Environment.getInstance().inject(s);

		if (!ok) {
			Logger.getInstance().error("failed to inject sensor " + row + "," + column);

		} else {
			new ParkingSensorController(s).start();
			Logger.getInstance().info("Done!");
		}

	}

}
