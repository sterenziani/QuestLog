package ar.edu.itba.paw.webapp.auth;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import ar.edu.itba.paw.webapp.auth.schemes.basic.BasicToken;
import ar.edu.itba.paw.webapp.auth.schemes.jwt.JWTToken;
import ar.edu.itba.paw.webapp.dto.JwtUserDto;

public class PawAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public PawAuthenticationFilter() {
        super("/api/**");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException
    {
        String header = request.getHeader("Authorization");
        if(header == null)
        	header = "";
        
        if(header.startsWith("Basic "))
        {
            String basic_token = header.substring(6);
            return getAuthenticationManager().authenticate(new BasicToken(basic_token));
        }
        else if(header.startsWith("Bearer "))
        {
            String bearer_token = header.substring(7);
            System.out.println("bearer: " + bearer_token);
            return getAuthenticationManager().authenticate(new JWTToken(bearer_token));
        }
        else
        {
            List<GrantedAuthority> auths = new ArrayList<>();
            auths.add(new SimpleGrantedAuthority("ROLE_ANONYMOUS"));
            return new AnonymousAuthenticationToken("anonymous", new JwtUserDto(), auths);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth)
            throws IOException, ServletException
    {
    	super.successfulAuthentication(request, response, chain, auth);
        chain.doFilter(request, response);
    }
}