package ar.edu.itba.paw.webapp.auth.exception;
import org.springframework.security.core.AuthenticationException;

public class ExpiredTokenException extends AuthenticationException {
	private static final long serialVersionUID = 1L;

	public ExpiredTokenException() {
        super("Token expired.");
    }
}