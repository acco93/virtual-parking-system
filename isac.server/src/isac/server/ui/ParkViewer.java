package isac.server.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import isac.core.data.SensorRepresentation;
import isac.core.sharedknowledge.R;
import isac.server.Storage;

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

		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			    RenderingHints.VALUE_ANTIALIAS_ON);
		
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
					g.fillRect(this.cellWidth * column + OFFSET, this.cellHeight * row + OFFSET,
							this.cellWidth - OFFSET, this.cellHeight - OFFSET);

				} else {
					g.setColor(Color.orange);
					g.fillRect(this.cellWidth * column + OFFSET, this.cellHeight * row + OFFSET,
							this.cellWidth - OFFSET, this.cellHeight - OFFSET);

				}

			}
			
			g.setColor(Color.BLACK);
			g.drawRect(this.cellWidth * column + OFFSET, this.cellHeight * row + OFFSET, this.cellWidth - OFFSET,
					this.cellHeight - OFFSET);

			g.drawString("User"+new Random().nextInt(255), this.cellWidth * column + OFFSET + this.cellWidth / 4,
					this.cellHeight * row + OFFSET + this.cellHeight / 2);
			
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
