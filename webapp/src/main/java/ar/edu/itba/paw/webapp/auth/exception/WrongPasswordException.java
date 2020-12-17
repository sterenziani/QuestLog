package ar.edu.itba.paw.webapp.auth.exception;
import org.springframework.security.core.AuthenticationException;

public class WrongPasswordException extends AuthenticationException {
	private static final long serialVersionUID = 1L;

	public WrongPasswordException(String msg) {
        super(msg);
    }
}