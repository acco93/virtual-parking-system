package isac.environment.main;

import isac.core.log.Logger;
import isac.environment.configurator.Configurator;

/**
 * Environment entry point.
 * 
 * @author acco
 *
 */
public class Main {

	public static void main(String[] args) {

		if (args.length < 2) {
			Logger.getInstance().error("Please provide a map file as first argument.");
			Logger.getInstance().error("For example: maps/example.txt");
			Logger.getInstance().error("Please provide the MOM ip address as second argument.");
			Logger.getInstance().error("For example: \"localhost\" or \"192.168.1.26\"");
			System.exit(1);
		}

		/*
		 * Setup the environment and start the sensors.
		 */
		Configurator configurator = new Configurator(args[0], args[1]);
		boolean ok = configurator.load();

		if (!ok) {
			Logger.getInstance().error("Something has gone wrong ...");
		}

	}

}
