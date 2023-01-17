package mapoints.mpesa.view;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MpesaTransactionValidationRequest {
    @JsonProperty("Initiator")
    private String initiator;

    @JsonProperty("SecurityCredential")
    private String securityCredential;

    @JsonProperty("CommandID")
    private String commandId;

    @JsonProperty("TransactionID")
    private String transactionId;

    @JsonProperty("PartyA")
    private String partyA;

    @JsonProperty("IdentifierType")
    private Integer identifierType;

    @JsonProperty("ResultURL")
    private String resultsUrl;

    @JsonProperty("QueueTimeOutURL")
    private String queueTimeoutUrl;

    @JsonProperty("Remarks")
    private String remarks;

    @JsonProperty("Occasion")
    private String occasion;

    public String getInitiator() {
        return initiator;
    }

    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    public String getSecurityCredential() {
        return securityCredential;
    }

    public void setSecurityCredential(String securityCredential) {
        this.securityCredential = securityCredential;
    }

    public String getCommandId() {
        return commandId;
    }

    public void setCommandId(String commandId) {
        this.commandId = commandId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPartyA() {
        return partyA;
    }

    public void setPartyA(String partyA) {
        this.partyA = partyA;
    }

    public Integer getIdentifierType() {
        return identifierType;
    }

    public void setIdentifierType(Integer identifierType) {
        this.identifierType = identifierType;
    }

    public String getResultsUrl() {
        return resultsUrl;
    }

    public void setResultsUrl(String resultsUrl) {
        this.resultsUrl = resultsUrl;
    }

    public String getQueueTimeoutUrl() {
        return queueTimeoutUrl;
    }

    public void setQueueTimeoutUrl(String queueTimeoutUrl) {
        this.queueTimeoutUrl = queueTimeoutUrl;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getOccasion() {
        return occasion;
    }

    public void setOccasion(String occasion) {
        this.occasion = occasion;
    }
}
