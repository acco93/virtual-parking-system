package acco.isac.debugui;

import acco.isac.environment.Cell;
import acco.isac.environment.Environment;

public interface IUserInterface {

	void setEnvironment(Environment e);
	
	void setGrid(Cell[][] grid, int width, int height);

}
