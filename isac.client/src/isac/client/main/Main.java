package isac.client.main;

import isac.client.controller.Client;

/**
 * 
 * Client application entry point.
 * 
 * @author acco
 *
 */
public class Main {

	public static void main(String[] args) {

		/*
		 * Create a client object.
		 */
		Client client = new Client();

		/*
		 * Start some services.
		 */
		client.start();

	}

}
