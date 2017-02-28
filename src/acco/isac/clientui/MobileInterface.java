package acco.isac.clientui;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class MobileInterface extends JPanel {

	private static final long serialVersionUID = 1L;

	public MobileInterface() {

		this.setLayout(new BorderLayout());

		this.add(new MapViewer(), BorderLayout.CENTER);

		JPanel bottomPanel = new JPanel(new BorderLayout());

		JButton left = new JButton("LEFT");
		JButton right = new JButton("RIGHT");
		JButton up = new JButton("UP");
		JButton bottom = new JButton("BOTTOM");

		bottomPanel.add(left, BorderLayout.WEST);
		bottomPanel.add(right, BorderLayout.EAST);
		bottomPanel.add(up, BorderLayout.NORTH);
		bottomPanel.add(bottom, BorderLayout.SOUTH);

		this.add(bottomPanel, BorderLayout.SOUTH);

	}

}
