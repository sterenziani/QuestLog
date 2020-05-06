package ar.edu.itba.paw.webapp.form;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import ar.edu.itba.paw.webapp.validators.anotation.EmailUnique;
import ar.edu.itba.paw.webapp.validators.anotation.FieldMatch;
import ar.edu.itba.paw.webapp.validators.anotation.UserUnique;

@FieldMatch(baseField = "password", matchField = "repeatPassword")
public class UserForm
{
	@Size(min = 6, max = 100)
	@Pattern(regexp = "[a-zA-Z0-9]+")
	@UserUnique()
	private String username;
	
	@Size(min = 6, max = 100)
	private String password;
	
	@Size(min = 6, max = 100)
	private String repeatPassword;
	
	@Size(min = 3, max = 100)
	@Pattern(regexp = "[a-zA-Z0-9.]+@[a-zA-Z0-9.]+")
	@EmailUnique()
	private String email;
	

	public String getUsername()
	{
		return username;
	}
	public void setUsername(String username)
	{
		this.username = username;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	public String getRepeatPassword()
	{
		return repeatPassword;
	}
	public void setRepeatPassword(String repeatPassword)
	{
		this.repeatPassword = repeatPassword;
	}
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}
}
