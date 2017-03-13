package isac.core.algorithms;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import isac.core.data.InfoType;
import isac.core.data.Position;
import isac.core.data.SensorRepresentation;
import isac.core.data.StreetRepresentation;
import isac.core.datastructures.Graph;
import isac.core.datastructures.Vertex;

/**
 * Build a graph from a map of sensors representation.
 */
public class GraphFromMap {

	private static final int SENSOR_WEIGHT = 100;
	private static final int STREET_WEIGHT = 1;

	public static Graph build(Map<String, SensorRepresentation> sensors, int worldRows, int worldColumns) {

		int rows = worldRows + 1;
		int columns = worldColumns + 1;
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
		for (SensorRepresentation sensor : sensors.values()) {
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

		return new Graph(nodes);

	}

	private static void connectBottom(Vertex[][] matrix, int matrixRows, int matrixColumns, int sourceRow,
			int sourceColumn) {
		connect(matrix, matrixRows, matrixColumns, sourceRow, sourceColumn, sourceRow - 1, sourceColumn);
	}

	private static void connectUp(Vertex[][] matrix, int matrixRows, int matrixColumns, int sourceRow,
			int sourceColumn) {
		connect(matrix, matrixRows, matrixColumns, sourceRow, sourceColumn, sourceRow + 1, sourceColumn);
	}

	private static void connectRight(Vertex[][] matrix, int matrixRows, int matrixColumns, int sourceRow,
			int sourceColumn) {
		connect(matrix, matrixRows, matrixColumns, sourceRow, sourceColumn, sourceRow, sourceColumn + 1);
	}

	private static void connectLeft(Vertex[][] matrix, int matrixRows, int matrixColumns, int sourceRow,
			int sourceColumn) {
		connect(matrix, matrixRows, matrixColumns, sourceRow, sourceColumn, sourceRow, sourceColumn - 1);
	}

	private static void connect(Vertex[][] matrix, int matrixRows, int matrixColumns, int sourceRow, int sourceColumn,
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
