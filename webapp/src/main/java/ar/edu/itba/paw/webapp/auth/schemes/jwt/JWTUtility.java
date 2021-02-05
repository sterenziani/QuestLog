package ar.edu.itba.paw.webapp.auth.schemes.jwt;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import ar.edu.itba.paw.model.entity.Role;
import ar.edu.itba.paw.webapp.auth.exception.ExpiredTokenException;
import ar.edu.itba.paw.webapp.dto.JwtUserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JWTUtility {
	
	private static final int TOKEN_DURATION_MINUTES = 130000; //130k = About 90 days

    private String getJWTKey()
    {
        InputStream input = getClass().getClassLoader().getResourceAsStream("jwt.key");
        try
        {
            return IOUtils.toString(input, StandardCharsets.UTF_8);
        }
        catch (Exception e)
        {
            throw new RuntimeException();
        }
    }
    
	public String createToken(String username, Set<Role> roles)
	{
	    List<String> roleTypes = roles.stream().map(Role::getRoleName).collect(Collectors.toList());
		String token = Jwts.builder()
            .setIssuer("QuestLog-API")
            .setAudience("QuestLog-App")
            .claim("roles", roleTypes)
	        .setSubject(username)
	        .setIssuedAt(new Date())
	        .setExpiration(new Date(System.currentTimeMillis() + TOKEN_DURATION_MINUTES*60000))
	        .signWith(Keys.hmacShaKeyFor(getJWTKey().getBytes()))
	        .compact();
		return token;
    }
	
    @SuppressWarnings("unchecked")
	public JwtUserDto parseToken(String token)
    {
        try
        {
            Claims body = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(getJWTKey().getBytes())).build().parseClaimsJws(token).getBody();
            JwtUserDto u = new JwtUserDto();
            u.setUsername(body.getSubject());
            List<String> roleTypes = body.get("roles", List.class);
            u.setRoles(new HashSet<>(roleTypes));
            return u;
        }
        catch (ExpiredJwtException e)
        {
            throw new ExpiredTokenException();
        }
        catch (JwtException e)
        {
            return null;
        }
    }
}
