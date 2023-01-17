package mapoints.payment.model;

public class PaymentException extends Exception{
    private final PaymentExceptionType exceptionType;

    public PaymentException(Throwable cause, PaymentExceptionType exceptionType) {
        super(cause);
        this.exceptionType = exceptionType;
    }

    public PaymentExceptionType getExceptionType() {
        return exceptionType;
    }
}
