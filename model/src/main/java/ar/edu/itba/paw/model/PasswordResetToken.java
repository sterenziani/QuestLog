package ar.edu.itba.paw.model;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tokens")
public class PasswordResetToken
{
	@Id @GeneratedValue
	private Long token_id;
	
	@OneToOne
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	private User user;
	
	@Column(length = 250)
	private String token;
	
	@Column(name = "expiration")
	private Date expiryDate;
	
	PasswordResetToken()
	{
		
	}
	
	public PasswordResetToken(String token, User u)
	{
		this.token = token;
		this.user = u;
	}
	
	public PasswordResetToken(String token, User u, Date d)
	{
		this.token = token;
		this.user = u;
		this.expiryDate = d;
	}
	
	public String getToken()
	{
		return token;
	}
	public void setToken(String token)
	{
		this.token = token;
	}
	public User getUser()
	{
		return user;
	}
	public void setUser(User user)
	{
		this.user = user;
	}
	public Date getExpiryDate()
	{
		return expiryDate;
	}
	public void setExpiryDate(Date expiryDate)
	{
		this.expiryDate = expiryDate;
	}
	
	@Override
	public int hashCode()
	{
		int hashCode = 1;
		hashCode = (int) (31 * hashCode + user.hashCode());
		hashCode = 31 * hashCode + token.hashCode();
		return hashCode;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof PasswordResetToken)
		{
			PasswordResetToken toCompare = (PasswordResetToken) o;
			return this.user.equals(toCompare.getUser()) && this.token.equals(toCompare.getToken());
		}
		return false;
	}
	
	@Override
	public String toString()
	{
		return user +":" +token;
	}
}