package ar.edu.itba.paw.webapp.dto;
import javax.ws.rs.core.UriInfo;
import ar.edu.itba.paw.model.entity.Playstyle;

public class AvgTimeDto
{
	private PlaystyleDto playstyle;
	private String time;
	
	public static AvgTimeDto fromAvgTime(Playstyle style, String time, UriInfo uriInfo)
	{   
		final AvgTimeDto dto = new AvgTimeDto();
		dto.playstyle = PlaystyleDto.fromPlaystyle(style, uriInfo);
		dto.time = time;
		return dto;
	}

	public PlaystyleDto getPlaystyle() {
		return playstyle;
	}

	public void setPlaystyle(PlaystyleDto playstyle) {
		this.playstyle = playstyle;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
}
