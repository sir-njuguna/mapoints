package mapoints.lib.exception;


public enum ExceptionType {
    NOT_FOUND(404),
    ALREADY_EXISTS(409),
    BAD_REQUEST(400),
    NOT_AUTHORISED(401);

    private final int type;

    ExceptionType(int type) {
        this.type = type;
    }

    public int value(){
        return type;
    }
}
