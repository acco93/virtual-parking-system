package isac.server;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import isac.core.constructs.EventLoop;
import isac.core.data.InfoType;
import isac.core.data.Position;
import isac.core.data.SensorRepresentation;
import isac.core.data.StreetRepresentation;
import isac.core.datastructures.Graph;
import isac.core.datastructures.Vertex;
import isac.core.message.SensorMessage;

/**
 * 
 * It translate sensor messages into useful aggregate information.
 * 
 * @author acco
 *
 */
public class SensorMessageProcessor extends EventLoop<SensorMessage> {

	private static final int SENSOR_WEIGHT = 100;
	private static final int STREET_WEIGHT = 1;
	private Storage storage;
	private PublisherDaemon publisherDaemon;

	public SensorMessageProcessor(PublisherDaemon publisherDaemon) {
		this.storage = Storage.getInstance();
		this.publisherDaemon = publisherDaemon;
	}

	/**
	 * Add the message to the processing queue. It will be process as soon as
	 * possible following a FIFO sematics.
	 * 
	 * @param msg
	 *            the sensor message
	 */
	public void appendSensorMessage(SensorMessage msg) {
		super.append(msg);
	}

	@Override
	protected void process(SensorMessage msg) {

		/*
		 * Retrieve the sensors stored in the server storage
		 */
		ConcurrentHashMap<String, SensorRepresentation> sensors = storage.getSensors();

		/*
		 * Check if the sensor associated with this message was already present
		 */
		SensorRepresentation rep = sensors.get(msg.getSensorId());

		boolean update = false;

		if (rep == null) {
			/*
			 * Unknown sensor
			 */
			rep = new SensorRepresentation(msg.getSensorId(), msg.getPosition(), msg.isFree());
			sensors.put(msg.getSensorId(), rep);
			updateMaxPosition(msg.getPosition());
			rebuildMap();
			update = true;
		} else {
			/*
			 * Known sensor
			 */
			update = rep.updateState(msg.isFree());
		}

		/*
		 * Notify clients if needed
		 */
		if (update) {
			publisherDaemon.notifyClients();
		}

	}

	/**
	 * Update the known world dimensions.
	 * 
	 * @param position
	 *            a sensor position
	 */
	private void updateMaxPosition(Position position) {

		int row = position.getRow();
		int column = position.getColumn();

		if (column > this.storage.getWorldColumns()) {
			this.storage.setWorldColumns(column);
		}

		if (row > this.storage.getWorldRows()) {
			this.storage.setWorldRows(row);
		}

	}

	/**
	 * Build a graph from the sensors representation.
	 */
	private void rebuildMap() {
		int rows = this.storage.getWorldRows() + 1;
		int columns = this.storage.getWorldColumns() + 1;
		/*
		 * +1 because the count starts from 0
		 */

		/*
		 * Define a matrix of vertices
		 */
		Vertex[][] matrix = new Vertex[rows][columns];

		/*
		 * Set street everywhere
		 */
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < columns; c++) {
				StreetRepresentation street = new StreetRepresentation(new Position(r, c));
				matrix[r][c] = new Vertex("v_" + r + "_" + c, street);
			}
		}

		/*
		 * Set the sensors in the correct position
		 */
		for (SensorRepresentation sensor : this.storage.getSensors().values()) {
			int row = sensor.getPosition().getRow();
			int column = sensor.getPosition().getColumn();
			matrix[row][column] = new Vertex("v_" + row + "_" + column, sensor);
		}

		List<Vertex> nodes = new LinkedList<>();

		/*
		 * Build the graph from the matrix
		 */
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < columns; c++) {
				nodes.add(matrix[r][c]);

				/*
				 * Street nodes are connected everywhere (within 1-grid block)
				 */
				if (matrix[r][c].getInfo().getType() == InfoType.STREET) {
					connectLeft(matrix, rows, columns, r, c);
					connectRight(matrix, rows, columns, r, c);
					connectUp(matrix, rows, columns, r, c);
					connectBottom(matrix, rows, columns, r, c);
				} else {
					/*
					 * Sensor nodes have an out-degree = 0
					 */
				}

			}
		}

		Graph map = new Graph(nodes);

		this.storage.setMap(map);

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
