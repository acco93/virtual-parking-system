package isac.client.main;

import isac.client.controller.Client;
import isac.core.log.Logger;

/**
 * 
 * Client application entry point.
 * 
 * @author acco
 *
 */
public class Main {

	public static void main(String[] args) {

		if(args.length < 1){
			Logger.getInstance().error("Please provide the MOM ip address as argument.");
			Logger.getInstance().error("For example: \"localhost\" or \"192.168.1.26\"");
			System.exit(1);
		}
		
		/*
		 * Create a client object.
		 */
		Client client = new Client(args[0]);

		/*
		 * Start some services.
		 */
		client.start();

	}

}
