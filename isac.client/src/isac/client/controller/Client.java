package isac.client.controller;

import isac.client.listeners.UserListener;
import isac.client.utils.ClientUtils;
import isac.client.view.UserInterface;
import isac.core.constructs.ActiveEntity;
import isac.core.log.ILogger;

/**
 * 
 * Client application. It is the controller of the MVC pattern.
 * 
 * @author acco
 *
 */
public class Client extends ActiveEntity {

	private UserInterface ui;
	private ServerDaemon serverDaemon;
	private SubscriberDaemon subscriberDaemon;
	private ServerChecker serverChecker;
	private ClientUtils utils;

	public Client() {

		/*
		 * User key listener.
		 */
		UserListener userListener = new UserListener(this);
		this.ui = new UserInterface(userListener);

		ILogger gLog = this.ui.getGraphicalLogger();

		this.utils = new ClientUtils(this.ui);

		this.subscriberDaemon = new SubscriberDaemon(utils, gLog);
		this.serverDaemon = new ServerDaemon();
		this.serverChecker = new ServerChecker(this);

	}

	@Override
	protected void work() {
		this.subscriberDaemon.start();
		this.serverDaemon.start();
		this.serverChecker.start();
	}

	/**
	 * Change the client status message.
	 * 
	 * @param status
	 *            message
	 */
	public void setStatus(String status) {
		this.ui.setStatusLabel(status);
	}

	/**
	 * Move the client left, if possible.
	 */
	public void moveLeft() {
		utils.moveLeft();
	}

	/**
	 * Move the client right, if possible.
	 */
	public void moveRight() {
		utils.moveRight();
	}

	/**
	 * Move the client up, if possible.
	 */
	public void moveUp() {
		utils.moveUp();
	}

	/**
	 * Move the client down, if possible.
	 */
	public void moveDown() {
		utils.moveDown();
	}

	/**
	 * Park the car in the current position, if possible.
	 */
	public void park() {
		utils.park();
	}

	/**
	 * Remove the car from the current position, if possible.
	 */
	public void removeCar() {
		utils.removeCar();
	}

	/**
	 * Search for a free near parking place.
	 */
	public void searchPark() {
		utils.searchPark();
	}

	/**
	 * Locate a previously parked car.
	 */
	public void locateCar() {
		utils.locateCar();
	}

	public void repaintMap() {
		this.ui.repaintMap();
	}

}
