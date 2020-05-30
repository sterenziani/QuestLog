package ar.edu.itba.paw.model;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="roles")
public class Role
{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roles_role_seq")
	@SequenceGenerator(allocationSize = 1, sequenceName = "roles_role_seq", name = "roles_role_seq")
	private Long role;

	@Column(name="role_name", length = 50, nullable = false, unique = true)
	private String roleName;
	
	@ManyToMany(mappedBy = "roles")
	private Set<User> users = new HashSet<>();

	Role()
	{
		
	}

	public Role(Number role_id)
	{
		this.role = role_id.longValue();
	}
	
	public Role(String role_name)
	{
		this.roleName = role_name;
	}
	
	public Long getRole() {
		return role;
	}

	public void setRole(Long role) {
		this.role = role;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String role_name) {
		this.roleName = role_name;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
	
	@Override
	public int hashCode()
	{
		return roleName.hashCode();
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof Role)
		{
			Role toCompare = (Role) o;
			return this.roleName.equals(toCompare.getRoleName());
		}
		return false;
	}
	
	@Override
	public String toString()
	{
		return roleName;
	}
}
