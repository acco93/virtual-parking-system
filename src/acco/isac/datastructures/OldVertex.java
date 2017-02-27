package acco.isac.datastructures;

import acco.isac.server.inforepresentation.EnvironmentInfo;
import acco.isac.server.inforepresentation.InfoType;

/**
 *
 * @author http://www.vogella.com/tutorials/JavaAlgorithmsDijkstra/article.html
 *
 */

public class OldVertex {

	final private String id;
	final private String name;
	private EnvironmentInfo info;

	public OldVertex(String id, String name, EnvironmentInfo info) {
		this.id = id;
		this.name = name;
		this.info = info;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}


	public EnvironmentInfo getInfo() {
		return info;
	}

	@Override
	public String toString() {
		return "Vertex [id=" + id + ", name=" + name + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OldVertex other = (OldVertex) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}



}
