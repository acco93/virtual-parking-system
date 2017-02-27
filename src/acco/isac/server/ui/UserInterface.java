package acco.isac.server.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import acco.isac.clientui.MapViewer;

public class UserInterface extends JFrame implements IUserInterface {

	private static final long serialVersionUID = 1L;

	public UserInterface() {

		this.setTitle("Server-side - Virtual car parking");
		this.setSize(new Dimension(800, 600));
		// this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setJMenuBar(new MenuBar());

		this.setLayout(new BorderLayout());

		JTabbedPane tabbedPane = new JTabbedPane();
		
		this.add(tabbedPane, BorderLayout.CENTER);

		ParkViewer parkViewer = new ParkViewer();
		tabbedPane.addTab("Park", parkViewer);

		GraphViewer graphViewer = new GraphViewer();
		tabbedPane.add("Graph", graphViewer);

		MapViewer mapViewer = new MapViewer();
		tabbedPane.add("Map (client-side)", mapViewer);
		
		//ToolBar toolbar = new ToolBar();

		//this.add(toolbar, BorderLayout.NORTH);

		StatusBar statusbar = new StatusBar();
		this.add(statusbar, BorderLayout.SOUTH);

		this.setVisible(true);

	}

}
