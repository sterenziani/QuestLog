package ar.edu.itba.paw.webapp.dto;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class EditUserPasswordDto {
    @NotNull
    @Size(min = 6, max = 100)
    private String password;
    private String token;

    public EditUserPasswordDto() {
    	
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
