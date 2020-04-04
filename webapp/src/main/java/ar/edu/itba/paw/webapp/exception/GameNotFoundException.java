package ar.edu.itba.paw.webapp.exception;

public class GameNotFoundException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public GameNotFoundException() {
		super();
	}

	public GameNotFoundException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public GameNotFoundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public GameNotFoundException(String arg0) {
		super(arg0);
	}

	public GameNotFoundException(Throwable arg0) {
		super(arg0);
	}
}
