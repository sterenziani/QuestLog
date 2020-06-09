package ar.edu.itba.paw.webapp.exception;

public class ScoresNotEnabledException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public ScoresNotEnabledException() {
		super();
	}

	public ScoresNotEnabledException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public ScoresNotEnabledException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ScoresNotEnabledException(String arg0) {
		super(arg0);
	}

	public ScoresNotEnabledException(Throwable arg0) {
		super(arg0);
	}
}
