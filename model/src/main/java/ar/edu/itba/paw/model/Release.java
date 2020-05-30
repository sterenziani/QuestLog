package ar.edu.itba.paw.model;
import ar.edu.itba.paw.model.compositekeys.ReleaseKey;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "releases")
public class Release
{
	@EmbeddedId
	private ReleaseKey id;

	@ManyToOne
	@MapsId("game")
	@JoinColumn(name = "game")
	private Game game;

	@ManyToOne
	@MapsId("region")
	@JoinColumn(name = "region")
	private Region region;

	@Column(name = "release_date")
	private Date date;

	public Release(){
		// Just for Hibernate
	}
	
	public Release(Game g, Region r, Date date)
	{
		this.region = r;
		this.date = date;
	}
	
	public Region getRegion()
	{
		return region;
	}
	
	public Date getDate()
	{
		return date;
	}
	
	@Override
	public int hashCode()
	{
		int hashCode = 1;
		hashCode = 31 * hashCode + region.hashCode();
		hashCode = 31 * hashCode + date.hashCode();
		return hashCode;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof Release)
		{
			Release toCompare = (Release) o;
			return this.region.equals(toCompare.getRegion()) && this.date.equals(toCompare.getDate());
		}
		return false;
	}
	
	@Override
	public String toString()
	{
		return region.getShortName() +": " +date;
	}
}
