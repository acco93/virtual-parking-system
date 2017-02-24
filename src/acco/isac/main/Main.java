package acco.isac.main;

import acco.isac.environment.Environment;
import acco.isac.sensor.ParkingSensor;
import acco.isac.ui.UserInterface;

public class Main {

	public static void main(String[] args) {

		Environment e = new Environment(14, 10);
		
		e.inject(new ParkingSensor(2, 2), 2, 2);
		
		e.inject(new ParkingSensor(3, 3), 3, 3);
		
		UserInterface ui = new UserInterface();
		
		ui.setEnvironment(e);
		
	}

}
