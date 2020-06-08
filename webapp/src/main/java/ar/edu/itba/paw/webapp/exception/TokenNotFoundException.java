package ar.edu.itba.paw.webapp.exception;

public class TokenNotFoundException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public TokenNotFoundException() {
		super();
	}

	public TokenNotFoundException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public TokenNotFoundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public TokenNotFoundException(String arg0) {
		super(arg0);
	}

	public TokenNotFoundException(Throwable arg0) {
		super(arg0);
	}

}
