package acco.isac.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import acco.isac.log.Logger;

public class ClientUserInterface extends JFrame implements KeyListener {

	private static final long serialVersionUID = 1L;
	private MapViewer v;
	private JLabel statusLabel;

	public ClientUserInterface() {

		this.setTitle("Client-side - Virtual car parking");
		this.setSize(new Dimension(800, 600));

		this.addKeyListener(this);

		this.setFocusable(true);

		this.setLayout(new BorderLayout());

		v = new MapViewer();

		this.add(v, BorderLayout.CENTER);

		JLabel helpLabel = new JLabel(
				"<html>HELP: use <b>W</b>, <b>A</b>, <b>S</b>, <b>D</b> to move, <b>N</b> to find the nearest parking, <b>P</b> to park and <b>L</b> to locate a previously parked car.</html>",
				SwingConstants.CENTER);
		helpLabel.setFont(helpLabel.getFont().deriveFont(Font.PLAIN));
		this.add(helpLabel, BorderLayout.NORTH);

		statusLabel = new JLabel("Connected to server", SwingConstants.CENTER);
		statusLabel.setOpaque(true);
		statusLabel.setBackground(Color.DARK_GRAY);
		
		this.add(statusLabel, BorderLayout.SOUTH);

		this.setVisible(true);

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		char c = e.getKeyChar();

		switch (c) {
		case 'a':
			Logger.getInstance().info("Going left");
			v.left();
			break;
		case 's':
			Logger.getInstance().info("Going down");
			v.down();
			break;
		case 'd':
			Logger.getInstance().info("Going right");
			v.right();
			break;
		case 'w':
			Logger.getInstance().info("Going up");
			v.up();
			break;
		case 'n':
			Logger.getInstance().info("Searching for free parking");
			break;
		case 'p':
			Logger.getInstance().info("Parking");
			break;
		case 'l':
			Logger.getInstance().info("Locating car");
			break;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void setStatusLabel(String string, Color color) {
		this.statusLabel.setText(string);
		this.statusLabel.setForeground(color);
	}

}
