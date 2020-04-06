package ar.edu.itba.paw.model;

import java.util.Date;

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
}
