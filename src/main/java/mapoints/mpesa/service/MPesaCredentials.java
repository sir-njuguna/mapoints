package mapoints.mpesa.service;

import mapoints.mpesa.model.MpesaCredential;
import mapoints.mpesa.model.MpesaCredentialType;
import mapoints.payment.model.PaymentException;
import mapoints.payment.model.PaymentExceptionType;
import net.minidev.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Cipher;
import java.io.IOException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class MPesaCredentials {
    private final String MPESA_ACCESS_TOKEN_URL = "https://api.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials";
    private final String MPESA_CERTIFICATE_NAME = "keys/mpesa_prod_certificate.cer";
    private final String MPESA_ENCRYPTION_STANDARD = "RSA/ECB/PKCS1Padding";
    private final String MPESA_CERTIFICATE_TYPE = "X.509";

    private final Map<MpesaCredentialType, MpesaCredential> credentialsMap= new HashMap<>();


    String getMpesaAccessToken(String consumerKey,
                               String consumerSecret,
                               MpesaCredentialType credentialType) throws PaymentException {
        String accessToken;
        MpesaCredential credential = credentialsMap.get(credentialType);
        if(credential != null && !credential.isExpired()){
            accessToken = credential.getAccessToken();
        }else{
            accessToken = getMpesaAccessToken(consumerKey, consumerSecret);
            credentialsMap.put(credentialType, new MpesaCredential(credentialType, accessToken));
        }
        return accessToken;
    }

    private String getMpesaAccessToken(String consumerKey,
                                       String consumerSecret) throws PaymentException {
        String usernamePassword = consumerKey+":"+consumerSecret;
        String encodedPassword = new String(Base64.getEncoder().encode(usernamePassword.getBytes()));

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set(HttpHeaders.AUTHORIZATION, "Basic "+encodedPassword);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        String accessToken;
        JSONObject responseObj;
        try {
            ResponseEntity<JSONObject> response = restTemplate.exchange(
                    MPESA_ACCESS_TOKEN_URL,
                    HttpMethod.GET,
                    httpEntity,
                    JSONObject.class
            );
            responseObj = response.getBody();
        }catch (Exception e){
            throw new PaymentException(e, PaymentExceptionType.FAILED_CREDENTIALS_GENERATION);
        }

        if(responseObj!= null) {
            accessToken = responseObj.getAsString("access_token");
        }else{
            throw new PaymentException(
                    new NullPointerException("responseObj.getAsString returned a null object"),
                    PaymentExceptionType.FAILED_CREDENTIALS_GENERATION
            );
        }
        return accessToken;
    }

    byte[] generateEncryptedPassword(String initiatorPassword){
        byte[] encryptedPassword = null;
        try{
            PublicKey publicKey = readPublicKey();
            Cipher cipher = Cipher.getInstance(MPESA_ENCRYPTION_STANDARD);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            encryptedPassword = cipher.doFinal(initiatorPassword.getBytes());
        } catch (Exception e){
            e.printStackTrace();
        }
        return Base64.getEncoder().encode(encryptedPassword);
    }

    private PublicKey readPublicKey() throws IOException,
            CertificateException {
        Resource resource = new ClassPathResource(MPESA_CERTIFICATE_NAME);
        CertificateFactory f = CertificateFactory.getInstance(MPESA_CERTIFICATE_TYPE);
        X509Certificate certificate = (X509Certificate)f.generateCertificate(resource.getInputStream());
        return certificate.getPublicKey();
    }
}
