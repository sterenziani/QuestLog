package ar.edu.itba.paw.webapp.auth;
import java.util.Collection;
import java.util.HashSet;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.User;

@Component
public class PawUserDetailsService implements UserDetailsService
{
	@Autowired
	private UserService us;
	
	@Autowired
	private PasswordEncoder encoder;
	
	private Pattern BCRYPT_PATTERN = Pattern.compile("\\A\\$2a?\\$\\d\\d\\$[./0-9A-Za-z]{53}");
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		final User user = us.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username +" not found"));
		final Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		
		// Aca están hardcodeados pero Sotuyo quiere que la version final tenga lógica
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		final String password;
		if(user.getPassword() == null || !BCRYPT_PATTERN.matcher(user.getPassword()).matches())
		{
			password = encoder.encode(user.getPassword());
		}
		else
		{
			password = user.getPassword();
		}
		return new org.springframework.security.core.userdetails.User(username, password, authorities);
	}
}
