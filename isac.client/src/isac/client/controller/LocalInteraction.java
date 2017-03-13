package isac.client.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import isac.client.model.Storage;
import isac.client.view.UserInterface;
import isac.core.data.Position;
import isac.core.log.Logger;
import isac.core.message.LocalReply;
import isac.core.message.LocalRequest;
import isac.core.message.LocalRequestType;
import isac.core.sharedknowledge.R;

/**
 * It sends and receives messages to and from sensors when the server is not
 * available.
 * 
 * @author acco
 *
 */
public class LocalInteraction {

	private UserInterface ui;
	private Channel channel;
	private String privateReplyChannel;
	private int requestIndex;
	private HashMap<String, Integer> requestsId;
	private String momIp;

	public LocalInteraction(UserInterface ui, String momIp) {
		this.momIp = momIp;
		this.ui = ui;
		this.requestIndex = 0;
		this.privateReplyChannel = UUID.randomUUID().toString();
		this.requestsId = new HashMap<>();
		this.setupRabbitMQ();
	}

	private void setupRabbitMQ() {

		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(momIp);
			Connection connection = factory.newConnection();
			channel = connection.createChannel();
			channel.exchangeDeclare(R.LOCAL_INTERACTIONS_CHANNEL, "fanout");

			channel.queueDeclare(this.privateReplyChannel, false, false, false, null);

		} catch (IOException | TimeoutException e) {
			Logger.getInstance().error(e.getMessage());
		}

		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {

				String message = new String(body, "UTF-8");

				Gson gson = new GsonBuilder().create();
				LocalReply reply = gson.fromJson(message, LocalReply.class);

				int distance = Math
						.abs(Storage.getInstance().getUserPosition().getRow() - reply.getDestination().getRow())
						+ Math.abs(Storage.getInstance().getUserPosition().getColumn()
								- reply.getDestination().getColumn());

				Integer bestReplyDistance = requestsId.get(reply.getId());

				if (bestReplyDistance == null || distance < bestReplyDistance) {
					requestsId.put(reply.getId(), distance);
					ui.setAirDistanceString(Integer.toString(distance));
					ui.setQueriedPosition("" + reply.getDestination());
					String airPath = computeAirPath(Storage.getInstance().getUserPosition(), reply.getDestination());
					ui.setAirPath(airPath);

				} else {
					/*
					 * Skip worse replies
					 */
				}

			}

			/*
			 * Define a string of steps to reach the destination (air path).
			 */
			private String computeAirPath(Position source, Position destination) {

				boolean toRight = false;
				boolean toBottom = false;

				int xSteps = source.getColumn() - destination.getColumn();
				int ySteps = source.getRow() - destination.getRow();

				if (xSteps < 0) {
					toRight = true;
				}

				if (ySteps < 0) {
					toBottom = true;
				}

				String path = "";

				for (int i = 0; i < Math.abs(xSteps); i++) {
					if (toRight) {
						path += "\u2192 "; // right
					} else {
						path += "\u2190 "; // left
					}
				}

				for (int i = 0; i < Math.abs(ySteps); i++) {
					if (toBottom) {
						path += "\u2193 "; // down
					} else {
						path += "\u2191 "; // up
					}
				}

				if (Math.abs(xSteps) + Math.abs(ySteps) == 0) {
					path = "in place";
				}

				return path;
			}

		};

		/*
		 * Replies will be received in this private channel
		 */
		try {
			channel.basicConsume(this.privateReplyChannel, true, consumer);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Send a search near free parking place request.
	 */
	public void searchNearFreePark() {
		this.generalRequest(LocalRequestType.PARK);
	}

	/**
	 * Send a locate my car request.
	 */
	public void locateParkedCar() {
		this.generalRequest(LocalRequestType.LOCATE);
	}

	/**
	 * Contact the nearest sensor, if one is available.
	 * 
	 * @param type
	 *            request type
	 */
	private void generalRequest(LocalRequestType type) {

		Position carPosition = null;

		if (Storage.getInstance().getCarPosition().isPresent()) {
			carPosition = Storage.getInstance().getCarPosition().get();
		}

		LocalRequest request = new LocalRequest("id" + this.requestIndex, type, Storage.getInstance().getUserPosition(),
				carPosition, this.privateReplyChannel);
		this.requestIndex++;
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(request);
		try {
			channel.basicPublish(R.LOCAL_INTERACTIONS_CHANNEL, "", null, json.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
