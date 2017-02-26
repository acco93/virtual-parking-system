package acco.isac.server.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import acco.isac.datastructures.Edge;
import acco.isac.datastructures.Graph;
import acco.isac.datastructures.Vertex;
import acco.isac.server.Storage;
import acco.isac.server.inforepresentation.EnvironmentInfo;
import acco.isac.server.inforepresentation.InfoType;
import acco.isac.server.inforepresentation.SensorRepresentation;
import acco.isac.server.inforepresentation.StreetRepresentation;

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
					Thread.sleep(100);
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

		Graph map = this.serverStorage.getMap();

		for (Edge edge : map.getEdges()) {
			EnvironmentInfo source = edge.getSource().getInfo();
			EnvironmentInfo destination = edge.getDestination().getInfo();

			int row1 = source.getPosition().getRow();
			int column1 = source.getPosition().getColumn();

			int row2 = destination.getPosition().getRow();
			int column2 = destination.getPosition().getColumn();

			g.drawLine((this.cellHeight * column1) + this.cellHeight / 3  + OFFSET, this.cellWidth * row1 + this.cellWidth / 3  + OFFSET,
					this.cellHeight * column2 + this.cellHeight / 3  + OFFSET, this.cellWidth * row2 + this.cellWidth / 3  + OFFSET);

		}
		
		for (Vertex node : map.getVertexes()) {

			int row = node.getInfo().getPosition().getRow();
			int column = node.getInfo().getPosition().getColumn();

			if (node.getInfo().getType() == InfoType.SENSOR) {
				// sensor
				SensorRepresentation sensor = (SensorRepresentation) node.getInfo();

				g.setColor(Color.BLUE);

				g.fillOval(this.cellWidth * column + OFFSET, this.cellHeight * row + OFFSET,
						this.cellWidth - OFFSET, this.cellHeight - OFFSET);

			} else {
				// street
				StreetRepresentation street = (StreetRepresentation) node.getInfo();
				g.setColor(Color.WHITE);
				
				g.fillOval(this.cellWidth * column + OFFSET, this.cellHeight * row + OFFSET,
						this.cellWidth -  OFFSET, this.cellHeight - OFFSET);

				 
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
