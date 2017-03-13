package isac.server.main;

import isac.core.log.Logger;
import isac.server.Server;

/**
 * Server application entry point.
 * 
 * @author acco
 *
 */
public class Main {

	public static void main(String[] args) {

		if (args.length < 1) {
			Logger.getInstance().error("Please provide the MOM ip address as argument.");
			Logger.getInstance().error("For example: \"localhost\" or \"192.168.1.26\"");
			System.exit(1);
		}

		Server server = new Server(args[0]);

		server.start();
	}

}
