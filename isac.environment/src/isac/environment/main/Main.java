package isac.environment.main;

import isac.environment.configurator.Configurator;

/**
 * Environment entry point.
 * 
 * @author acco
 *
 */
public class Main {

	public static void main(String[] args) {

		/*
		 * Setup the environment and start the sensors.
		 */
		Configurator configurator = new Configurator("maps/example.txt");
		configurator.load();

	}

}
