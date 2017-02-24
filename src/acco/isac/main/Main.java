package acco.isac.main;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import acco.isac.debugui.UserInterface;
import acco.isac.environment.Environment;
import acco.isac.sensor.ParkingSensor;
import acco.isac.sensor.ParkingSensorController;
import acco.isac.server.Server;

public class Main {

	public static void main(String[] args) {

		Environment e = new Environment();

		try {
			new Server();
		} catch (IOException | TimeoutException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		ParkingSensor s0 = new ParkingSensor(2, 2, "s0");
		ParkingSensor s1 = new ParkingSensor(5, 7, "s1");
		
		
		e.inject(s0);
		e.inject(s1);

		
		new ParkingSensorController(s0, 50).start();
		new ParkingSensorController(s1, 500).start();

		UserInterface ui = new UserInterface();

		ui.setEnvironment(e);

	}

}
