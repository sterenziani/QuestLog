package ar.edu.itba.paw.webapp.exception;

public class ReviewNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ReviewNotFoundException() {
		super();
	}

	public ReviewNotFoundException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public ReviewNotFoundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ReviewNotFoundException(String arg0) {
		super(arg0);
	}

	public ReviewNotFoundException(Throwable arg0) {
		super(arg0);
	}

}
