package ar.edu.itba.paw.model;

import javax.persistence.*;

@Entity
@Table(name="playstyles")
public class Playstyle {
	
	@Id
	@Column(name = "playstyle")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "playstyles_playstyle_id_seq")
	@SequenceGenerator(allocationSize = 1, sequenceName = "playstyles_playstyle_id_seq", name = "playstyles_playstyle_id_seq")
	private Long playstyle;
	
	@Column(name = "playstyle_name", nullable = false)
	private String name;
	
	public Playstyle()
	{
		
	}
	
	@Deprecated
	public Playstyle(long playstyle, String name) {
		this.playstyle = playstyle;
		this.name = name;
	}
	
	public Playstyle(String name) {
		this.name = name;
	}
	
	public Long getId() {
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
