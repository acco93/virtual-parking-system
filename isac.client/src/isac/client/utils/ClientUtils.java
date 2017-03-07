package isac.client.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import acco.isac.algorithms.DijkstraAlgorithm;
import acco.isac.algorithms.NearestFreePark;
import isac.client.controller.EnvironmentInteraction;
import isac.client.controller.LocalInteraction;
import isac.client.model.Storage;
import isac.client.view.UserInterface;
import isac.core.data.Position;
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

	private Storage storage;
	private boolean isSearchingNearestPark;
	private boolean isLocatingCar;
	private EnvironmentInteraction envInteraction;
	private UserInterface ui;
	private LocalInteraction localInteraction;

	public ClientUtils(UserInterface ui) {

		this.ui = ui;

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

		this.localInteraction = new LocalInteraction(this.ui);
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

		if (!alreadyParked) {

			// please park in parking place <.<

			envInteraction.park(this.storage.getUserPosition());

			int r = this.storage.getUserPosition().getRow();
			int c = this.storage.getUserPosition().getColumn();

			this.storage.setCarPosition(Optional.of(new Position(r, c)));

			if (this.isSearchingNearestPark) {
				// turn off the search
				this.searchPark();
			}
		}

		this.ui.repaintMap();

	}

	/**
	 * Remove the car from the current position, if possible.
	 */
	public void removeCar() {

		boolean alreadyParked = this.storage.getCarPosition().isPresent();
		if (alreadyParked && this.storage.getCarPosition().get().equals(this.storage.getUserPosition())) {

			this.storage.setCarPosition(Optional.empty());
			envInteraction.remove(this.storage.getUserPosition());

			if (this.isLocatingCar) {
				// turn off the search
				this.locateCar();
			}

		}

		this.ui.repaintMap();

	}

	/**
	 * Search for a free near parking place.
	 * 
	 * @return the path to a free park.
	 */
	public void searchPark() {

		if (!this.isSearchingNearestPark) {
			this.isSearchingNearestPark = true;
			this.searchParkProcedure();
		} else {
			this.isSearchingNearestPark = false;
			this.ui.setNearestParkPath(new LinkedList<>());
		}

		this.ui.repaintMap();

	}

	private void searchParkProcedure() {

		if (this.storage.isServerOn()) {
			// if the server is on
			Graph map = this.storage.getMap();

			Vertex source = map.getVertexFromPosition(this.storage.getUserPosition());
			if (source == null) {
				/*
				 * The user is outside the map
				 */
				this.ui.setNearestParkPath(new LinkedList<>());
				return;
			}
			NearestFreePark nfp = new NearestFreePark(map, source);
			List<Vertex> path = nfp.find();
			this.ui.setNearestParkPath(path);
		} else {
			// otherwise try to use some local info
			this.localInteraction.searchNearFreePark();

		}
	}

	/**
	 * Locate a previously parked car.
	 * 
	 * @return the path to the car
	 */
	public void locateCar() {

		if (!this.isLocatingCar && this.storage.getCarPosition().isPresent()) {
			this.isLocatingCar = true;
			this.locateCarProcedure();
		} else {
			this.isLocatingCar = false;
			this.ui.setShortestPathToCar(new LinkedList<>());
		}

		this.ui.repaintMap();

	}

	private void locateCarProcedure() {

		Graph map = this.storage.getMap();

		Vertex source = map.getVertexFromPosition(this.storage.getUserPosition());
		if (source == null) {
			/*
			 * The user is outside the map
			 */
			this.ui.setShortestPathToCar(new LinkedList<>());
			return;
		}

		DijkstraAlgorithm da = new DijkstraAlgorithm(map, source);
		List<Vertex> path = da.getPath(map.getVertexFromPosition(this.storage.getCarPosition().get()));
		this.ui.setShortestPathToCar(path);

	}

	private void generalMove(int row, int column) {

		// int worldRow = this.storage.getWorldRows();
		// int worldColumns = this.storage.getWorldColumns();

		Position newPosition = new Position(row, column);

		this.storage.setUserPosition(newPosition);

		// if it is on, recompute new place
		if (this.isSearchingNearestPark) {
			this.searchParkProcedure();
		}

		if (this.isLocatingCar) {
			this.locateCarProcedure();
		}

		this.ui.repaintMap();

	}

	private int userRow() {
		return storage.getUserPosition().getRow();
	}

	private int userColumn() {
		return storage.getUserPosition().getColumn();
	}

	public void repaintMap() {
		// if it is on, recompute new place
		if (this.isSearchingNearestPark) {
			this.searchParkProcedure();
		}

		if (this.isLocatingCar) {
			this.locateCarProcedure();
		}

		this.ui.repaintMap();
	}
}
