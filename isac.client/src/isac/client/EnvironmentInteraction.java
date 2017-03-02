package isac.client;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import isac.core.data.EnvironmentMessage;
import isac.core.data.Position;
import isac.core.sharedknowledge.R;

public class EnvironmentInteraction {

	private Channel channel;

	public EnvironmentInteraction() {

		this.setupRabbitMQ();

	}

	private void setupRabbitMQ() {

		try {

			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			Connection connection;
			connection = factory.newConnection();
			channel = connection.createChannel();
			channel.queueDeclare(R.CLIENT_TO_SERVER_QUEUE, false, false, false, null);

		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}

	}

	public void park(Position position) {
		this.messageToEnvironment(position, true);
	}

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
