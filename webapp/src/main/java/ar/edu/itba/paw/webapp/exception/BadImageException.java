package ar.edu.itba.paw.webapp.exception;

public class BadImageException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public BadImageException() {
        super();
    }

    public BadImageException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }

    public BadImageException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public BadImageException(String arg0) {
        super(arg0);
    }

    public BadImageException(Throwable arg0) {
        super(arg0);
    }
}
