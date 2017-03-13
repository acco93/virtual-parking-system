package isac.client.controller;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import isac.core.data.Position;
import isac.core.message.EnvironmentMessage;
import isac.core.sharedknowledge.R;

/**
 * 
 * It handles the interaction with the virtual environment.
 * 
 * @author acco
 *
 */
public class EnvironmentInteraction {

	private Channel channel;
	private String momIp;

	public EnvironmentInteraction(String momIp) {
		this.momIp = momIp;
		this.setupRabbitMQ();

	}

	private void setupRabbitMQ() {

		try {

			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(momIp);
			Connection connection;
			connection = factory.newConnection();
			channel = connection.createChannel();
			channel.queueDeclare(R.CLIENT_TO_SERVER_QUEUE, false, false, false, null);

		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Occupy a physical parking position.
	 * 
	 * @param position
	 */
	public void park(Position position) {
		this.messageToEnvironment(position, true);
	}

	/**
	 * Release a previously occupied parking position.
	 * 
	 * @param position
	 */
	public void remove(Position position) {
		this.messageToEnvironment(position, false);
	}

	private void messageToEnvironment(Position position, boolean park) {
		EnvironmentMessage msg = new EnvironmentMessage(position, park);
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(msg);

		try {
			channel.basicPublish("", R.ENVIRONMENT_CHANNEL, null, json.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
