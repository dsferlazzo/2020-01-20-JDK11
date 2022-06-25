package it.polito.tdp.artsmia.model;

public class Artist {
	
	private int artistId;
	private String name;
	public Artist(int artistId, String name) {
		super();
		this.artistId = artistId;
		this.name = name;
	}
	public int getArtistId() {
		return artistId;
	}
	public String getName() {
		return name;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + artistId;
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
		Artist other = (Artist) obj;
		if (artistId != other.artistId)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	@Override
	public String toString() {	//TODO
		return name + "(" + artistId + ")";
	}
	
	

}
