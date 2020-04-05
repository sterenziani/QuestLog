package ar.edu.itba.paw.webapp.exception;

public class GenreNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public GenreNotFoundException() {
		super();
	}

	public GenreNotFoundException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public GenreNotFoundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public GenreNotFoundException(String arg0) {
		super(arg0);
	}

	public GenreNotFoundException(Throwable arg0) {
		super(arg0);
	}

}
