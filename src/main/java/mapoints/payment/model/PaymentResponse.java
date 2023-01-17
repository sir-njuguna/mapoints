package mapoints.payment.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.minidev.json.JSONObject;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentResponse {
    private boolean status;
    private String transactionId;
    private String message;
    private JSONObject responseObj;
    private ResponseType responseType;
    private PaymentChannel paymentChannel;
    private String transactionCode;
    private String paymentRecipient;

    private Date responseTime = new Date();

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JSONObject getResponseObj() {
        return responseObj;
    }

    public void setResponseObj(JSONObject responseObj) {
        this.responseObj = responseObj;
    }

    public ResponseType getResponseType() {
        return responseType;
    }

    public void setResponseType(ResponseType responseType) {
        this.responseType = responseType;
    }

    public PaymentChannel getPaymentChannel() {
        return paymentChannel;
    }

    public void setPaymentChannel(PaymentChannel paymentChannel) {
        this.paymentChannel = paymentChannel;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public Date getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Date responseTime) {
        this.responseTime = responseTime;
    }

    public String getPaymentRecipient() {
        return paymentRecipient;
    }

    public void setPaymentRecipient(String paymentRecipient) {
        this.paymentRecipient = paymentRecipient;
    }
}
