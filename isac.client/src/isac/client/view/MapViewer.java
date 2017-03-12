package isac.client.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import isac.client.model.Storage;
import isac.core.data.InfoType;
import isac.core.data.SensorRepresentation;
import isac.core.datastructures.Graph;
import isac.core.datastructures.Vertex;

/**
 * 
 * Graphical component that draw a the park map.
 * 
 * @author acco
 *
 */
public class MapViewer extends JPanel {

	private static final long serialVersionUID = 1L;
	private Storage storage;
	private int cellWidth;
	private int cellHeight;
	private List<Vertex> nearestParkPath;
	private List<Vertex> shortestPathToCar;

	private String queriedPosition;
	private String airDistance;
	private String airPath;

	public MapViewer() {

		this.storage = Storage.getInstance();
		/*
		 * Initialize lists to avoid if in paint.
		 */
		this.nearestParkPath = new LinkedList<>();
		this.shortestPathToCar = new LinkedList<>();

		this.queriedPosition = "";
		this.airDistance = "";
		this.airPath = "";
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (this.storage.isServerOn()) {
			drawMap(g);
		} else {
			displayInfo(g);
		}

	}

	private void displayInfo(Graphics g) {
		
		this.setBackground(Color.DARK_GRAY);
		
		g.setColor(Color.yellow);
		
		g.drawString(":: OFFLINE INFORMATION ::", this.getWidth()/2-75, 20);
		g.drawString("Current position: " + this.storage.getUserPosition(), 20, 40);

		this.storage.getCarPosition().ifPresent(p -> {
			g.drawString("Car parked: " + p, 250, 40);
		});

		g.drawLine(0, 60, this.getWidth(), 60);

		if (!this.queriedPosition.isEmpty()) {
			g.drawString("Response: " + this.queriedPosition, 20, 80);
			g.drawString("Air distance: " + this.airDistance, 20, 100);
			g.drawString("Air path: " + this.airPath, 20, 120);
		}

	}

	private void drawMap(Graphics g) {
		this.setUpDimensions();

		Graph map = this.storage.getMap();

		for (Vertex vertex : map.getNodes()) {

			int row = vertex.getInfo().getPosition().getRow();
			int column = vertex.getInfo().getPosition().getColumn();

			if (vertex.getInfo().getType() == InfoType.SENSOR) {
				// sensor
				SensorRepresentation sensor = (SensorRepresentation) vertex.getInfo();

				if (sensor.isFree()) {
					g.setColor(Color.green);
					g.fillRect(this.cellWidth * column, this.cellHeight * row, this.cellWidth, this.cellHeight);

				} else {
					g.setColor(Color.orange);
					g.fillRect(this.cellWidth * column, this.cellHeight * row, this.cellWidth, this.cellHeight);

				}

				/*
				 * Parking place name.
				 */
				g.setColor(Color.BLACK);
				g.setFont(new Font("default", Font.BOLD, 14));
				g.drawString(sensor.getName(), this.cellWidth * column + this.cellWidth / 3,
						this.cellHeight * row + this.cellHeight / 2);

			} else {
				// street
				g.setColor(Color.LIGHT_GRAY);
				g.fillRect(this.cellWidth * column, this.cellHeight * row, this.cellWidth, this.cellHeight);
			}

			g.setColor(Color.BLACK);
			g.drawRect(this.cellWidth * column, this.cellHeight * row, this.cellWidth, this.cellHeight);

		}

		/*
		 * Draw the nearest park path, if present.
		 */
		g.setColor(Color.yellow);
		for (int i = 0; i < this.nearestParkPath.size(); i++) {
			Vertex node = nearestParkPath.get(i);
			int row = node.getInfo().getPosition().getRow();
			int column = node.getInfo().getPosition().getColumn();

			g.fillOval(this.cellWidth * column + this.cellWidth / 3, this.cellHeight * row + this.cellHeight / 3,
					this.cellWidth / 4, this.cellHeight / 4);
		}

		/*
		 * Draw the shortest path to car, if present.
		 */
		g.setColor(Color.cyan);
		for (int i = 0; i < this.shortestPathToCar.size(); i++) {
			Vertex node = shortestPathToCar.get(i);
			int row = node.getInfo().getPosition().getRow();
			int column = node.getInfo().getPosition().getColumn();

			g.fillOval(this.cellWidth * column + this.cellWidth / 3, this.cellHeight * row + this.cellHeight / 3,
					this.cellWidth / 4, this.cellHeight / 4);
		}

		/*
		 * Draw the user.
		 */
		int userRow = this.storage.getUserPosition().getRow();
		int userColumn = this.storage.getUserPosition().getColumn();
		g.setColor(Color.WHITE);
		g.fillOval(this.cellWidth * userColumn + this.cellWidth / 4, this.cellHeight * userRow + this.cellHeight / 4,
				this.cellWidth - this.cellWidth / 2, this.cellHeight - this.cellHeight / 2);
	}

	/**
	 * Resize the grid to fit the panel.
	 */
	private void setUpDimensions() {
		int gridWidth = storage.getWorldColumns() + 1;
		int gridHeight = storage.getWorldRows() + 1;
		// +1 because the drawings starts from the upper left corner

		Dimension panelSize = this.getSize();

		this.cellWidth = (int) (panelSize.getWidth() / gridWidth);
		this.cellHeight = (int) (panelSize.getHeight() / gridHeight);
	}

	/**
	 * Set the nearest park path, that will eventually be drawn.
	 * 
	 * @param path
	 *            a list of vertex
	 */
	public void setNearestParkPath(List<Vertex> path) {
		this.nearestParkPath = path;

	}

	/**
	 * Set the shortest path to a parked car. It will eventually be drawn.
	 * 
	 * @param path
	 *            a list of vertex
	 */
	public void setShortestPathToCar(List<Vertex> path) {
		this.shortestPathToCar = path;

	}

	public void setQueriedPosition(String queriedPosition) {
		this.queriedPosition = queriedPosition;
	}

	public void setAirDistanceString(String airDistanceString) {
		this.airDistance = airDistanceString;
	}

	public void setAirPath(String airPath) {
		this.airPath = airPath;
	}

	public void resetDisplay() {
		this.setAirDistanceString("");
		this.setAirPath("");
		this.setQueriedPosition("");
	}

}
