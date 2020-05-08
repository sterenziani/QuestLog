package ar.edu.itba.paw.webapp.exception;

public class ImageNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public ImageNotFoundException() {
        super();
    }

    public ImageNotFoundException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }

    public ImageNotFoundException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public ImageNotFoundException(String arg0) {
        super(arg0);
    }

    public ImageNotFoundException(Throwable arg0) {
        super(arg0);
    }
}
