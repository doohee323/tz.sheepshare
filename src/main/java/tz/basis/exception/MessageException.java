package tz.basis.exception;

public abstract class MessageException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private Object[] args = null;

    public Object[] getArgs(){
        return this.args;
    }

    public MessageException(String propKey) {
        super(propKey);
    }

    public MessageException(String propKey, Object[] args) {
        super(propKey);
        this.args = args;
    }

    public MessageException(String propKey, Throwable t) {
        super(propKey, t);
        this.args = new Object[] { t.getMessage() };
    }

    public MessageException(String propKey, Object[] args, Throwable t) {
        super(propKey, t);

        this.args = new Object[args.length + 1];
        System.arraycopy(args, 0, this.args, 0, args.length);
        this.args[args.length] = t.getMessage();
    }
}