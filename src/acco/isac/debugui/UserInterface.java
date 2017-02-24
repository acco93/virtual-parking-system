package acco.isac.debugui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import acco.isac.environment.Cell;
import acco.isac.environment.Environment;

public class UserInterface extends JFrame implements IUserInterface {

	private static final long serialVersionUID = 1L;
	private WorldViewer worldViewer;

	public UserInterface() {

		this.setTitle("Virtual car parking world");
		this.setSize(new Dimension(800, 600));
		//this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		this.setLayout(new BorderLayout());

		worldViewer = new WorldViewer();
		this.add(worldViewer, BorderLayout.CENTER);

		this.setVisible(true);

	}

	@Override
	public void setGrid(Cell[][] grid, int width, int height) {
		worldViewer.updateGrid(grid, width, height);

	}

	@Override
	public void setEnvironment(Environment e) {
		this.setGrid(e.getSensorsLayer(), e.getWidth(), e.getHeight());

	}

}
