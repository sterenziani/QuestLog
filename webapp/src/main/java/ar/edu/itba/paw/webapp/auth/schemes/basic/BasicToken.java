package ar.edu.itba.paw.webapp.auth.schemes.basic;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import ar.edu.itba.paw.model.entity.User;

public class BasicToken extends UsernamePasswordAuthenticationToken {
	private static final long serialVersionUID = 1L;
	private String token;

    public BasicToken(String token){
        super(new User(),new ArrayList<>());
        this.token = token;
    }
    
    public BasicToken(String user, String pass){
        super(user,pass);
    }
    
    public BasicToken(Object principal, Object creds) {
        super(principal, creds);
    }
    
    public BasicToken(Object principal, Object creds, Collection<? extends GrantedAuthority> auths) {
        super(principal, creds, auths);
    }

    public String getToken(){
        return token;
    }
}