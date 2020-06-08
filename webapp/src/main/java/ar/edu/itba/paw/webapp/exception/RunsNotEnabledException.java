package ar.edu.itba.paw.webapp.exception;

public class RunsNotEnabledException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public RunsNotEnabledException() {
		super();
	}

	public RunsNotEnabledException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public RunsNotEnabledException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public RunsNotEnabledException(String arg0) {
		super(arg0);
	}

	public RunsNotEnabledException(Throwable arg0) {
		super(arg0);
	}
}
