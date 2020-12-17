package ar.edu.itba.paw.webapp.auth.handlers;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.model.entity.Role;
import ar.edu.itba.paw.webapp.auth.schemes.jwt.JWTUtility;

@Component
public class PawAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
    private JWTUtility ut = new JWTUtility();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication auth) throws ServletException, IOException
    {
        Set<Role> roles = new HashSet<>();
        boolean flag = false;
        for(GrantedAuthority a : auth.getAuthorities())
        {
        	switch(a.getAuthority())
        	{
        		case "ROLE_ADMIN":
        			roles.add(new Role("Admin"));
        			break;
        		case "ROLE_USER":
        			flag = true;
        			break;
        	}
        }
        if(flag){
            res.addHeader("Authorization", "Bearer " + ut.createToken(auth.getName(), roles));
        }
    }
}
