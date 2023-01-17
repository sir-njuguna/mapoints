package mapoints.payment.model;

public enum PaymentChannel {
    MPESA("M-PESA");

    private final String value;

    PaymentChannel(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
