package tz.basis.core.exception;

public class TzException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 8737632253185272562L;

    public TzException() {
        super();
    }

    public TzException(String msg) {
        super(msg);
    }

    public TzException(Exception e) {
        super(e);
    }

    public TzException(String msg, Exception e) {
        super(msg, e);
    }
}
