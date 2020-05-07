package ar.edu.itba.paw.webapp.form;
import javax.validation.constraints.Size;
import ar.edu.itba.paw.webapp.validators.anotation.FieldMatch;

@FieldMatch(baseField = "password", matchField = "repeatPassword")
public class ChangePasswordForm
{
	@Size(min = 6, max = 100)
	private String password;
	
	@Size(min = 6, max = 100)
	private String repeatPassword;

	private String token;
	
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
	public String getToken()
	{
		return token;
	}
	public void setToken(String token)
	{
		this.token = token;
	}
}