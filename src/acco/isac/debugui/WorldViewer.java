package acco.isac.debugui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Optional;

import javax.swing.JPanel;

import acco.isac.environment.Cell;

public class WorldViewer extends JPanel implements ComponentListener {

	private static final int OFFSET = 5;
	private static final long serialVersionUID = 1L;
	private Cell[][] grid;
	private int gridWidth;
	private int gridHeight;
	private int cellWidth;
	private int cellHeight;

	public WorldViewer() {

		this.addComponentListener(this);

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (grid != null) {

			for (int i = 0; i < gridWidth; i++) {
				for (int j = 0; j < gridHeight; j++) {
					if(this.grid[i][j].isEmpty()){
						//g.setColor(Color.GRAY);
						//g.drawRect(this.cellWidth * i, this.cellHeight * j, this.cellWidth, this.cellHeight);						
					} else {
						g.setColor(Color.GREEN);
						g.fillRect(this.cellWidth * i +OFFSET, this.cellHeight * j +OFFSET, this.cellWidth-OFFSET, this.cellHeight-OFFSET);
						g.setColor(Color.BLACK);
						g.drawRect(this.cellWidth * i +OFFSET, this.cellHeight * j +OFFSET, this.cellWidth-OFFSET, this.cellHeight-OFFSET);
					}

				}
			}

		}

	}

	public void updateGrid(Cell[][] grid, int width, int height) {
		this.grid = grid;
		this.gridWidth = width;
		this.gridHeight = height;
	}

	@Override
	public void componentResized(ComponentEvent e) {
		System.out.println(this.getSize());

		Dimension panelSize = this.getSize();

		this.cellWidth = (int) (panelSize.getWidth() / this.gridWidth);
		this.cellHeight = (int) (panelSize.getHeight() / this.gridHeight);

		this.repaint();
		
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

}
