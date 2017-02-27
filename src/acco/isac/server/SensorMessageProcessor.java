package acco.isac.server;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import acco.isac.algorithms.DijkstraAlgorithm;
import acco.isac.core.EventLoop;
import acco.isac.datastructures.EnvironmentVertex;
import acco.isac.datastructures.Graph;
import acco.isac.datastructures.ShortestPathVertex;
import acco.isac.environment.Position;
import acco.isac.sensor.SensorMessage;
import acco.isac.server.inforepresentation.InfoType;
import acco.isac.server.inforepresentation.SensorRepresentation;
import acco.isac.server.inforepresentation.StreetRepresentation;

public class SensorMessageProcessor extends EventLoop<SensorMessage> {

	private static final int SENSOR_WEIGHT = 100;
	private static final int STREET_WEIGHT = 1;
	private Storage storage;

	public SensorMessageProcessor() {
		this.storage = Storage.getInstance();
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

		// retrieve the sensors stored in the server storage
		ConcurrentHashMap<String, SensorRepresentation> sensors = storage.getSensors();

		// check if the sensor associated with this message was already present
		SensorRepresentation rep = sensors.get(msg.getSensorId());

		if (rep == null) {
			// it is an unknown sonar
			rep = new SensorRepresentation(msg.getSensorId(), msg.getPosition(), msg.isFree());
			sensors.put(msg.getSensorId(), rep);
			updateMaxPosition(msg.getPosition());
			rebuildMap();
		} else {
			rep.updateState(msg.isFree());
		}

	}

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

	private void rebuildMap() {
		int rows = this.storage.getWorldRows() + 1;
		int columns = this.storage.getWorldColumns() + 1;
		// +1 because the count starts from 0

		// define a matrix of vertices
		EnvironmentVertex[][] matrix = new EnvironmentVertex[rows][columns];

		// first:
		// set street everywhere
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < columns; c++) {
				StreetRepresentation street = new StreetRepresentation(new Position(r, c));
				matrix[r][c] = new EnvironmentVertex("v_" + r + "_" + c, street);
			}
		}

		// then:
		// set the sensors in the correct position
		for (SensorRepresentation sensor : this.storage.getSensors().values()) {
			int row = sensor.getPosition().getRow();
			int column = sensor.getPosition().getColumn();
			matrix[row][column] = new EnvironmentVertex("v_" + row + "_" + column, sensor);
		}

		List<ShortestPathVertex> nodes = new LinkedList<>();

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

		
		
		Graph<ShortestPathVertex> map = new Graph<>(nodes);

		this.storage.setMap(map);
		
	}

	private void connectBottom(EnvironmentVertex[][] matrix, int matrixRows, int matrixColumns, int sourceRow,
			int sourceColumn) {

		this.connect(matrix, matrixRows, matrixColumns, sourceRow, sourceColumn, sourceRow - 1, sourceColumn);

	}

	private void connectUp(EnvironmentVertex[][] matrix, int matrixRows, int matrixColumns, int sourceRow,
			int sourceColumn) {

		this.connect(matrix, matrixRows, matrixColumns, sourceRow, sourceColumn, sourceRow + 1, sourceColumn);

	}

	private void connectRight(EnvironmentVertex[][] matrix, int matrixRows, int matrixColumns, int sourceRow,
			int sourceColumn) {
		this.connect(matrix, matrixRows, matrixColumns, sourceRow, sourceColumn, sourceRow, sourceColumn + 1);

	}

	private void connectLeft(EnvironmentVertex[][] matrix, int matrixRows, int matrixColumns, int sourceRow,
			int sourceColumn) {
		this.connect(matrix, matrixRows, matrixColumns, sourceRow, sourceColumn, sourceRow, sourceColumn - 1);

	}

	private void connect(EnvironmentVertex[][] matrix, int matrixRows, int matrixColumns, int sourceRow,
			int sourceColumn, int destinationRow, int destinationColumn) {

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
