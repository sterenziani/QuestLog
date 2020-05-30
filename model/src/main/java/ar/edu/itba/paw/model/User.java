package ar.edu.itba.paw.model;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="users")
public class User
{
	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_user_id_seq")
	@SequenceGenerator(allocationSize = 1, sequenceName = "users_user_id_seq", name = "users_user_id_seq")
	private Long id;
	
	@Column(length = 100, nullable = false, unique = true)
	private String username;
	
	@Column(length = 255, nullable = false)
	private String password;
	
	@Column(length = 100, nullable = false, unique = true)
	private String email;
	
	@Column(length = 25, nullable = false)
	private Locale locale;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "role_assignments", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role"))
	private Set<Role> roles = new HashSet<Role>();

	@ManyToMany(mappedBy = "backlog")
	private Set<Game> backlog = new HashSet<>();
	
	User()
	{
		// Let Hibernate do the work
	}
	
	@Deprecated
	public User(long id, String username, String password, String email, String locale)
	{
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.locale = Locale.forLanguageTag(locale);
	}
	
	public User(String username, String password, String email, String locale)
	{
		this.username = username;
		this.password = password;
		this.email = email;
		this.locale = Locale.forLanguageTag(locale);
	}

	public void addToBacklog(Game g){
		backlog.add(g);
	}

	public void removeFromBacklog(Game g){
		if(!isInBacklog(g))
			return;
		backlog.remove(g);
	}

	public boolean isInBacklog(Game g){
		return backlog.contains(g);
	}

	public Set<Game> getBacklog() {
		return backlog;
	}

	public Long getId()
	{
		return id;
	}

	public void setUsername(String s)
	{
		username = s;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}
	
	public Locale getLocale()
	{
		return locale;
	}

	public void setLocale(Locale locale)
	{
		this.locale = locale;
	}
	
	public boolean getAdminStatus()
	{
		return roles.contains(new Role("Admin"));
	}
	
	public void setAdminStatus(boolean value)
	{
		if(getAdminStatus())
		{
			if(!value)
				roles.remove(new Role("Admin"));
			return;
		}
		if(value)
			roles.add(new Role("Admin"));		
	}
	
	public void addRole(Role role)
	{
		roles.add(role);
		role.getUsers().add(this);
	}
	
	public void removeRole(Role role)
	{
		roles.remove(role);
		role.getUsers().remove(this);
	}
	
	@Override
	public int hashCode()
	{
		int hashCode = 1;
		hashCode = (int) (31 * hashCode + id);
		hashCode = 31 * hashCode + username.hashCode();
		return hashCode;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof User)
		{
			User toCompare = (User) o;
			return this.id == toCompare.getId() && this.username.equals(toCompare.getUsername());
		}
		return false;
	}
	
	@Override
	public String toString()
	{
		return username;
	}
}
