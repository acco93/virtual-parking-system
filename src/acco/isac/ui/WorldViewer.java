package acco.isac.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import acco.isac.sensor.SensorMessage;
import acco.isac.server.SensorRepresentation;
import acco.isac.sharedknowledge.R;

public class WorldViewer extends JPanel implements ComponentListener {

	private static final int OFFSET = 5;
	private static final long serialVersionUID = 1L;

	private int gridWidth;
	private int gridHeight;
	private int cellWidth;
	private int cellHeight;
	private ConcurrentHashMap<String, SensorRepresentation> storage;

	public WorldViewer() {

		this.gridWidth = R.ENV_WIDTH;
		this.gridHeight = R.ENV_HEIGHT;

		// just to avoid the null-check each time in paintComponent
		this.storage = new ConcurrentHashMap<>();

		this.addComponentListener(this);

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

		for (SensorRepresentation sensor : storage.values()) {

			int x = sensor.getPosition().getX();
			int y = sensor.getPosition().getY();

			if (sensor.isDead()) {
				g.setColor(Color.red);
				g.fillRect(this.cellWidth * x + OFFSET, this.cellHeight * y + OFFSET, this.cellWidth - OFFSET,
						this.cellHeight - OFFSET);
			} else {
				// is alive
				if (sensor.isFree()) {
					g.setColor(Color.green);
					g.fillRect(this.cellWidth * x + OFFSET, this.cellHeight * y + OFFSET, this.cellWidth - OFFSET,
							this.cellHeight - OFFSET);

				} else {
					g.setColor(Color.orange);
					g.fillRect(this.cellWidth * x + OFFSET, this.cellHeight * y + OFFSET, this.cellWidth - OFFSET,
							this.cellHeight - OFFSET);

				}

				// R.MAX_SENSOR_DELAY : 255 = remainingTime : x
				long remainingTime = sensor.remainingTime();
				int colorValue = 255 - (int) (remainingTime * 255 / (R.MAX_SENSOR_DELAY));
				
				Color color = new Color(255-colorValue,colorValue,0);
				
				g.setColor(color);
				g.fillRect(this.cellWidth * x + OFFSET, this.cellHeight * y + OFFSET, this.cellWidth/3 - OFFSET,
						this.cellHeight/3 - OFFSET);
						
				
				
			}

			g.setColor(Color.BLACK);
			g.drawRect(this.cellWidth * x + OFFSET, this.cellHeight * y + OFFSET, this.cellWidth - OFFSET,
					this.cellHeight - OFFSET);

		}

	}

	@Override
	public void componentResized(ComponentEvent e) {
		
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

	public void updateSensors(ConcurrentHashMap<String, SensorRepresentation> storage) {
		this.storage = storage;
		this.repaint();
	}

}
