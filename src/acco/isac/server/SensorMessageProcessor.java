package acco.isac.server;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import acco.isac.algorithms.DijkstraAlgorithm;
import acco.isac.core.EventLoop;
import acco.isac.datastructures.Edge;
import acco.isac.datastructures.Graph;
import acco.isac.datastructures.Vertex;
import acco.isac.datastructures.VertexType;
import acco.isac.environment.Position;
import acco.isac.sensor.SensorMessage;

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

		// ui.updateSensors(storage);

	}

	private void updateMaxPosition(Position position) {

		int x = position.getX();
		int y = position.getY();

		if (x > this.storage.getMaxWorldWidth()) {
			this.storage.setMaxWorldWidth(x + 1);
		}

		if (y > this.storage.getMaxWorldHeight()) {
			this.storage.setMaxWorldHeight(y + 1);
		}

	}

	private void rebuildMap() {

		// +1 because if it is 0
		// ArrayOutOfBoundsException
		// it doesn't exist a 0-sized matrix
		int maxHeight = this.storage.getMaxWorldHeight() + 1;
		int maxWidth = this.storage.getMaxWorldWidth() + 1;

		// intermediate mapping in a matrix
		boolean[][] matrix = new boolean[maxHeight][maxWidth];
		for (int i = 0; i < maxHeight; i++) {
			for (int j = 0; j < maxWidth; j++) {
				matrix[i][j] = new Boolean(false);
			}
		}

		for (SensorRepresentation sensor : this.storage.getSensors().values()) {
			int x = sensor.getPosition().getX();
			int y = sensor.getPosition().getY();
			matrix[y][x] = true;
		}

		// define a matrix of nodes

		Vertex[][] edgesMatrix = new Vertex[maxHeight][maxWidth];
		List<Vertex> sensorNodes = new LinkedList<Vertex>();
		List<Vertex> streetNodes = new LinkedList<Vertex>();
		List<Vertex> nodes = new LinkedList<Vertex>();

		for (int i = 0; i < maxHeight; i++) {
			for (int j = 0; j < maxWidth; j++) {

				if (matrix[i][j] == true) {
					edgesMatrix[i][j] = new Vertex(i + "_" + j, "sensor", VertexType.SENSOR);
					sensorNodes.add(edgesMatrix[i][j]);
				} else {
					edgesMatrix[i][j] = new Vertex(i + "_" + j, "street", VertexType.STREET);
					streetNodes.add(edgesMatrix[i][j]);
				}

			}
		}

		nodes.addAll(streetNodes);
		nodes.addAll(sensorNodes);

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
					if (source.getType() == VertexType.STREET || destination.getType() == VertexType.STREET) {

						int weight = STREET_WEIGHT;
						if (destination.getType() == VertexType.SENSOR) {
							weight = SENSOR_WEIGHT;
						}

						Edge edgeSD = new Edge("link_(" + i + "," + j + ")_(" + i + "," + (j + 1) + ")", source,
								destination, weight);
						edges.add(edgeSD);

						Edge edgeDS = new Edge("link_(" + i + "," + (j + 1) + ")_(" + i + "," + j + ")", destination, source, weight);
						edges.add(edgeDS);

					}
				}

				// top to bottom
				if (i + 1 < maxHeight) {
					// if not at the grid-bottom border
					source = edgesMatrix[i][j];
					destination = edgesMatrix[i + 1][j];
					if (source.getType() == VertexType.STREET || destination.getType() == VertexType.STREET) {

						int weight = STREET_WEIGHT;
						if (destination.getType() == VertexType.SENSOR) {
							weight = SENSOR_WEIGHT;
						}

						Edge edgeSD = new Edge("link_(" + i + "," + j + ")_(" + (i+1) + "," + j + ")", source, destination, weight);
						edges.add(edgeSD);

						Edge edgeDS = new Edge("link_(" + (i+1) + "," + j + ")_(" + i + "," + j + ")", destination, source, weight);
						edges.add(edgeDS);
					}

				}

			}
		}

		storage.setMap(new Graph(nodes, edges));

		/*if (maxWidth > 10 && maxHeight > 7) {
			DijkstraAlgorithm d = new DijkstraAlgorithm(new Graph(nodes, edges));
			d.execute(edgesMatrix[0][0]);
			LinkedList<Vertex> p = d.getPath(edgesMatrix[6][0]);
			for (Vertex vertex : p) {
				System.out.println(vertex);
			}
		}*/
	}

}
