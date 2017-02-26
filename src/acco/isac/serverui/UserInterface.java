package acco.isac.serverui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

public class UserInterface extends JFrame implements IUserInterface {

	private static final long serialVersionUID = 1L;
	private ParkViewer worldViewer;

	public UserInterface() {

		this.setTitle("Server-side - Virtual car parking");
		this.setSize(new Dimension(800, 600));
		// this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setLayout(new BorderLayout());

		// worldViewer = new ParkViewer();
		// this.add(worldViewer, BorderLayout.CENTER);

		GraphViewer graphViewer = new GraphViewer();
		this.add(graphViewer, BorderLayout.CENTER);

		
		ToolBar toolbar = new ToolBar();

		this.add(toolbar, BorderLayout.NORTH);

		StatusBar statusbar = new StatusBar();
		this.add(statusbar, BorderLayout.SOUTH);

		this.setVisible(true);

	}

}
