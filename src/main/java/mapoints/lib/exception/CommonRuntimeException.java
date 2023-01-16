package mapoints.lib.exception;

public class CommonRuntimeException extends RuntimeException {
    private final ExceptionType type;
    private final String msg;

    public CommonRuntimeException(ExceptionType type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return msg;
    }

    public ExceptionType getType() {
        return type;
    }
}
