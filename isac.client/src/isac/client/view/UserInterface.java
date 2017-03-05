package isac.client.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import isac.client.listeners.UserListener;
import isac.core.datastructures.Vertex;
import isac.core.log.ILogger;

/**
 * 
 * Client user interface.
 * 
 * @author acco
 *
 */
public class UserInterface extends JFrame {

	private static final long serialVersionUID = 1L;
	private MapViewer mapViewer;
	private JLabel statusLabel;
	private LogViewer logViewer;

	public UserInterface(UserListener userListener) {

		this.setTitle("Client-side - Virtual car parking");
		this.setSize(new Dimension(400, 300));

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		this.addKeyListener(userListener);

		/*
		 * To allow key detection.
		 */
		this.setFocusable(true);

		this.setLayout(new BorderLayout());

		JTabbedPane tabbedPane = new JTabbedPane();
		this.add(tabbedPane, BorderLayout.CENTER);

		this.logViewer = new LogViewer();
		tabbedPane.addTab("Log", logViewer);

		logViewer.info("App online");
		logViewer.error("Error");

		mapViewer = new MapViewer();
		tabbedPane.addTab("Map", mapViewer);

		// that's horrible ...
		JLabel helpLabel = new JLabel("<html> HELP: use <b>W</b>, <b>A</b>, <b>S</b>, <b>D</b> to move,"
				+ " <b>N</b> to find the nearest parking, <b>P</b> to park,"
				+ " <b>R</b> to remove a previously parked car and <b>L</b> to"
				+ " locate a previously parked car.</html>", SwingConstants.CENTER);

		helpLabel.setFont(helpLabel.getFont().deriveFont(Font.PLAIN));
		helpLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.add(helpLabel, BorderLayout.NORTH);

		statusLabel = new JLabel("Connected to server", SwingConstants.CENTER);

		this.add(statusLabel, BorderLayout.SOUTH);

		this.setVisible(true);

	}

	/**
	 * Change the user interface status label.
	 * 
	 * @param string
	 *            a message
	 */
	public void setStatusLabel(String string) {
		this.statusLabel.setText(string);
	}

	/**
	 * Returns an ILogger implementation.
	 * 
	 * @return graphical logger
	 */
	public ILogger getGraphicalLogger() {
		return this.logViewer;

	}

	/**
	 * Set the nearest park path, that will eventually be drawn.
	 * 
	 * @param path
	 *            a list of vertex
	 */
	public void setNearestParkPath(List<Vertex> path) {

		this.mapViewer.setNearestParkPath(path);

		this.repaintMap();

	}

	/**
	 * Ask to repaint the map.
	 */
	public void repaintMap() {
		SwingUtilities.invokeLater(() -> {
			this.mapViewer.repaint();
		});
	}

	/**
	 * Set the shortest path to a parked car. It will eventually be drawn.
	 * 
	 * @param path
	 *            a list of vertex
	 */
	public void setShortestPathToCar(List<Vertex> path) {
		this.mapViewer.setShortestPathToCar(path);
		this.repaintMap();

	}

}
