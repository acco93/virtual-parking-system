package acco.isac.datastructures;

import acco.isac.server.inforepresentation.EnvironmentInfo;

/**
 * 
 * Vertex with environment information.
 * 
 * @author acco
 *
 */
public class EnvironmentVertex extends ShortestPathVertex {

	private EnvironmentInfo info;

	public EnvironmentVertex(String id, EnvironmentInfo info) {
		super(id);
		this.info = info;
	}

	public EnvironmentInfo getInfo() {
		return info;
	}

}
