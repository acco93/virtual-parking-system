package isac.core.message;

import isac.core.data.Position;

/**
 * Client to environment message.
 * 
 * @author acco
 *
 */
public class EnvironmentMessage {

	private Position position;
	private boolean park;

	public EnvironmentMessage(Position position, boolean park) {
		this.position = position;
		this.park = park;
	}

	public Position getPosition() {
		return position;
	}

	public boolean isPark() {
		return park;
	}

}
