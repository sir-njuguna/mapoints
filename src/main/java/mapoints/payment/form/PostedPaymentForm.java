package mapoints.payment.form;

public class PostedPaymentForm extends PaymentForm {
    private String accountNumber;
    private String transactionCode;

    public String getAccountNumber() {
        return accountNumber == null
                ? null
                : accountNumber.toLowerCase().trim();
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }
}
