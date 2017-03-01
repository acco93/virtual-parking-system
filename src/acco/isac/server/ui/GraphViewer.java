package acco.isac.server.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import acco.isac.datastructures.Graph;
import acco.isac.datastructures.Vertex;
import acco.isac.environment.Position;
import acco.isac.server.Storage;
import acco.isac.server.inforepresentation.InfoType;
import acco.isac.server.inforepresentation.SensorRepresentation;
import acco.isac.server.inforepresentation.StreetRepresentation;
import acco.isac.sharedknowledge.R;

public class GraphViewer extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final int OFFSET = 20;

	private Storage serverStorage;
	private int gridWidth;
	private int gridHeight;
	private int cellWidth;
	private int cellHeight;

	public GraphViewer() {
		this.serverStorage = Storage.getInstance();

		new Thread(() -> {

			while (true) {

				SwingUtilities.invokeLater(() -> {
					repaint();
				});

				try {
					Thread.sleep(250);
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

		this.setUpDimensions();
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Graph map = this.serverStorage.getMap();

		// draw edges

		for (Vertex vertex : map.getNodes()) {

			int row = vertex.getInfo().getPosition().getRow();
			int column = vertex.getInfo().getPosition().getColumn();

			for (Vertex adj : vertex.getAdjecent()) {
				Position adjPosition = adj.getInfo().getPosition();
				int adjRow = adjPosition.getRow();
				int adjColumn = adjPosition.getColumn();

				// g.drawLine((this.cellHeight * column) + this.cellHeight / 3 +
				// OFFSET,
				// this.cellWidth * row + this.cellWidth / 3 + OFFSET,
				// this.cellHeight * adjColumn + this.cellHeight / 3 + OFFSET,
				// this.cellWidth * adjRow + this.cellWidth / 3 + OFFSET);

				int x1 = this.cellWidth * column + OFFSET + this.cellWidth / 3;
				int y1 = this.cellHeight * row + OFFSET + this.cellHeight / 3;

				// System.out.println(x1+" "+y1);

				if (adjColumn > column) {
					g.drawLine(x1, y1, x1 + this.cellWidth, y1);
				} else if (adjColumn < column) {
					g.drawLine(x1 - this.cellWidth, y1, x1, y1);
				}

				if (adjRow > row) {
					g.drawLine(x1, y1, x1, y1 + this.cellHeight);
				} else if (adjRow < row) {
					g.drawLine(x1, y1 - this.cellHeight, x1, y1);
				}

			}
		}

		for (Vertex vertex : map.getNodes()) {

			int row = vertex.getInfo().getPosition().getRow();
			int column = vertex.getInfo().getPosition().getColumn();

			if (vertex.getInfo().getType() == InfoType.SENSOR) {
				// sensor
				SensorRepresentation sensor = (SensorRepresentation) vertex.getInfo();

				// R.MAX_SENSOR_DELAY : 255 = remainingTime : x
				long remainingTime = sensor.remainingTime();
				int colorValue = 255 - (int) (remainingTime * 255 / (R.MAX_SENSOR_DELAY));
				if (colorValue < 0) {
					colorValue = 0;
				}
				Color color = new Color(255 - colorValue, colorValue, 0);

				g.setColor(color);
				g.fillOval(this.cellWidth * column + OFFSET, this.cellHeight * row + OFFSET, this.cellWidth - OFFSET,
						this.cellHeight - OFFSET);

			} else {
				// street
				StreetRepresentation street = (StreetRepresentation) vertex.getInfo();
				g.setColor(Color.WHITE);

				g.fillOval(this.cellWidth * column + OFFSET, this.cellHeight * row + OFFSET, this.cellWidth - OFFSET,
						this.cellHeight - OFFSET);

			}

			g.setColor(Color.BLACK);
			g.drawOval(this.cellWidth * column + OFFSET, this.cellHeight * row + OFFSET, this.cellWidth - OFFSET,
					this.cellHeight - OFFSET);

		}

	}

	private void setUpDimensions() {
		this.gridWidth = serverStorage.getWorldColumns() + 1;
		this.gridHeight = serverStorage.getWorldRows() + 1;
		// +1 because the drawings starts from the upper left corner

		Dimension panelSize = this.getSize();

		this.cellWidth = (int) (panelSize.getWidth() / this.gridWidth);
		this.cellHeight = (int) (panelSize.getHeight() / this.gridHeight);
	}

}
