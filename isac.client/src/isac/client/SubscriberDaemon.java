package isac.client;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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

import isac.core.data.InfoType;
import isac.core.data.Position;
import isac.core.data.SensorRepresentation;
import isac.core.data.StreetRepresentation;
import isac.core.datastructures.Graph;
import isac.core.datastructures.Vertex;
import isac.core.log.ILogger;
import isac.core.log.Logger;
import isac.core.sharedknowledge.R;

public class SubscriberDaemon {

	private static final int SENSOR_WEIGHT = 100;
	private static final int STREET_WEIGHT = 1;
	
	private Channel channel;
	private String queueName;
	private ILogger gLog;

	public SubscriberDaemon(ILogger gLog) {

		Logger.getInstance().info("started");
		this.gLog = gLog;
		this.setupMqtt();

	}

	private void setupMqtt() {

		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			Connection connection;
			connection = factory.newConnection();
			channel = connection.createChannel();

			channel.exchangeDeclare(R.EXCHANGE_NAME, "fanout");
			queueName = channel.queueDeclare().getQueue();
			channel.queueBind(queueName, R.EXCHANGE_NAME, "");
		} catch (IOException | TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void start() {

		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String message = new String(body, "UTF-8");

				gLog.info("Received an updated map.");
				
				Gson gson = new GsonBuilder().create();
				Type type = new TypeToken<Map<String, SensorRepresentation>>() {
				}.getType();
				HashMap<String, SensorRepresentation> sensors = gson.fromJson(message, type);
				updateMaxPosition(sensors);
				rebuildMap(sensors);

			}

		};

		try {
			channel.basicConsume(this.queueName, true, consumer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void updateMaxPosition(HashMap<String, SensorRepresentation> sensors) {
		for (SensorRepresentation sensor : sensors.values()) {

			int row = sensor.getPosition().getRow();
			int column = sensor.getPosition().getColumn();

			if (row > ClientStorage.getInstance().getWorldRows()) {
				ClientStorage.getInstance().setWorldRows(row);
			}

			if (column > ClientStorage.getInstance().getWorldColumns()) {
				ClientStorage.getInstance().setWorldColumns(column);
			}
		}
	}

	private void rebuildMap(HashMap<String, SensorRepresentation> sensors) {

		ClientStorage storage = ClientStorage.getInstance();

		int rows = storage.getWorldRows() + 1;
		int columns = storage.getWorldColumns() + 1;
		// +1 because the count starts from 0

		// define a matrix of vertices
		Vertex[][] matrix = new Vertex[rows][columns];

		// first:
		// set street everywhere
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < columns; c++) {
				StreetRepresentation street = new StreetRepresentation(new Position(r, c));
				matrix[r][c] = new Vertex("v_" + r + "_" + c, street);
			}
		}

		// then:
		// set the sensors in the correct position
		for (SensorRepresentation sensor : sensors.values()) {
			int row = sensor.getPosition().getRow();
			int column = sensor.getPosition().getColumn();
			matrix[row][column] = new Vertex("v_" + row + "_" + column, sensor);
		}

		List<Vertex> nodes = new LinkedList<>();

		// build the graph from the matrix
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < columns; c++) {
				nodes.add(matrix[r][c]);

				// street nodes are connected everywhere (within 1-grid block)
				if (matrix[r][c].getInfo().getType() == InfoType.STREET) {
					connectLeft(matrix, rows, columns, r, c);
					connectRight(matrix, rows, columns, r, c);
					connectUp(matrix, rows, columns, r, c);
					connectBottom(matrix, rows, columns, r, c);
				} else {
					// sensor nodes are connected just to street nodes
				}

			}
		}

		Graph map = new Graph(nodes);

		storage.setMap(map);

	}

	private void connectBottom(Vertex[][] matrix, int matrixRows, int matrixColumns, int sourceRow, int sourceColumn) {

		this.connect(matrix, matrixRows, matrixColumns, sourceRow, sourceColumn, sourceRow - 1, sourceColumn);

	}

	private void connectUp(Vertex[][] matrix, int matrixRows, int matrixColumns, int sourceRow, int sourceColumn) {

		this.connect(matrix, matrixRows, matrixColumns, sourceRow, sourceColumn, sourceRow + 1, sourceColumn);

	}

	private void connectRight(Vertex[][] matrix, int matrixRows, int matrixColumns, int sourceRow, int sourceColumn) {
		this.connect(matrix, matrixRows, matrixColumns, sourceRow, sourceColumn, sourceRow, sourceColumn + 1);

	}

	private void connectLeft(Vertex[][] matrix, int matrixRows, int matrixColumns, int sourceRow, int sourceColumn) {
		this.connect(matrix, matrixRows, matrixColumns, sourceRow, sourceColumn, sourceRow, sourceColumn - 1);

	}

	private void connect(Vertex[][] matrix, int matrixRows, int matrixColumns, int sourceRow, int sourceColumn,
			int destinationRow, int destinationColumn) {

		if (destinationRow < 0 || destinationRow >= matrixRows || destinationColumn < 0
				|| destinationColumn >= matrixColumns) {
			return;
		}

		int weight = STREET_WEIGHT;
		if (matrix[destinationRow][destinationColumn].getInfo().getType() == InfoType.SENSOR) {
			weight = SENSOR_WEIGHT;
		}

		matrix[sourceRow][sourceColumn].addAdjacent(matrix[destinationRow][destinationColumn], weight);

	}

}
