package acco.isac.server;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import acco.isac.algorithms.DijkstraAlgorithm;
import acco.isac.core.EventLoop;
import acco.isac.datastructures.Edge;
import acco.isac.datastructures.EnvironmentVertex;
import acco.isac.datastructures.Graph;
import acco.isac.datastructures.OldGraph;
import acco.isac.datastructures.OldVertex;
import acco.isac.datastructures.ShortestPathVertex;
import acco.isac.environment.Position;
import acco.isac.sensor.SensorMessage;
import acco.isac.server.inforepresentation.EnvironmentInfo;
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

	private void oldrebuildMap() {

		// +1 because if it is 0
		// ArrayOutOfBoundsException
		// it doesn't exist a 0-sized matrix
		int maxHeight = this.storage.getWorldRows();
		int maxWidth = this.storage.getWorldColumns();

		if (maxHeight == 0) {
			maxHeight = 1;
		}

		if (maxWidth == 0) {
			maxWidth = 1;
		}

		// intermediate mapping in a matrix of EnvironmentInfo
		EnvironmentInfo[][] matrix = new EnvironmentInfo[maxHeight][maxWidth];
		for (int i = 0; i < maxHeight; i++) {
			for (int j = 0; j < maxWidth; j++) {
				// at the beginning everything is street
				matrix[i][j] = new StreetRepresentation(new Position(i, j));
			}
		}

		for (EnvironmentInfo element : this.storage.getSensors().values()) {
			int x = element.getPosition().getRow();
			int y = element.getPosition().getColumn();
			matrix[y][x] = element;
		}

		// define a matrix of Vertex

		OldVertex[][] edgesMatrix = new OldVertex[maxHeight][maxWidth];
		List<OldVertex> nodes = new LinkedList<OldVertex>();

		for (int i = 0; i < maxHeight; i++) {
			for (int j = 0; j < maxWidth; j++) {

				edgesMatrix[i][j] = new OldVertex(i + "_" + j, "node", matrix[i][j]);
				nodes.add(edgesMatrix[i][j]);

			}
		}

		System.out.println(nodes.size());

		List<Edge> edges = new LinkedList<Edge>();

		// link adjacent street nodes

		for (int i = 0; i < maxHeight; i++) {
			for (int j = 0; j < maxWidth; j++) {

				OldVertex source, destination;

				// left to right
				if (j + 1 < maxWidth) {
					// if not at the grid-right border
					source = edgesMatrix[i][j];
					destination = edgesMatrix[i][j + 1];
					if (source.getInfo().getType() == InfoType.STREET
							|| destination.getInfo().getType() == InfoType.STREET) {

						int weight = STREET_WEIGHT;
						if (destination.getInfo().getType() == InfoType.SENSOR) {
							weight = SENSOR_WEIGHT;
						}

						Edge edgeSD = new Edge("link_(" + i + "," + j + ")_(" + i + "," + (j + 1) + ")", source,
								destination, weight);
						edges.add(edgeSD);

						Edge edgeDS = new Edge("link_(" + i + "," + (j + 1) + ")_(" + i + "," + j + ")", destination,
								source, weight);
						edges.add(edgeDS);

					}
				}

				// top to bottom
				if (i + 1 < maxHeight) {
					// if not at the grid-bottom border
					source = edgesMatrix[i][j];
					destination = edgesMatrix[i + 1][j];
					if (source.getInfo().getType() == InfoType.STREET
							|| destination.getInfo().getType() == InfoType.STREET) {

						int weight = STREET_WEIGHT;
						if (destination.getInfo().getType() == InfoType.SENSOR) {
							weight = SENSOR_WEIGHT;
						}

						Edge edgeSD = new Edge("link_(" + i + "," + j + ")_(" + (i + 1) + "," + j + ")", source,
								destination, weight);
						edges.add(edgeSD);

						Edge edgeDS = new Edge("link_(" + (i + 1) + "," + j + ")_(" + i + "," + j + ")", destination,
								source, weight);
						edges.add(edgeDS);
					}

				}

			}
		}

		storage.setMap(new OldGraph(nodes, edges));

		/*
		 * if (maxWidth > 3 && maxHeight > 3) { DijkstraAlgorithm d = new
		 * DijkstraAlgorithm(new Graph(nodes, edges));
		 * d.execute(edgesMatrix[1][0]); LinkedList<Vertex> p =
		 * d.getPath(edgesMatrix[0][2]); for (Vertex vertex : p) {
		 * System.out.println(vertex); } }
		 */
	}

	private void ololrebuildMap() {

		int rows = this.storage.getWorldRows() + 1;
		int columns = this.storage.getWorldColumns() + 1;
		// +1 because the count starts from 0

		// define a matrix of vertices
		OldVertex[][] matrix = new OldVertex[rows][columns];

		// first:
		// set street everywhere
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < columns; c++) {
				StreetRepresentation street = new StreetRepresentation(new Position(r, c));
				matrix[r][c] = new OldVertex("v_" + r + "_" + c, "streetnode", street);
			}
		}

		// then:
		// set the sensors in the correct position
		for (SensorRepresentation sensor : this.storage.getSensors().values()) {
			int row = sensor.getPosition().getRow();
			int column = sensor.getPosition().getColumn();
			matrix[row][column] = new OldVertex("v_" + row + "_" + column, "streetnode", sensor);
		}

		List<OldVertex> nodes = new LinkedList<>();
		List<Edge> edges = new LinkedList<>();

		// build the graph from the matrix
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < columns; c++) {
				// add all nodes to the node list
				nodes.add(matrix[r][c]);

				// link the nodes
				OldVertex source, destination;

				// left to right
				if (c + 1 < columns) {
					// if not at the grid-right border
					source = matrix[r][c];
					destination = matrix[r][c + 1];
					if (source.getInfo().getType() == InfoType.STREET
							|| destination.getInfo().getType() == InfoType.STREET) {

						int weight = STREET_WEIGHT;
						if (destination.getInfo().getType() == InfoType.SENSOR) {
							weight = SENSOR_WEIGHT;
						}

						Edge edgeSD = new Edge("link_(" + r + "," + c + ")_(" + r + "," + (c + 1) + ")", source,
								destination, weight);
						edges.add(edgeSD);

						Edge edgeDS = new Edge("link_(" + r + "," + (c + 1) + ")_(" + r + "," + c + ")", destination,
								source, weight);
						edges.add(edgeDS);

					}
				}

				// top to bottom
				if (r + 1 < rows) {
					// if not at the grid-bottom border
					source = matrix[r][c];
					destination = matrix[r + 1][c];
					if (source.getInfo().getType() == InfoType.STREET
							|| destination.getInfo().getType() == InfoType.STREET) {

						int weight = STREET_WEIGHT;
						if (destination.getInfo().getType() == InfoType.SENSOR) {
							weight = SENSOR_WEIGHT;
						}

						Edge edgeSD = new Edge("link_(" + r + "," + c + ")_(" + (r + 1) + "," + c + ")", source,
								destination, weight);
						edges.add(edgeSD);

						Edge edgeDS = new Edge("link_(" + (r + 1) + "," + c + ")_(" + r + "," + c + ")", destination,
								source, weight);
						edges.add(edgeDS);
					}

				}
			}
		}

		OldGraph map = new OldGraph(nodes, edges);
		this.storage.setMap(map);

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

		if (rows > 3 && columns > 3) {
			DijkstraAlgorithm da = new DijkstraAlgorithm(map, matrix[0][0]);
			System.out.println(da.getPath(matrix[0][3]));
		}
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

		System.out.println(destinationRow + " " + destinationColumn);

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
