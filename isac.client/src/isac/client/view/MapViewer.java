package isac.client.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import isac.client.model.Storage;
import isac.core.data.InfoType;
import isac.core.data.SensorRepresentation;
import isac.core.data.StreetRepresentation;
import isac.core.datastructures.Graph;
import isac.core.datastructures.Vertex;

public class MapViewer extends JPanel {
	public static final Color BACKGROUND_COLOR = new Color(161, 212, 144);
	private static final long serialVersionUID = 1L;

	private static final int OFFSET = 0;

	private Storage storage;
	private int cellWidth;
	private int cellHeight;
	private List<Vertex> nearestParkPath;
	private List<Vertex> shortestPathToCar;

	public MapViewer() {

		this.storage = Storage.getInstance();
		this.nearestParkPath = new LinkedList<>();
		this.shortestPathToCar = new LinkedList<>();
		this.setBackground(BACKGROUND_COLOR);

		new Thread(() -> {

			while (true) {

				SwingUtilities.invokeLater(() -> {
					repaint();
				});

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		this.setUpDimensions();

		Graph map = this.storage.getMap();

		// draw edges

		for (Vertex vertex : map.getNodes()) {

			int row = vertex.getInfo().getPosition().getRow();
			int column = vertex.getInfo().getPosition().getColumn();

			if (vertex.getInfo().getType() == InfoType.SENSOR) {
				// sensor
				SensorRepresentation sensor = (SensorRepresentation) vertex.getInfo();

				if (sensor.isFree()) {
					g.setColor(Color.green);
					g.fillRect(this.cellWidth * column + OFFSET, this.cellHeight * row + OFFSET,
							this.cellWidth - OFFSET, this.cellHeight - OFFSET);

				} else {
					g.setColor(Color.orange);
					g.fillRect(this.cellWidth * column + OFFSET, this.cellHeight * row + OFFSET,
							this.cellWidth - OFFSET, this.cellHeight - OFFSET);

				}

				g.setColor(Color.BLACK);
				g.setFont(new Font("default", Font.BOLD, 14));
				g.drawString(sensor.getName(), this.cellWidth * column + OFFSET + this.cellWidth / 3,
						this.cellHeight * row + OFFSET + this.cellHeight / 2);

			} else {
				// street
				StreetRepresentation street = (StreetRepresentation) vertex.getInfo();

				g.setColor(Color.LIGHT_GRAY);
				g.fillRect(this.cellWidth * column + OFFSET, this.cellHeight * row + OFFSET, this.cellWidth - OFFSET,
						this.cellHeight - OFFSET);
			}

			g.setColor(Color.BLACK);
			g.drawRect(this.cellWidth * column + OFFSET, this.cellHeight * row + OFFSET, this.cellWidth - OFFSET,
					this.cellHeight - OFFSET);

		}

		/*
		 * if (storage.getWorldRows() > 6 && storage.getWorldColumns() > 10) {
		 * DijkstraAlgorithm da = new DijkstraAlgorithm(map,
		 * map.getVertexFromId("v_" + this.userRow + "_" + this.userColumn));
		 * List<Vertex> path = da.getPath(map.getVertexFromId("v_4_8"));
		 * 
		 * for (int i = 0; i < path.size() - 1; i++) { Vertex node =
		 * path.get(i); int row = node.getInfo().getPosition().getRow(); int
		 * column = node.getInfo().getPosition().getColumn();
		 * 
		 * g.setColor(Color.yellow); g.fillOval(this.cellWidth * column + OFFSET
		 * + this.cellWidth / 3, this.cellHeight * row + OFFSET +
		 * this.cellHeight / 3, this.cellWidth / 4 - OFFSET, this.cellHeight / 4
		 * - OFFSET); }
		 * 
		 * int row = (path.get(path.size() -
		 * 1)).getInfo().getPosition().getRow(); int column =
		 * (path.get(path.size() - 1)).getInfo().getPosition().getColumn();
		 * 
		 * Stroke oldStroke = g2.getStroke(); g2.setStroke(new BasicStroke(3));
		 * g.drawOval(this.cellWidth * column + OFFSET + this.cellWidth / 4,
		 * this.cellHeight * row + OFFSET + this.cellHeight / 4, this.cellWidth
		 * / 2 - OFFSET, this.cellHeight / 2 - OFFSET); g2.setStroke(oldStroke);
		 * }
		 */

		g.setColor(Color.yellow);
		for (int i = 0; i < this.nearestParkPath.size(); i++) {
			Vertex node = nearestParkPath.get(i);
			int row = node.getInfo().getPosition().getRow();
			int column = node.getInfo().getPosition().getColumn();

			g.fillOval(this.cellWidth * column + OFFSET + this.cellWidth / 3,
					this.cellHeight * row + OFFSET + this.cellHeight / 3, this.cellWidth / 4 - OFFSET,
					this.cellHeight / 4 - OFFSET);
		}

		g.setColor(Color.magenta);
		for (int i = 0; i < this.shortestPathToCar.size(); i++) {
			Vertex node = shortestPathToCar.get(i);
			int row = node.getInfo().getPosition().getRow();
			int column = node.getInfo().getPosition().getColumn();

			g.fillOval(this.cellWidth * column + OFFSET + this.cellWidth / 3,
					this.cellHeight * row + OFFSET + this.cellHeight / 3, this.cellWidth / 4 - OFFSET,
					this.cellHeight / 4 - OFFSET);
		}

		int userRow = this.storage.getUserPosition().getRow();
		int userColumn = this.storage.getUserPosition().getColumn();
		g.setColor(Color.WHITE);
		g.fillOval(this.cellWidth * userColumn + this.cellWidth / 4, this.cellHeight * userRow + this.cellHeight / 4,
				this.cellWidth - this.cellWidth / 2, this.cellHeight - this.cellHeight / 2);

	}

	private void setUpDimensions() {
		int gridWidth = storage.getWorldColumns() + 1;
		int gridHeight = storage.getWorldRows() + 1;
		// +1 because the drawings starts from the upper left corner

		Dimension panelSize = this.getSize();

		this.cellWidth = (int) (panelSize.getWidth() / gridWidth);
		this.cellHeight = (int) (panelSize.getHeight() / gridHeight);
	}

	public void setNearestParkPath(List<Vertex> path) {
		this.nearestParkPath = path;

	}

	public void setShortestPathToCar(List<Vertex> path) {
		this.shortestPathToCar = path;

	}

}
