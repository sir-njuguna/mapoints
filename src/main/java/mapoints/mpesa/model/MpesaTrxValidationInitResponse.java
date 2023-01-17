package mapoints.mpesa.model;

import javax.persistence.Entity;

@Entity
public class MpesaTrxValidationInitResponse extends BaseMpesaInitResponse {

    private String transactionId;
    private String occasion;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOccasion() {
        return occasion;
    }

    public void setOccasion(String occasion) {
        this.occasion = occasion;
    }
}
