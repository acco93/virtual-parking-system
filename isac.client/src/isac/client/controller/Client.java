package isac.client.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import acco.isac.algorithms.DijkstraAlgorithm;
import acco.isac.algorithms.NearestFreePark;
import isac.client.listeners.UserListener;
import isac.client.model.Storage;
import isac.client.view.UserInterface;
import isac.core.constructs.ActiveEntity;
import isac.core.data.InfoType;
import isac.core.data.Position;
import isac.core.data.SensorRepresentation;
import isac.core.datastructures.Graph;
import isac.core.datastructures.Vertex;
import isac.core.log.ILogger;

/**
 * 
 * Client application. Controller
 * 
 * @author acco
 *
 */
public class Client extends ActiveEntity {

	private UserInterface ui;
	private ServerDaemon serverDaemon;
	private SubscriberDaemon subscriberDaemon;
	private EnvironmentInteraction envInteraction;
	private ServerChecker serverChecker;
	private boolean searchingNearestPark;
	private boolean locatingCar;
	private Storage storage;

	public Client() {

		UserListener userListener = new UserListener(this);
		this.ui = new UserInterface(userListener);

		this.storage = Storage.getInstance();

		ILogger gLog = this.ui.getGraphicalLogger();

		this.subscriberDaemon = new SubscriberDaemon(gLog);
		this.serverDaemon = new ServerDaemon(gLog);
		this.serverChecker = new ServerChecker(this, gLog);

		this.envInteraction = new EnvironmentInteraction();

	}

	@Override
	protected void work() {
		this.subscriberDaemon.start();
		this.serverDaemon.start();
		this.serverChecker.start();
	}

	public void setStatus(String status) {
		this.ui.setStatusLabel(status);
	}

	public void moveLeft() {
		int r = Storage.getInstance().getUserPosition().getRow();
		int c = Storage.getInstance().getUserPosition().getColumn();
		this.generalMove(r, c - 1);
		this.ui.repaintMap();
	}

	public void moveRight() {
		int r = Storage.getInstance().getUserPosition().getRow();
		int c = Storage.getInstance().getUserPosition().getColumn();
		this.generalMove(r, c + 1);
		this.ui.repaintMap();
	}

	public void moveUp() {
		int r = Storage.getInstance().getUserPosition().getRow();
		int c = Storage.getInstance().getUserPosition().getColumn();
		this.generalMove(r - 1, c);
		this.ui.repaintMap();
	}

	public void moveDown() {
		int r = Storage.getInstance().getUserPosition().getRow();
		int c = Storage.getInstance().getUserPosition().getColumn();
		this.generalMove(r + 1, c);
		this.ui.repaintMap();
	}

	private void generalMove(int row, int column) {

		int worldRow = this.storage.getWorldRows();
		int worldColumns = this.storage.getWorldColumns();

		if (row < 0 || row > worldRow || column < 0 || column > worldColumns) {
			return;
		}

		Position newPosition = new Position(row, column);

		this.storage.setUserPosition(newPosition);

		// if it is on, recompute new place
		if (this.searchingNearestPark) {
			this.searchParkProcedure();
		}

		if (this.locatingCar) {
			this.locateCarProcedure();
		}

	}

	public void park() {

		boolean alreadyParked = this.storage.getCarPosition().isPresent();
		Position userPosition = this.storage.getUserPosition();
		Vertex vertex = this.storage.getMap().getVertexFromPosition(userPosition);
		InfoType blockType = vertex.getInfo().getType();

		if (!alreadyParked && blockType == InfoType.SENSOR && ((SensorRepresentation) vertex.getInfo()).isFree()) {

			// please park in parking place <.<

			envInteraction.park(this.storage.getUserPosition());

			int r = this.storage.getUserPosition().getRow();
			int c = this.storage.getUserPosition().getColumn();

			this.storage.setCarPosition(Optional.of(new Position(r, c)));
		}

	}

	public void removeCar() {

		boolean alreadyParked = this.storage.getCarPosition().isPresent();
		if (alreadyParked && this.storage.getCarPosition().get().equals(this.storage.getUserPosition())) {

			this.storage.setCarPosition(Optional.empty());
			envInteraction.remove(this.storage.getUserPosition());

		}

	}

	public void searchPark() {

		if (!this.searchingNearestPark) {
			this.searchingNearestPark = true;
			this.searchParkProcedure();
		} else {
			this.searchingNearestPark = false;
			this.ui.setNearestParkPath(new LinkedList<>());
		}

	}

	private void searchParkProcedure() {
		Graph map = this.storage.getMap();
		NearestFreePark nfp = new NearestFreePark(map, map.getVertexFromPosition(this.storage.getUserPosition()));
		List<Vertex> path = nfp.find();
		this.ui.setNearestParkPath(path);
	}

	public void locateCar() {

		if (!this.storage.getCarPosition().isPresent()) {
			return;
		}

		if (!this.locatingCar) {
			this.locatingCar = true;
			this.locateCarProcedure();
		} else {
			this.locatingCar = false;
			this.ui.setShortestPathToCar(new LinkedList<>());
		}

	}

	private void locateCarProcedure() {
		Graph map = this.storage.getMap();

		DijkstraAlgorithm da = new DijkstraAlgorithm(map, map.getVertexFromPosition(this.storage.getUserPosition()));
		List<Vertex> path = da.getPath(map.getVertexFromPosition(this.storage.getCarPosition().get()));
		this.ui.setShortestPathToCar(path);
	}

}
