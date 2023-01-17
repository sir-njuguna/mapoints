package mapoints.mpesa.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This is the immediate response that we get from M-PESA when we request for a pay in transaction.
 */


public class MpesaSTKPushInitResponse extends BaseMpesaInitResponse {

    @JsonProperty("MerchantRequestID")
    private String merchantRequestId;

    @JsonProperty("CheckoutRequestID")
    private String checkOutRequestId;

    @JsonProperty("CustomerMessage")
    private String customerMessage;

    public String getMerchantRequestId() {
        return merchantRequestId;
    }

    public void setMerchantRequestId(String merchantRequestId) {
        this.merchantRequestId = merchantRequestId;
    }

    public String getCheckOutRequestId() {
        return checkOutRequestId;
    }

    public void setCheckOutRequestId(String checkOutRequestId) {
        this.checkOutRequestId = checkOutRequestId;
    }

    public String getCustomerMessage() {
        return customerMessage;
    }

    public void setCustomerMessage(String customerMessage) {
        this.customerMessage = customerMessage;
    }
}
