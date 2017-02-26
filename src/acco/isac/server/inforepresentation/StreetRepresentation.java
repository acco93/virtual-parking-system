package acco.isac.server.inforepresentation;

import acco.isac.environment.Position;

public class StreetRepresentation implements EnvironmentInfo {

	private Position position;

	public StreetRepresentation(Position position) {
		this.position = position;
	}

	@Override
	public Position getPosition() {
		return this.position;
	}

	@Override
	public InfoType getType() {
		return InfoType.STREET;
	}

}
