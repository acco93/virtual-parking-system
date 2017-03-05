package isac.client.controller;

import java.util.List;

import isac.client.listeners.UserListener;
import isac.client.utils.ClientUtils;
import isac.client.view.UserInterface;
import isac.core.constructs.ActiveEntity;
import isac.core.datastructures.Vertex;
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

		this.subscriberDaemon = new SubscriberDaemon(this, gLog);
		this.serverDaemon = new ServerDaemon();
		this.serverChecker = new ServerChecker(this, gLog);

		this.utils = ClientUtils.getInstance();

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
		this.repaintMap();
	}

	/**
	 * Move the client right, if possible.
	 */
	public void moveRight() {
		utils.moveRight();
		this.repaintMap();
	}

	/**
	 * Move the client up, if possible.
	 */
	public void moveUp() {
		utils.moveUp();
		this.repaintMap();
	}

	/**
	 * Move the client down, if possible.
	 */
	public void moveDown() {
		utils.moveDown();
		this.repaintMap();
	}

	/**
	 * Park the car in the current position, if possible.
	 */
	public void park() {
		utils.park();
		this.repaintMap();
	}

	/**
	 * Remove the car from the current position, if possible.
	 */
	public void removeCar() {
		utils.removeCar();
		this.repaintMap();
	}

	/**
	 * Search for a free near parking place.
	 */
	public void searchPark() {
		List<Vertex> path = utils.searchPark();
		this.ui.setNearestParkPath(path);
		this.repaintMap();
	}

	/**
	 * Locate a previously parked car.
	 */
	public void locateCar() {
		List<Vertex> path = utils.locateCar();
		this.ui.setNearestParkPath(path);
		this.repaintMap();
	}

	public void repaintMap() {
		this.ui.repaintMap();
	}

}
