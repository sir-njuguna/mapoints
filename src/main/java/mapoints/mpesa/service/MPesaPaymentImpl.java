package mapoints.mpesa.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import mapoints.mpesa.model.*;
import mapoints.mpesa.repository.MPesaTrxCallbackValidationRepository;
import mapoints.mpesa.repository.MPesaTrxInitValidationRepository;
import mapoints.mpesa.view.MpesaSTKPushInitRequest;
import mapoints.mpesa.view.MpesaTransactionValidationRequest;
import mapoints.payment.model.PaymentChannel;
import mapoints.payment.model.PaymentException;
import mapoints.payment.model.PaymentResponse;
import mapoints.payment.model.TransactionTrace;
import mapoints.payment.service.PaymentProvider;
import net.minidev.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MPesaPaymentImpl implements PaymentProvider {
    private MPesaCredentials mpesaCredentials;
    private final ObjectMapper objectMapper;

    private MPesaTrxInitValidationRepository trxInitValidationRepository;
    private MPesaTrxCallbackValidationRepository trxCallbackValidationRepository;

    private final String MPESA_C2B_INITIATE_PAYIN_URL = "https://api.safaricom.co.ke/mpesa/stkpush/v1/processrequest";
    private final String MPESA_TRX_VALIDATION_URL = "https://api.safaricom.co.ke/mpesa/transactionstatus/v1/query";

    private String mpesaC2BShortCode;
    private String mpesaStkPushConsumerKey;
    private String mpesaStkPushConsumerSecret;
    private String mpesaStkPushpassKey;
    private String mpesaStkPushCustomerPayBillOnlineName;
    private String mpesaStkPushCallbackUrl;
    private String mpesaC2BValidationConsumerKey;
    private String mpesaC2BValidationConsumerSecret;
    private String mpesaC2BValidationInitiatorName;
    private String mpesaC2BValidationSecurityPassword;
    private int mpesaC2BValidationIdentifierType;
    private String mpesaC2BValidationCallbackUrl;
    private String mpesaC2BValidationCommandId;



    public MPesaPaymentImpl(){
        objectMapper = new ObjectMapper();
    }


    /**
     * Initializes requests to receive payments by call M-PESA STK push API
     * @param phoneNumber account number to be debited. This is a phone number
     * @param amount to be paid
     * @return {@link PaymentResponse}
     */
    @Override
    public PaymentResponse initPayIn(String phoneNumber, BigDecimal amount) throws PaymentException {


        String accessToken = mpesaCredentials.getMpesaAccessToken(
                mpesaStkPushConsumerKey,
                mpesaStkPushConsumerSecret,
                MpesaCredentialType.M_PESA_C2B_PAYIN
        );

        String currentTimestamp = generateTimestamp();
        String password = generateBase64EncodeMpesaPassword(currentTimestamp);


        MpesaSTKPushInitRequest request = new MpesaSTKPushInitRequest();
        request.setBusinessShortCode(mpesaC2BShortCode);
        request.setPassword(password);
        request.setTimestamp(currentTimestamp);
        request.setTransactionType(mpesaStkPushCustomerPayBillOnlineName);
        request.setAmount(amount);

        phoneNumber = getRecipientPhoneNumber(phoneNumber);
        String accountReference = MPesaC2BService.STK_PUSH_NAME+"-"+phoneNumber;

        request.setPartyA(phoneNumber);
        request.setPartyB(mpesaC2BShortCode);
        request.setPhoneNumber(phoneNumber);

        request.setCallBackURL(mpesaStkPushCallbackUrl);
        request.setAccountReference(accountReference);
        request.setTransactionDesc("Request payment from "+phoneNumber);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        HttpEntity<MpesaSTKPushInitRequest> httpEntity = new HttpEntity<>(request, headers);

        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setPaymentChannel(PaymentChannel.MPESA);
        try{
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                MPESA_C2B_INITIATE_PAYIN_URL,
                HttpMethod.POST,
                httpEntity,
                String.class
            );
            String dump = responseEntity.getBody();

            paymentResponse.setResponseObj(objectMapper.readValue(dump, JSONObject.class));

            MpesaSTKPushInitResponse initResponse = objectMapper.readValue(dump, MpesaSTKPushInitResponse.class);
            paymentResponse.setTransactionId(initResponse.getCheckOutRequestId());
            paymentResponse.setStatus(initResponse.getResponseCode() != null && initResponse.getResponseCode() == 0);
            paymentResponse.setMessage(initResponse.getCustomerMessage());
        }catch (Exception e){
            e.printStackTrace();
            paymentResponse.setStatus(false);
            paymentResponse.setMessage(e.getMessage());
        }
        return paymentResponse;
    }

    /**
     * Handles payin callback response from M-PESA STK Push API
     * @param response a {@link JSONObject} posted back by the payments service after a payin has been donne
     * @return {@link PaymentResponse}
     */
    @Override
    public PaymentResponse onPayInCallback(JSONObject response) {
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setPaymentChannel(PaymentChannel.MPESA);
        paymentResponse.setResponseObj(response);

        MpesaSTKPushCallback mpesaSTKPushCallback = new MpesaSTKPushCallback();
        LinkedHashMap<String, Object> body = (LinkedHashMap<String, Object>) response.get("Body");
        LinkedHashMap<String, Object> stkCallback = (LinkedHashMap<String, Object>) body.get("stkCallback");
        Integer resultCode = (Integer) stkCallback.get("ResultCode");
        mpesaSTKPushCallback.setResultCode(resultCode);
        mpesaSTKPushCallback.setResultDesc((String) stkCallback.get("ResultDesc"));
        mpesaSTKPushCallback.setCheckOutRequestId((String) stkCallback.get("CheckoutRequestID"));
        mpesaSTKPushCallback.setMerchantRequestId((String)  stkCallback.get("MerchantRequestID"));

        paymentResponse.setTransactionId(mpesaSTKPushCallback.getCheckOutRequestId());
        paymentResponse.setMessage(mpesaSTKPushCallback.getResultDesc());

        if(resultCode != null && resultCode == 0){
            paymentResponse.setStatus(true);

            LinkedHashMap<String, Object> callbackMetadata = (LinkedHashMap<String, Object>) stkCallback.get("CallbackMetadata");
            List<Object> items = (List<Object>) callbackMetadata.get("Item");

            for(Object item : items){
                HashMap<String, Object> obj = (HashMap<String, Object>)item;
                String name = (String) obj.get("Name");
                Object value = obj.get("Value");

                if(name.equalsIgnoreCase("Amount")) {

                    try {
                        mpesaSTKPushCallback.setAmount(BigDecimal.valueOf((double) value));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }

                if(name.equalsIgnoreCase("MpesaReceiptNumber")){
                    mpesaSTKPushCallback.setMpesaReceiptNumber(value.toString());
                }

                if(name.equalsIgnoreCase("PhoneNumber")){
                    mpesaSTKPushCallback.setPhoneNumber(value.toString());
                }
            }

            paymentResponse.setTransactionCode(
                PaymentChannel.MPESA+"/"+mpesaSTKPushCallback.getMpesaReceiptNumber()
            );

        } else {
            paymentResponse.setStatus(false);
        }
        return paymentResponse;
    }


    /**
     * Initiate a request to validate an M-PESA C2B transaction
     * @param c2BCallback {@link MpesaC2BCallback}
     */
    void initiateTransactionValidationRequest(MpesaC2BCallback c2BCallback) throws PaymentException{

        MpesaTransactionValidationRequest request = new MpesaTransactionValidationRequest();
        request.setInitiator(mpesaC2BValidationInitiatorName);

        byte[] securityCredential = mpesaCredentials.generateEncryptedPassword(
               mpesaC2BValidationSecurityPassword
        );

        request.setSecurityCredential(new String(securityCredential));
        request.setCommandId(mpesaC2BValidationCommandId);
        request.setTransactionId(c2BCallback.getTransactionCode());
        request.setPartyA(mpesaC2BShortCode);
        request.setIdentifierType(mpesaC2BValidationIdentifierType);
        request.setResultsUrl(mpesaC2BValidationCallbackUrl);
        request.setQueueTimeoutUrl(mpesaC2BValidationCallbackUrl);
        request.setRemarks("Validate payment request");
        request.setOccasion(c2BCallback.getValidationOccasion());

        String accessToken = mpesaCredentials.getMpesaAccessToken(
                mpesaC2BValidationConsumerKey,
                mpesaC2BValidationConsumerSecret,
                MpesaCredentialType.M_PESA_C2B_VALIDATION
        );

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        HttpEntity<MpesaTransactionValidationRequest> httpEntity = new HttpEntity<>(request, headers);

        MpesaTrxValidationInitResponse initResponse = new MpesaTrxValidationInitResponse();

        String responseDump = null;
        try {
            initResponse.addToTransactionTraces(new TransactionTrace(TransactionTrace.TRACE_NAME.BEFORE_ENDPOINT_CALL));
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    MPESA_TRX_VALIDATION_URL,
                    HttpMethod.POST,
                    httpEntity,
                    String.class
            );
            responseDump = responseEntity.getBody();
            initResponse.addToTransactionTraces(new TransactionTrace(TransactionTrace.TRACE_NAME.ON_ENDPOINT_CALL_SUCCESS));
        }catch (Exception e){
            e.printStackTrace();
        }

        if(responseDump != null){
            initResponse.setPayloadDump(responseDump);
            try {
                List<TransactionTrace> prevTraces =  new ArrayList<>(initResponse.getTransactionTraces());
                initResponse = objectMapper.readValue(responseDump, MpesaTrxValidationInitResponse.class);
                initResponse.setTransactionTraces(prevTraces);
                initResponse.addToTransactionTraces(
                        new TransactionTrace(TransactionTrace.TRACE_NAME.ON_RESPONSE_SERIALIZATION_SUCCESS)
                );
                initResponse.setProcessingStatus(MpesaTrxValidationInitResponse.PROCESSING_STATUS.PENDING);
            }catch (Exception e){
                initResponse.addToTransactionTraces(
                        new TransactionTrace(TransactionTrace.TRACE_NAME.ON_RESPONSE_SERIALIZATION_FAILURE)
                );
            }
        }

        initResponse.setTransactionId(c2BCallback.getTransactionCode());
        initResponse.setUserPhoneNumber(c2BCallback.getPhoneNumber());
        initResponse.setAmount(c2BCallback.getTransactionAmount());
        initResponse.setOccasion(request.getOccasion());

        trxInitValidationRepository.save(initResponse);
    }

    /**
     * Handles M-PESA C2B transaction validation
     * @param obj a {@link JSONObject} posted to by M-PESA service
     * @return a {@link MpesaTrxValidationCallbackResponse}
     */
    public MpesaTrxValidationCallbackResponse handleTransactionValidationCallback(JSONObject obj) {
        MpesaTrxValidationCallbackResponse callbackResponse = null;
        try{
            LinkedHashMap<String, Object> result = (LinkedHashMap<String, Object>) obj.get("Result");
            String jsonStr = objectMapper.writeValueAsString(result);
            callbackResponse = objectMapper.readValue(jsonStr, MpesaTrxValidationCallbackResponse.class);

            callbackResponse.setPayloadDump(obj);
            callbackResponse = trxCallbackValidationRepository.save(callbackResponse);

            LinkedHashMap<String, Object>  referenceData =
                    (LinkedHashMap<String, Object>) result.get("ReferenceData");
            LinkedHashMap<String, Object> referenceItem =
                    (LinkedHashMap<String, Object>) referenceData.get("ReferenceItem");
            String occasionKey = (String)referenceItem.get("Key");
            if(StringUtils.equalsIgnoreCase(occasionKey, "Occasion")){
                String occasionRef = (String)referenceItem.get("Value");
                callbackResponse.setOccasionReference(occasionRef);
            }

            Integer resultCode = callbackResponse.getResultCode();
            if(resultCode != null && resultCode == 0){
                LinkedHashMap<String, Object>  resultsParams =
                        (LinkedHashMap<String, Object>) result.get("ResultParameters");
                List<Object> items = (List<Object>) resultsParams.get("ResultParameter");
                for(Object item : items){
                    HashMap<String, Object> objMap = (HashMap)item;
                    String key = (String) objMap.get("Key");
                    Object value = objMap.get("Value");

                    if(key.equalsIgnoreCase("Amount")) {
                        try {
                            callbackResponse.setAmount(new BigDecimal(value.toString()));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }

                    if(key.equals("DebitPartyName")){
                        callbackResponse.setDebitParty((String)value);
                    }

                    if(key.equals("CreditPartyName")){
                        callbackResponse.setCreditParty((String)value);
                    }

                    if(key.equals("ReceiptNo")){
                        callbackResponse.setMpesaReceiptNumber((String)value);
                    }
                }
            }
            trxCallbackValidationRepository.save(callbackResponse);
        }catch (IOException e){
            e.printStackTrace();
        }
        return callbackResponse;
    }


    /**
     * Strips the '+' symbol from the phone number
     * @param phoneNumber to be formatted
     * @return formatted phone number
     */
    private String getRecipientPhoneNumber(String phoneNumber) {
        if(phoneNumber.charAt(0) == '+'){
            phoneNumber = phoneNumber.substring(1);
        }
        return phoneNumber;
    }


    /**
     * Generates and returns a timestamp string of the format yyyyMMddHHmmss
     * @return current timestamp formatted to M-PESA's specification
     */
    private String generateTimestamp(){
        return  new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    /**
     * This method generates a base 64 encoded password
     * The raw password is first generated by concatenating mpesaC2BShortcode, mpesaStkPushPasskey and currentTimestamp
     * This raw password is then encoded into a base 64 byte[] then converted to a string
     * @param currentTimestamp current timestamp formatted to  M-PESA's specifications
     * @return the base 64 encoded password
     */
    private String generateBase64EncodeMpesaPassword(String currentTimestamp){

        String rawPassword = mpesaC2BShortCode
                            + mpesaStkPushpassKey
                            + currentTimestamp;

        byte[] encodedPassword = Base64.getEncoder().encode(rawPassword.getBytes());
        return new String(encodedPassword);
    }


    @Override
    public PaymentChannel getPaymentChannel() {
        return PaymentChannel.MPESA;
    }

    @Autowired
    public void setMpesaCredentials(MPesaCredentials mpesaCredentials) {
        this.mpesaCredentials = mpesaCredentials;
    }


    @Autowired
    public void setTrxInitValidationRepository(MPesaTrxInitValidationRepository trxInitValidationRepository) {
        this.trxInitValidationRepository = trxInitValidationRepository;
    }

    @Autowired
    public void setTrxCallbackValidationRepository(MPesaTrxCallbackValidationRepository trxCallbackValidationRepository) {
        this.trxCallbackValidationRepository = trxCallbackValidationRepository;
    }


    @Value("${mpesa.c2b.short-code}")
    public void setMpesaC2BShortCode(String mpesaC2BShortCode) {
        this.mpesaC2BShortCode = mpesaC2BShortCode;
    }

    @Value("${mpesa.stk.push.consumer-key}")
    public void setMpesaStkPushConsumerKey(String mpesaStkPushConsumerKey) {
        this.mpesaStkPushConsumerKey = mpesaStkPushConsumerKey;
    }

    @Value("${mpesa.stk.push.consumer-secret}")
    public void setMpesaStkPushConsumerSecret(String mpesaStkPushConsumerSecret) {
        this.mpesaStkPushConsumerSecret = mpesaStkPushConsumerSecret;
    }

    @Value("${mpesa.stk.push.pass-key}")
    public void setMpesaStkPushpassKey(String mpesaStkPushpassKey) {
        this.mpesaStkPushpassKey = mpesaStkPushpassKey;
    }


    @Value("${mpesa.stk.push.customer-pay-bill-online-name}")
    public void setMpesaStkPushCustomerPayBillOnlineName(String mpesaStkPushCustomerPayBillOnlineName) {
        this.mpesaStkPushCustomerPayBillOnlineName = mpesaStkPushCustomerPayBillOnlineName;
    }

    @Value("${mpesa.stk.push.callback-url}")
    public void setMpesaStkPushCallbackUrl(String mpesaStkPushCallbackUrl) {
        this.mpesaStkPushCallbackUrl = mpesaStkPushCallbackUrl;
    }

    @Value("${mpesa.c2b.validation-consumer-key}")
    public void setMpesaC2BValidationConsumerKey(String mpesaC2BValidationConsumerKey) {
        this.mpesaC2BValidationConsumerKey = mpesaC2BValidationConsumerKey;
    }

    @Value("${mpesa.c2b.validation-consumer-secret}")
    public void setMpesaC2BValidationConsumerSecret(String mpesaC2BValidationConsumerSecret) {
        this.mpesaC2BValidationConsumerSecret = mpesaC2BValidationConsumerSecret;
    }

    @Value("${mpesa.c2b.validation-initiator-name}")
    public void setMpesaC2BValidationInitiatorName(String mpesaC2BValidationInitiatorName) {
        this.mpesaC2BValidationInitiatorName = mpesaC2BValidationInitiatorName;
    }

    @Value("${mpesa.c2b.validation-security-password}")
    public void setMpesaC2BValidationSecurityPassword(String mpesaC2BValidationSecurityPassword) {
        this.mpesaC2BValidationSecurityPassword = mpesaC2BValidationSecurityPassword;
    }


    @Value("${mpesa.c2b.validation-identifier-type}")
    public void setMpesaC2BValidationIdentifierType(int mpesaC2BValidationIdentifierType) {
        this.mpesaC2BValidationIdentifierType = mpesaC2BValidationIdentifierType;
    }

    @Value("${mpesa.c2b.validation-callback-url}")
    public void setMpesaC2BValidationCallbackUrl(String mpesaC2BValidationCallbackUrl) {
        this.mpesaC2BValidationCallbackUrl = mpesaC2BValidationCallbackUrl;
    }

    @Value("${mpesa.c2b.validation-command-id}")
    public void setMpesaC2BValidationCommandId(String mpesaC2BValidationCommandId) {
        this.mpesaC2BValidationCommandId = mpesaC2BValidationCommandId;
    }
}
