package acco.isac.server.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import acco.isac.client.ui.ClientUserInterface;

public class ServerUserInterface extends JFrame {

	private static final long serialVersionUID = 1L;

	public ServerUserInterface() {

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

		//MobileInterface mapViewer = new MobileInterface();
		//tabbedPane.add("Map (client-side)", mapViewer);
		
		//ToolBar toolbar = new ToolBar();

		//this.add(toolbar, BorderLayout.NORTH);

		StatusBar statusbar = new StatusBar();
		this.add(statusbar, BorderLayout.SOUTH);

		this.setVisible(true);

	}

}
