package isac.client.controller;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import isac.client.model.Storage;
import isac.client.utils.ClientUtils;
import isac.core.algorithms.GraphFromMap;
import isac.core.data.SensorRepresentation;
import isac.core.datastructures.Graph;
import isac.core.log.Logger;
import isac.core.sharedknowledge.R;

/**
 * It receives raw world information (a map of sensor names to sensor states)
 * from the server and build the internal client world representation as a graph
 * of sensor and street nodes .
 * 
 * @author acco
 *
 */
public class SubscriberDaemon {

	private Channel channel;
	private String queueName;
	private ClientUtils utils;
	private String momIp;

	public SubscriberDaemon(ClientUtils utils, String momIp) {

		this.utils = utils;
		this.momIp = momIp;

		Logger.getInstance().info("started");

		this.setupMqtt();

	}

	private void setupMqtt() {

		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(momIp);
			Connection connection;
			connection = factory.newConnection();
			channel = connection.createChannel();

			channel.exchangeDeclare(R.SERVER_TO_CLIENTS_CHANNEL, "fanout");
			queueName = channel.queueDeclare().getQueue();
			channel.queueBind(queueName, R.SERVER_TO_CLIENTS_CHANNEL, "");
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}

	}

	public void start() {

		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String message = new String(body, "UTF-8");

				Gson gson = new GsonBuilder().create();
				Type type = new TypeToken<Map<String, SensorRepresentation>>() {
				}.getType();
				HashMap<String, SensorRepresentation> sensors = gson.fromJson(message, type);
				updateMaxPosition(sensors);
				rebuildMap(sensors);
				utils.repaintMap();
			}

		};

		try {
			channel.basicConsume(this.queueName, true, consumer);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void updateMaxPosition(HashMap<String, SensorRepresentation> sensors) {
		for (SensorRepresentation sensor : sensors.values()) {

			int row = sensor.getPosition().getRow();
			int column = sensor.getPosition().getColumn();

			if (row > Storage.getInstance().getWorldRows()) {
				Storage.getInstance().setWorldRows(row);
			}

			if (column > Storage.getInstance().getWorldColumns()) {
				Storage.getInstance().setWorldColumns(column);
			}
		}
	}

	/**
	 * Build a graph from the sensors representation.
	 */
	private void rebuildMap(HashMap<String, SensorRepresentation> sensors) {

		Storage storage = Storage.getInstance();
		Graph map = GraphFromMap.build(sensors, storage.getWorldRows(), storage.getWorldColumns());

		storage.setMap(map);

	}

}
