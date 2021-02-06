package ar.edu.itba.paw.webapp.dto;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class JwtUserDto {
	private String username;
    private Set<String> roles;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = new HashSet<>(roles);
    }

    public static JwtUserDto fromAuthentication(Authentication authentication)
    {
    	final JwtUserDto dto = new JwtUserDto();
		@SuppressWarnings("unchecked")
		List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();
        for(GrantedAuthority a : authorities)
        {
        	if(a.toString().equals("ROLE_ADMIN"))
        		dto.roles.add("Admin");
        }
        dto.username = (((User) authentication.getPrincipal()).getUsername());
        return dto;
    }
}
