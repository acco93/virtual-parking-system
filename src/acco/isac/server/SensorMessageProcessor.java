package acco.isac.server;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import acco.isac.algorithms.DijkstraAlgorithm;
import acco.isac.core.EventLoop;
import acco.isac.datastructures.Edge;
import acco.isac.datastructures.Graph;
import acco.isac.datastructures.Vertex;
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

		Vertex[][] edgesMatrix = new Vertex[maxHeight][maxWidth];
		List<Vertex> nodes = new LinkedList<Vertex>();

		for (int i = 0; i < maxHeight; i++) {
			for (int j = 0; j < maxWidth; j++) {

				edgesMatrix[i][j] = new Vertex(i + "_" + j, "node", matrix[i][j]);
				nodes.add(edgesMatrix[i][j]);

			}
		}

		System.out.println(nodes.size());

		List<Edge> edges = new LinkedList<Edge>();

		// link adjacent street nodes

		for (int i = 0; i < maxHeight; i++) {
			for (int j = 0; j < maxWidth; j++) {

				Vertex source, destination;

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

		storage.setMap(new Graph(nodes, edges));

		/*
		 * if (maxWidth > 3 && maxHeight > 3) { DijkstraAlgorithm d = new
		 * DijkstraAlgorithm(new Graph(nodes, edges));
		 * d.execute(edgesMatrix[1][0]); LinkedList<Vertex> p =
		 * d.getPath(edgesMatrix[0][2]); for (Vertex vertex : p) {
		 * System.out.println(vertex); } }
		 */
	}

	private void rebuildMap() {

		int rows = this.storage.getWorldRows() + 1;
		int columns = this.storage.getWorldColumns() + 1;
		// +1 because the count starts from 0

		// define a matrix of street nodes
		EnvironmentInfo[][] matrix = new EnvironmentInfo[rows][columns];

	}

}
