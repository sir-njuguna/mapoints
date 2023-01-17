package mapoints.mpesa.model;

import org.apache.commons.lang.time.DateUtils;

import java.util.Date;

public class MpesaCredential {
    private static final int expiryOffsetInSeconds = 3000;

    private MpesaCredentialType credentialType;
    private String accessToken;
    private Date timeLastGenerated = new Date();

    public MpesaCredential(MpesaCredentialType credentialType, String accessToken) {
        this.credentialType = credentialType;
        this.accessToken = accessToken;
    }

    public MpesaCredentialType getCredentialType() {
        return credentialType;
    }

    public void setCredentialType(MpesaCredentialType credentialType) {
        this.credentialType = credentialType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Date getTimeLastGenerated() {
        return timeLastGenerated;
    }

    public void setTimeLastGenerated(Date timeLastGenerated) {
        this.timeLastGenerated = timeLastGenerated;
    }

    public boolean isExpired(){
        return DateUtils.addSeconds(new Date(), -expiryOffsetInSeconds).after(timeLastGenerated);
    }
}
