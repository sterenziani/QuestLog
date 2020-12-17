package ar.edu.itba.paw.webapp.auth.schemes.jwt;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Set;
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
		String token = Jwts.builder()
            .setIssuer("QuestLog-API")
            .setAudience("QuestLog-App")
            .claim("roles", roles)
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
            u.setRoles(body.get("roles", Set.class));
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
