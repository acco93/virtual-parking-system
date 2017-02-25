package acco.isac.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import acco.isac.server.SensorRepresentation;

public class UserInterface extends JFrame implements IUserInterface {

	private static final long serialVersionUID = 1L;
	private WorldViewer worldViewer;

	public UserInterface() {

		this.setTitle("Virtual car parking world");
		this.setSize(new Dimension(800, 600));
		// this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setLayout(new BorderLayout());

		worldViewer = new WorldViewer();
		this.add(worldViewer, BorderLayout.CENTER);

		this.setVisible(true);

	}


	public void updateSensors(ConcurrentHashMap<String, SensorRepresentation> storage) {
		SwingUtilities.invokeLater(() -> {
			worldViewer.updateSensors(storage);

		});	
	}

}
