package ar.edu.itba.paw.webapp.auth.schemes.jwt;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ar.edu.itba.paw.model.entity.Role;
import ar.edu.itba.paw.webapp.auth.AuthenticatedUser;
import ar.edu.itba.paw.webapp.auth.exception.MalformedTokenException;
import ar.edu.itba.paw.webapp.dto.JwtUserDto;

@Component
public class JWTProvider extends AbstractUserDetailsAuthenticationProvider {
    public JWTProvider(){
    }
    
    public JWTUtility ut = new JWTUtility();
    
    @Override
    public boolean supports(Class<?> auth) {
        return (JWTToken.class.isAssignableFrom(auth));
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails dets, UsernamePasswordAuthenticationToken auth) throws AuthenticationException {
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken auth) throws AuthenticationException
    {
        JWTToken jwt_token = (JWTToken) auth;
        String token = jwt_token.getToken();
        JwtUserDto us = ut.parseToken(token);
        if (us == null)
            throw new MalformedTokenException("Invalid JTW token");
        
        List<GrantedAuthority> auths = new ArrayList<>();
        auths.add(new SimpleGrantedAuthority("ROLE_USER"));
        
        if(us.getRoles().contains(new Role("Admin")))
            auths.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        
        return new AuthenticatedUser(us.getUsername(), auths);
    }
}