package isac.server;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import isac.core.log.Logger;
import isac.core.sharedknowledge.R;

/**
 * 
 * It continuously ping clients to indicate that the server is alive.
 * 
 * @author acco
 *
 */
public class OnlineDaemon extends Thread {

	private Channel channel;
	private String momIp;

	public OnlineDaemon(String momIp) {
		
		this.momIp = momIp;
		Logger.getInstance().info("started");

		this.setupMqtt();
	}

	private void setupMqtt() {

		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(momIp);
			Connection connection = factory.newConnection();
			channel = connection.createChannel();
			channel.exchangeDeclare(R.SERVER_HEARTBEAT_CHANNEL, "fanout");
		} catch (IOException | TimeoutException e) {
			Logger.getInstance().error(e.getMessage());
		}

	}

	@Override
	public void run() {

		while (true) {

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Logger.getInstance().error(e.getMessage());
			}

			try {
				channel.basicPublish(R.SERVER_HEARTBEAT_CHANNEL, "", null, "1".getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}
