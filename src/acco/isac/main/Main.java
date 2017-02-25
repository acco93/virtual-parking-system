package acco.isac.main;

import acco.isac.configurator.Configurator;
import acco.isac.server.Server;

public class Main {

	public static void main(String[] args) {


		Configurator configurator = new Configurator("maps/example.txt");
		boolean ok = configurator.load();
		
		if(!ok){
			System.err.println("Error while loading file.");
			System.exit(1);
		}

		new Server().start();
		
		

		// UserInterface ui = new UserInterface();

		// ui.setEnvironment(Environment.getInstance());

	}

}
