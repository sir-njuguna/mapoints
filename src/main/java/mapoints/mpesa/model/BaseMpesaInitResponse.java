package mapoints.mpesa.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import mapoints.payment.model.TransactionTrace;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.ArrayList;
import java.util.List;


@MappedSuperclass
abstract class BaseMpesaInitResponse extends BaseMpesaResponse {

    private String userPhoneNumber;

    @JsonProperty("OriginatorConversationID")
    private String originatorConversationId;

    @JsonProperty("ConversationID")
    private String conversationId;

    @JsonProperty("ResponseCode")
    private Integer responseCode;

    @JsonProperty("ResponseDescription")
    private String responseDescription;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private List<TransactionTrace> transactionTraces;

    private PROCESSING_STATUS processingStatus;

    public enum PROCESSING_STATUS{
        PENDING,
        PROCESSED
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getOriginatorConversationId() {
        return originatorConversationId;
    }

    public void setOriginatorConversationId(String originatorConversationId) {
        this.originatorConversationId = originatorConversationId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }


    public List<TransactionTrace> getTransactionTraces() {
        return transactionTraces == null ? new ArrayList<>() : transactionTraces;
    }

    public void setTransactionTraces(List<TransactionTrace> transactionTraces) {
        this.transactionTraces = transactionTraces;
    }

    public void addToTransactionTraces(TransactionTrace transactionTrace) {
        List<TransactionTrace> trxTraces = getTransactionTraces();
        trxTraces.add(transactionTrace);
        setTransactionTraces(trxTraces);
    }

    public PROCESSING_STATUS getProcessingStatus() {
        return processingStatus;
    }

    public void setProcessingStatus(PROCESSING_STATUS processingStatus) {
        this.processingStatus = processingStatus;
    }
}
