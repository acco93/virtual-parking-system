package acco.isac.clientui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import acco.isac.algorithms.DijkstraAlgorithm;
import acco.isac.datastructures.Graph;
import acco.isac.datastructures.Vertex;
import acco.isac.server.Storage;
import acco.isac.server.inforepresentation.InfoType;
import acco.isac.server.inforepresentation.SensorRepresentation;
import acco.isac.server.inforepresentation.StreetRepresentation;

public class MapViewer extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final int OFFSET = 0;

	private Storage serverStorage;
	private int gridWidth;
	private int gridHeight;
	private int cellWidth;
	private int cellHeight;

	private int userRow;

	private int userColumn;

	public MapViewer() {

		this.userRow = 0;
		this.userColumn = 0;
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
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		this.setUpDimensions();

		Graph map = this.serverStorage.getMap();

		// draw edges

		for (Vertex vertex : map.getNodes()) {

			int row = vertex.getInfo().getPosition().getRow();
			int column = vertex.getInfo().getPosition().getColumn();

			if (vertex.getInfo().getType() == InfoType.SENSOR) {
				// sensor
				SensorRepresentation sensor = (SensorRepresentation) vertex.getInfo();

				if (sensor.isDead()) {
					g.setColor(Color.red);
					g.fillRect(this.cellWidth * column + OFFSET, this.cellHeight * row + OFFSET,
							this.cellWidth - OFFSET, this.cellHeight - OFFSET);
				} else {
					// is alive
					if (sensor.isFree()) {
						g.setColor(Color.green);
						g.fillRect(this.cellWidth * column + OFFSET, this.cellHeight * row + OFFSET,
								this.cellWidth - OFFSET, this.cellHeight - OFFSET);

					} else {
						g.setColor(Color.orange);
						g.fillRect(this.cellWidth * column + OFFSET, this.cellHeight * row + OFFSET,
								this.cellWidth - OFFSET, this.cellHeight - OFFSET);

					}

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

		if (serverStorage.getWorldRows() > 6 && serverStorage.getWorldColumns() > 10) {
			DijkstraAlgorithm da = new DijkstraAlgorithm(map,
					map.getVertexFromId("v_" + this.userRow + "_" + this.userColumn));
			List<Vertex> path = da.getPath(map.getVertexFromId("v_4_8"));

			for (int i = 0; i < path.size() - 1; i++) {
				Vertex node = path.get(i);
				int row = node.getInfo().getPosition().getRow();
				int column = node.getInfo().getPosition().getColumn();

				g.setColor(Color.yellow);
				g.fillOval(this.cellWidth * column + OFFSET + this.cellWidth / 3,
						this.cellHeight * row + OFFSET + this.cellHeight / 3, this.cellWidth / 4 - OFFSET,
						this.cellHeight / 4 - OFFSET);
			}

			int row = (path.get(path.size() - 1)).getInfo().getPosition().getRow();
			int column = (path.get(path.size() - 1)).getInfo().getPosition().getColumn();

			Stroke oldStroke = g2.getStroke();
			g2.setStroke(new BasicStroke(3));
			g.drawOval(this.cellWidth * column + OFFSET + this.cellWidth / 4,
					this.cellHeight * row + OFFSET + this.cellHeight / 4, this.cellWidth / 2 - OFFSET,
					this.cellHeight / 2 - OFFSET);
			g2.setStroke(oldStroke);
		}

		g.setColor(Color.WHITE);
		g.fillOval(this.cellWidth * this.userColumn + this.cellWidth / 4, this.cellHeight*this.userRow + this.cellHeight / 4, this.cellWidth - this.cellWidth / 2,
				this.cellHeight - this.cellHeight / 2);

	}

	private void setUpDimensions() {
		this.gridWidth = serverStorage.getWorldColumns() + 1;
		this.gridHeight = serverStorage.getWorldRows() + 1;
		// +1 because the drawings starts from the upper left corner

		Dimension panelSize = this.getSize();

		this.cellWidth = (int) (panelSize.getWidth() / this.gridWidth);
		this.cellHeight = (int) (panelSize.getHeight() / this.gridHeight);
	}

	public void left() {
		this.userColumn--;
		if(this.userColumn<0){this.userColumn=0;}
	}

	public void right() {
		this.userColumn++;
		if(this.userColumn>serverStorage.getWorldColumns()){this.userColumn=serverStorage.getWorldColumns();}
	}

	public void up() {
		this.userRow--;
		if(this.userRow<0){this.userRow=0;}
	}

	public void down() {
		this.userRow++;
		if(this.userRow>serverStorage.getWorldRows()){this.userRow=serverStorage.getWorldRows();}
	}

}
