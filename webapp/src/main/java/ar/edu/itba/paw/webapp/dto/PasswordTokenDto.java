package ar.edu.itba.paw.webapp.dto;
import java.time.LocalDate;

import javax.ws.rs.core.UriInfo;
import ar.edu.itba.paw.model.entity.PasswordResetToken;
import ar.edu.itba.paw.model.entity.User;

public class PasswordTokenDto {
	private String token;
	private User user;
	private LocalDate expiration;
	
	public static PasswordTokenDto fromToken(PasswordResetToken token, UriInfo uriInfo) {
		PasswordTokenDto dto = new PasswordTokenDto();
		dto.token = token.getToken();
		dto.user = token.getUser();
		dto.setExpiration(token.getExpiryDate());
		return dto;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LocalDate getExpiration() {
		return expiration;
	}

	public void setExpiration(LocalDate expiration) {
		this.expiration = expiration;
	}	
}
