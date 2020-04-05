package ar.edu.itba.paw.webapp.exception;

public class DeveloperNotFoundException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public DeveloperNotFoundException() {
		super();
	}

	public DeveloperNotFoundException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public DeveloperNotFoundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public DeveloperNotFoundException(String arg0) {
		super(arg0);
	}

	public DeveloperNotFoundException(Throwable arg0) {
		super(arg0);
	}

}
