package ar.edu.itba.paw.webapp.dto;
import javax.ws.rs.core.UriInfo;

public class AnonBacklogDto {
	private String backlog;
	
	public static AnonBacklogDto fromCookie(String s, UriInfo uriInfo)
	{
		final AnonBacklogDto dto = new AnonBacklogDto();
		dto.setBacklog(s);
		return dto;
	}

	public String getBacklog() {
		return backlog;
	}

	public void setBacklog(String backlog) {
		this.backlog = backlog;
	}
}
