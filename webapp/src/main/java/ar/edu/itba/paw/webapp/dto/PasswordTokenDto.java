package ar.edu.itba.paw.webapp.dto;
import java.time.LocalDate;
import javax.ws.rs.core.UriInfo;
import ar.edu.itba.paw.model.entity.PasswordResetToken;

public class PasswordTokenDto {
	private String token;
	private UserDto user;
	private LocalDate expiration;
	
	public static PasswordTokenDto fromToken(PasswordResetToken token, UriInfo uriInfo) {
		PasswordTokenDto dto = new PasswordTokenDto();
		dto.token = token.getToken();
		dto.setUser(UserDto.fromUser(token.getUser(), uriInfo));
		dto.setExpiration(token.getExpiryDate());
		return dto;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public LocalDate getExpiration() {
		return expiration;
	}

	public void setExpiration(LocalDate expiration) {
		this.expiration = expiration;
	}

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}	
}
