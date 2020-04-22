package ar.edu.itba.paw.model;

public class Playstyle {
	private final long playstyle;
	private String name;
	
	public Playstyle(long playstyle, String name) {
		this.playstyle = playstyle;
		this.name = name;
	}
	
	public long getId() {
		return playstyle;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String n) {
		name = n;
	}
	
	@Override
	public int hashCode()
	{
		int hashCode = 1;
		hashCode = (int) (31 * hashCode + playstyle);
		hashCode = 31 * hashCode + name.hashCode();
		return hashCode;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof Playstyle)
		{
			Playstyle toCompare = (Playstyle) o;
			return this.playstyle == toCompare.getId() && this.name.equals(toCompare.getName());
		}
		return false;
	}
	
	@Override
	public String toString()
	{
		return name;
	}

}
