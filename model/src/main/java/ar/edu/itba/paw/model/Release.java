package ar.edu.itba.paw.model;
import java.sql.Date;

public class Release
{
	private Region region;
	private Date date;
	
	public Release(Region r, Date date)
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
}
