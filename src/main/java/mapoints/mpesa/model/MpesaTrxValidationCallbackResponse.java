package mapoints.mpesa.model;

import javax.persistence.Entity;

@Entity
public class MpesaTrxValidationCallbackResponse extends BaseMpesaCallbackResponse {
    private String debitParty;
    private String creditParty;
    private String mpesaReceiptNumber;
    private String occasionReference;

    public String getDebitParty() {
        return debitParty;
    }

    public void setDebitParty(String debitParty) {
        this.debitParty = debitParty;
    }

    public String getCreditParty() {
        return creditParty;
    }

    public void setCreditParty(String creditParty) {
        this.creditParty = creditParty;
    }

    public String getMpesaReceiptNumber() {
        return mpesaReceiptNumber;
    }

    public void setMpesaReceiptNumber(String mpesaReceiptNumber) {
        this.mpesaReceiptNumber = mpesaReceiptNumber;
    }

    public String getOccasionReference() {
        return occasionReference;
    }

    public void setOccasionReference(String occasionReference) {
        this.occasionReference = occasionReference;
    }

}
