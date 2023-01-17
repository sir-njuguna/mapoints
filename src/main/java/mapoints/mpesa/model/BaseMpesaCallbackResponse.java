package mapoints.mpesa.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
abstract class BaseMpesaCallbackResponse  extends BaseMpesaResponse {

    @JsonProperty("ResultType")
    private String resultType;

    @JsonProperty("ResultCode")
    private Integer resultCode;

    @JsonProperty("ResultDesc")
    private String resultDesc;

    @JsonProperty("OriginatorConversationID")
    private String originatorConversationId;

    @JsonProperty("ConversationID")
    private String conversationId;

    @JsonProperty("TransactionID")
    private String transactionId;

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
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

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
