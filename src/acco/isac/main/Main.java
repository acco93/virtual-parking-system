package acco.isac.main;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import acco.isac.configurator.Configurator;
import acco.isac.debugui.UserInterface;
import acco.isac.environment.Environment;
import acco.isac.sensor.ParkingSensor;
import acco.isac.sensor.ParkingSensorController;
import acco.isac.server.Server;

public class Main {

	public static void main(String[] args) {


		Configurator configurator = new Configurator("maps/example.txt");
		boolean ok = configurator.load();
		
		if(!ok){
			System.err.println("Error while loading file.");
			System.exit(1);
		}

		try {
			new Server();
		} catch (IOException | TimeoutException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		

		UserInterface ui = new UserInterface();

		ui.setEnvironment(Environment.getInstance());

	}

}
