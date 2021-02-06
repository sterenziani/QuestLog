package ar.edu.itba.paw.webapp.auth.schemes.basic;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.entity.User;
import ar.edu.itba.paw.webapp.auth.AuthenticatedUser;
import ar.edu.itba.paw.webapp.auth.exception.WrongPasswordException;

@Component
public class BasicProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    private UserService us;
    @Autowired
    private PasswordEncoder encoder;
    
    @Override
    public boolean supports(Class<?> auth) {
        return (BasicToken.class.isAssignableFrom(auth));
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails dets, UsernamePasswordAuthenticationToken auth) throws AuthenticationException {
    }

    // Recibe un authentication con las credenciales username:password en Base64
    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken auth) throws AuthenticationException
    {
        BasicToken basic_token = (BasicToken) auth;
        String[] uspass = new String(Base64.getDecoder().decode(basic_token.getToken())).split(":");
        if(uspass.length != 2)
            throw new WrongPasswordException("Invalid username or password");
        User user = us.findByUsername(uspass[0]).orElse(null);
        if(user == null || !encoder.matches(uspass[1], user.getPassword()))
            throw new WrongPasswordException("Invalid username or password");
        
        List<GrantedAuthority> auths = new ArrayList<>();
        auths.add(new SimpleGrantedAuthority("ROLE_USER"));
        if(user.getAdminStatus())
            auths.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return new AuthenticatedUser(user.getUsername(), auths);
    }
}
