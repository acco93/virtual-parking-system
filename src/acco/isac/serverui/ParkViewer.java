package acco.isac.serverui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import acco.isac.server.Storage;
import acco.isac.server.inforepresentation.SensorRepresentation;
import acco.isac.sharedknowledge.R;

public class ParkViewer extends JPanel {

	private static final int OFFSET = 5;
	private static final long serialVersionUID = 1L;

	private int gridWidth;
	private int gridHeight;
	private int cellWidth;
	private int cellHeight;
	private Storage serverStorage;

	public ParkViewer() {

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
		
		setUpDimensions();
		
		

		for (SensorRepresentation sensor : serverStorage.getSensors().values()) {

			int row = sensor.getPosition().getRow();
			int column = sensor.getPosition().getColumn();

			
			
			if (sensor.isDead()) {
				g.setColor(Color.red);
				g.fillRect(this.cellWidth * column + OFFSET, this.cellHeight * row + OFFSET, this.cellWidth - OFFSET,
						this.cellHeight - OFFSET);
			} else {
				// is alive
				if (sensor.isFree()) {
					g.setColor(Color.green);
					g.fillRect(this.cellWidth * column + OFFSET,this.cellHeight * row + OFFSET,  this.cellWidth - OFFSET,
							this.cellHeight - OFFSET);

				} else {
					g.setColor(Color.orange);
					g.fillRect( this.cellWidth * column + OFFSET,this.cellHeight * row + OFFSET, this.cellWidth - OFFSET,
							this.cellHeight - OFFSET);

				}

				// R.MAX_SENSOR_DELAY : 255 = remainingTime : x
				long remainingTime = sensor.remainingTime();
				int colorValue = 255 - (int) (remainingTime * 255 / (R.MAX_SENSOR_DELAY));
				
				Color color = new Color(255-colorValue,colorValue,0);
				
				g.setColor(color);
				g.fillRect( this.cellWidth * column + OFFSET,this.cellHeight * row + OFFSET, this.cellWidth/3 - OFFSET,
						this.cellHeight/3 - OFFSET);
						
				
				
			}

			g.setColor(Color.BLACK);
			g.drawRect( this.cellWidth * column + OFFSET,this.cellHeight * row + OFFSET, this.cellWidth - OFFSET,
					this.cellHeight - OFFSET);

		}

	}
	

	private void setUpDimensions() {
		this.gridWidth = serverStorage.getWorldColumns() +1;
		this.gridHeight = serverStorage.getWorldRows() + 1;
		// +1 because the drawings starts from the upper left corner
		
		Dimension panelSize = this.getSize();
		
		this.cellWidth = (int) (panelSize.getWidth() / this.gridWidth);
		this.cellHeight = (int) (panelSize.getHeight() / this.gridHeight);
	}



}
