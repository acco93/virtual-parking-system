package acco.isac.main;

import acco.isac.client.Client;
import acco.isac.configurator.Configurator;
import acco.isac.server.Server;

public class Main {

	public static void main(String[] args) {

		/*
		 * Setup the environment and start the sensors.
		 */
		Configurator configurator = new Configurator("maps/example.txt");
		configurator.load();

		new Server().start();

		new Client().start();
		
		
		
	}

}
