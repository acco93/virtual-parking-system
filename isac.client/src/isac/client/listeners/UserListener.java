package isac.client.listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import isac.client.controller.Client;

/**
 * 
 * Key listener implementation.
 * 
 * @author acco
 *
 */
public class UserListener implements KeyListener {

	private Client client;

	public UserListener(Client client) {
		this.client = client;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// empty
	}

	@Override
	public void keyPressed(KeyEvent e) {
		char c = e.getKeyChar();

		switch (c) {
		case 'a':
			this.client.moveLeft();
			break;
		case 's':
			this.client.moveDown();
			break;
		case 'd':
			this.client.moveRight();
			break;
		case 'w':
			this.client.moveUp();
			break;
		case 'n':
			this.client.searchPark();
			break;
		case 'p':
			this.client.park();
			break;
		case 'r':
			this.client.removeCar();
			break;
		case 'l':
			this.client.locateCar();
			break;
		default:
			// unknown key
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// empty
	}

}
