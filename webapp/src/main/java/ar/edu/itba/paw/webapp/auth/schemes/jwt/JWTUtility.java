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

// Basado en https://github.com/BHRother/spring-boot-security-jwt/tree/master/src/main/java/nl/palmapps/myawesomeproject/security/util
public class JWTUtility {
	
	private static final int TOKEN_DURATION_MINUTES = 30;

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
	    System.out.println("Pizza Pizza");
	    System.out.println(roles.toString());
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
        System.out.println("111.");
        try
        {
            System.out.println("222.");
            Claims body = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(getJWTKey().getBytes())).build().parseClaimsJws(token).getBody();
            JwtUserDto u = new JwtUserDto();
            u.setUsername(body.getSubject());
            System.out.println("223.");
            System.out.println(body.get("roles", List.class));
            System.out.println("224.");
            List<String> roleTypes = body.get("roles", List.class);
            u.setRoles(new HashSet<>(roleTypes));
            return u;
        }
        catch (ExpiredJwtException e)
        {
            System.out.println("333.");
            throw new ExpiredTokenException();
        }
        catch (JwtException e)
        {
            System.out.println("444.");
            return null;
        }
    }
}
