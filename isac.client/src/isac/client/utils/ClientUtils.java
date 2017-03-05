package isac.client.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import acco.isac.algorithms.DijkstraAlgorithm;
import acco.isac.algorithms.NearestFreePark;
import isac.client.controller.EnvironmentInteraction;
import isac.client.model.Storage;
import isac.core.data.InfoType;
import isac.core.data.Position;
import isac.core.data.SensorRepresentation;
import isac.core.datastructures.Graph;
import isac.core.datastructures.Vertex;

/**
 * 
 * Utility class, it perform client functionalities.
 * 
 * @author acco
 *
 */
public class ClientUtils {

	private static ClientUtils instance = new ClientUtils();
	private Storage storage;
	private boolean isSearchingNearestPark;
	private boolean isLocatingCar;
	private EnvironmentInteraction envInteraction;

	private ClientUtils() {
		this.storage = Storage.getInstance();
		/*
		 * Not searching for a free park at the beginning
		 */
		this.isSearchingNearestPark = false;
		/*
		 * Not searching for a parked car at the beginning
		 */
		this.isLocatingCar = false;
		/*
		 * Environment interaction "simulator"
		 */
		this.envInteraction = new EnvironmentInteraction();
	}

	public static ClientUtils getInstance() {
		return instance;
	}

	/**
	 * Move the client left, if possible.
	 */
	public void moveLeft() {
		this.generalMove(userRow(), userColumn() - 1);
	}

	/**
	 * Move the client right, if possible.
	 */
	public void moveRight() {
		this.generalMove(userRow(), userColumn() + 1);
	}

	/**
	 * Move the client down, if possible.
	 */
	public void moveDown() {
		this.generalMove(userRow() + 1, userColumn());
	}

	/**
	 * Move the client up, if possible.
	 */
	public void moveUp() {
		this.generalMove(userRow() - 1, userColumn());
	}

	/**
	 * Park the car in the current position, if possible.
	 */
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

	/**
	 * Remove the car from the current position, if possible.
	 */
	public void removeCar() {

		boolean alreadyParked = this.storage.getCarPosition().isPresent();
		if (alreadyParked && this.storage.getCarPosition().get().equals(this.storage.getUserPosition())) {

			this.storage.setCarPosition(Optional.empty());
			envInteraction.remove(this.storage.getUserPosition());

		}

	}

	/**
	 * Search for a free near parking place.
	 * 
	 * @return the path to a free park.
	 */
	public List<Vertex> searchPark() {

		if (!this.isSearchingNearestPark) {
			this.isSearchingNearestPark = true;
			this.searchParkProcedure();
		} else {
			this.isSearchingNearestPark = false;
			this.ui.setNearestParkPath(new LinkedList<>());
		}

	}

	private void searchParkProcedure() {
		Graph map = this.storage.getMap();
		NearestFreePark nfp = new NearestFreePark(map, map.getVertexFromPosition(this.storage.getUserPosition()));
		List<Vertex> path = nfp.find();
		this.ui.setNearestParkPath(path);
	}

	/**
	 * Locate a previously parked car.
	 * 
	 * @return the path to the car
	 */
	public List<Vertex> locateCar() {

		if (!this.storage.getCarPosition().isPresent()) {
			return;
		}

		if (!this.isLocatingCar) {
			this.isLocatingCar = true;
			this.locateCarProcedure();
		} else {
			this.isLocatingCar = false;
			this.ui.setShortestPathToCar(new LinkedList<>());
		}

	}

	private void locateCarProcedure() {
		Graph map = this.storage.getMap();

		DijkstraAlgorithm da = new DijkstraAlgorithm(map, map.getVertexFromPosition(this.storage.getUserPosition()));
		List<Vertex> path = da.getPath(map.getVertexFromPosition(this.storage.getCarPosition().get()));
		this.ui.setShortestPathToCar(path);
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
		if (this.isSearchingNearestPark) {
			this.searchParkProcedure();
		}

		if (this.isLocatingCar) {
			this.locateCarProcedure();
		}

	}

	private int userRow() {
		return storage.getUserPosition().getRow();
	}

	private int userColumn() {
		return storage.getUserPosition().getColumn();
	}
}
