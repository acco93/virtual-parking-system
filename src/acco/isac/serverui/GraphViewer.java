package acco.isac.serverui;

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

	private static final int OFFSET = 5;

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

		for (Vertex node : map.getVertexes()) {

			int x = node.getInfo().getPosition().getRow();
			int y = node.getInfo().getPosition().getColumn();

			if (node.getInfo().getType() == InfoType.SENSOR) {
				// sensor
				SensorRepresentation sensor = (SensorRepresentation) node.getInfo();

				g.setColor(Color.BLUE);
				g.fillOval(this.cellWidth * x + OFFSET, this.cellHeight * y + OFFSET, this.cellWidth - 5*OFFSET,
						this.cellHeight - 5*OFFSET);

			} else {
				// street
				StreetRepresentation street = (StreetRepresentation) node.getInfo();
				g.setColor(Color.WHITE);
				g.fillOval(this.cellWidth * x + OFFSET, this.cellHeight * y + OFFSET, this.cellWidth - 5*OFFSET,
						this.cellHeight - 5*OFFSET);
			}

			g.setColor(Color.BLACK);
			g.drawOval(this.cellWidth * x + OFFSET, this.cellHeight * y + OFFSET, this.cellWidth - 5 * OFFSET,
					this.cellHeight - 5 * OFFSET);

		}

		
		/*for (Edge edge : map.getEdges()) {
			EnvironmentInfo source = edge.getSource().getInfo();
			EnvironmentInfo destination = edge.getDestination().getInfo();
			
			int x1 = source.getPosition().getX();
			int y1 = source.getPosition().getY();
			
			int x2 = destination.getPosition().getX();
			int y2 = destination.getPosition().getY();
			
			//System.out.println("("+x1+","+y1+") -> ("+x2+","+y2+")");
			
			g.drawLine(this.cellWidth*x1, this.cellHeight*y1, this.cellWidth*x2, this.cellHeight*y2);
			
		}*/

	}

	private void setUpDimensions() {
		// rows and columns are exchange between graphics and code
		this.gridWidth = serverStorage.getWorldRows();
		this.gridHeight = serverStorage.getWorldColumns();
		Dimension panelSize = this.getSize();

		this.cellWidth = (int) (panelSize.getWidth() / this.gridWidth);
		this.cellHeight = (int) (panelSize.getHeight() / this.gridHeight);
	}

}
