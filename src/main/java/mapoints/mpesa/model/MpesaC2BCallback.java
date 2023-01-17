package mapoints.mpesa.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import mapoints.lib.model.BaseModel;
import mapoints.payment.model.TransactionVerificationStatus;
import mapoints.user.model.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
public class MpesaC2BCallback extends BaseModel {

    @JsonProperty("TransactionType")
    private String transactionType;

    @NonNull
    @JsonProperty("BillRefNumber")
    private String billRefNumber;

    @JsonProperty("MSISDN")
    private String phoneNumber;

    @JsonProperty("FirstName")
    private String firstName;

    @JsonProperty("MiddleName")
    private String middleName;

    @JsonProperty("LastName")
    private String lastName;

    @JsonProperty("BusinessShortCode")
    private String businessShortCode;

    @JsonProperty("OrgAccountBalance")
    private BigDecimal orgAccountBalance;

    @JsonProperty("TransAmount")
    private BigDecimal transactionAmount;

    @JsonProperty("TransID")
    private String transactionCode;

    private String validationOccasion;

    @OneToOne
    @JoinColumn(name = "validationResponse")
    private MpesaTrxValidationCallbackResponse validationResponse;

    @Enumerated(EnumType.STRING)
    private TransactionVerificationStatus verificationStatus = TransactionVerificationStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "revalidatedBy")
    private User revalidatedBy;

    @Lob
    private String revalidationReason;

    public MpesaC2BCallback() { }

    public MpesaC2BCallback(MpesaC2BCallback c2bResponse) {
        setTransactionType(c2bResponse.getTransactionType());
        setBillRefNumber(c2bResponse.getBillRefNumber());
        setPhoneNumber(c2bResponse.getPhoneNumber());
        setFirstName(c2bResponse.getFirstName());
        setMiddleName(c2bResponse.getMiddleName());
        setLastName(c2bResponse.getLastName());
        setBusinessShortCode(c2bResponse.getBusinessShortCode());
        setOrgAccountBalance(c2bResponse.getOrgAccountBalance());
        setTransactionAmount(c2bResponse.getTransactionAmount());
        setTransactionCode(c2bResponse.getTransactionCode());
    }


    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getBillRefNumber() {
        return StringUtils.normalizeSpace(billRefNumber).toLowerCase();
    }

    public void setBillRefNumber(@NonNull String billRefNumber) {
        this.billRefNumber = billRefNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName == null ? "" : firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName == null ? "" : middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName == null ? "" : lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getFullName(){
        return String.format("%s %s %s", getFirstName(), getMiddleName(), getLastName());
    }
    public String getBusinessShortCode() {
        return businessShortCode;
    }

    public void setBusinessShortCode(String businessShortCode) {
        this.businessShortCode = businessShortCode;
    }

    public BigDecimal getOrgAccountBalance() {
        return orgAccountBalance;
    }

    public void setOrgAccountBalance(BigDecimal orgAccountBalance) {
        this.orgAccountBalance = orgAccountBalance;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public TransactionVerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(TransactionVerificationStatus verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public String getValidationOccasion() {
        return validationOccasion;
    }

    public void setValidationOccasion(String validationOccasion) {
        this.validationOccasion = validationOccasion;
    }

    public MpesaTrxValidationCallbackResponse getValidationResponse() {
        return validationResponse;
    }

    public void setValidationResponse(MpesaTrxValidationCallbackResponse validationResponse) {
        this.validationResponse = validationResponse;
    }

    public User getRevalidatedBy() {
        return revalidatedBy;
    }

    public void setRevalidatedBy(User revalidatedBy) {
        this.revalidatedBy = revalidatedBy;
    }

    public String getRevalidationReason() {
        return revalidationReason;
    }

    public void setRevalidationReason(String revalidationReason) {
        this.revalidationReason = revalidationReason;
    }
}
