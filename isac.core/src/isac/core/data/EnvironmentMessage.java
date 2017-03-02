package isac.core.data;

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
