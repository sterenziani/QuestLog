package ar.edu.itba.paw.webapp.dto;

import javax.validation.constraints.Min;

public class RegisterRunDto {
	
	public RegisterRunDto() {}
	
	@Min(0)
	private Long time;
	private Long platform;
	private Long playstyle;
	
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public Long getPlatform() {
		return platform;
	}
	public void setPlatform(Long platform) {
		this.platform = platform;
	}
	public Long getPlaystyle() {
		return playstyle;
	}
	public void setPlaystyle(Long playstyle) {
		this.playstyle = playstyle;
	}
}
