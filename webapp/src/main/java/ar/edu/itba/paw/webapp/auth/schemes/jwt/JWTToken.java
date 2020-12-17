package ar.edu.itba.paw.webapp.auth.schemes.jwt;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import ar.edu.itba.paw.model.entity.User;

public class JWTToken extends UsernamePasswordAuthenticationToken {
	private static final long serialVersionUID = 1L;
	private String token;

    public JWTToken(String token){
        super(new User(),new ArrayList<>());
        this.token = token;
    }
    
    public JWTToken(String username, String password){
        super(username,password);
    }
    
    public JWTToken(Object principal, Object creds) {
        super(principal, creds);
    }
    
    public JWTToken(Object principal, Object creds, Collection<? extends GrantedAuthority> auths) {
        super(principal, creds, auths);
    }

    public String getToken(){
        return token;
    }
}