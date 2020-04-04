package ar.edu.itba.paw.webapp.exception;

public class PlatformNotFoundException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public PlatformNotFoundException() {
		super();
	}

	public PlatformNotFoundException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public PlatformNotFoundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public PlatformNotFoundException(String arg0) {
		super(arg0);
	}

	public PlatformNotFoundException(Throwable arg0) {
		super(arg0);
	}
}
