package acco.isac.serverui;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import acco.isac.datastructures.Edge;
import acco.isac.datastructures.Graph;
import acco.isac.datastructures.Vertex;
import acco.isac.server.Storage;

public class GraphViewer extends JPanel {

	private static final long serialVersionUID = 1L;
	private Storage serverStorage;
	private int gridWidth;
	private int gridHeight;
	private int cellWidth;
	private int cellHeight;

	public GraphViewer() {
		this.serverStorage = Storage.getInstance();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		this.setUpDimensions();
		
		Graph map = this.serverStorage.getMap();

		for(Vertex node:map.getVertexes()){
			
		}
		
		for (Edge edge :map.getEdges()){
			
		}
		
	}

	private void setUpDimensions() {
		this.gridWidth = serverStorage.getMaxWorldWidth();
		this.gridHeight = serverStorage.getMaxWorldHeight();
		Dimension panelSize = this.getSize();

		this.cellWidth = (int) (panelSize.getWidth() / this.gridWidth);
		this.cellHeight = (int) (panelSize.getHeight() / this.gridHeight);
	}

}
