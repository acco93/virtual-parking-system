package isac.environment.main;

import acco.isac.configurator.Configurator;

public class Main {

	public static void main(String[] args) {

		/*
		 * Setup the environment and start the sensors.
		 */
		Configurator configurator = new Configurator("maps/example.txt");
		configurator.load();

	}

}
