package mapoints.mpesa.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import mapoints.lib.service.BaseService;
import mapoints.mpesa.model.MpesaC2BCallback;
import mapoints.mpesa.model.MpesaTrxValidationCallbackResponse;
import mapoints.mpesa.repository.MpesaC2BCallbackRepository;
import mapoints.payment.form.PostedPaymentForm;
import mapoints.payment.model.PaymentChannel;
import mapoints.payment.model.PaymentException;
import mapoints.payment.model.TransactionVerificationStatus;
import mapoints.payment.service.PaymentService;
import net.minidev.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.*;

@Service
public class MPesaC2BService extends BaseService<MpesaC2BCallback, MpesaC2BCallbackRepository> {
    final static String STK_PUSH_NAME = "STK";
    private MPesaPaymentImpl mPesaPayment;
    private PaymentService paymentService;

    /**
     * Handles M-PESA C2B callback requests
     * If similar transaction exists and is verified drop request
     * If this request was initialized by this application via M-PESA stk push, log the request as verified
     *  else log the request as pending verification and initialize verification
     * @param request a {@link JSONObject} posted by M-PESA
     */
    public void onC2BCallback(JSONObject request){
        String responseDump = request.toJSONString();
        MpesaC2BCallback response;

        try{
            ObjectMapper om = new ObjectMapper();
            response = om.readValue(responseDump, MpesaC2BCallback.class);
            List<TransactionVerificationStatus> statuses = new ArrayList<>(Arrays.asList(
                    TransactionVerificationStatus.PENDING,
                    TransactionVerificationStatus.VERIFIED
                )
            );

            if(repository.existsByTransactionCodeAndVerificationStatusIn(
                    response.getTransactionCode(),
                    statuses)){
                throw new EntityExistsException("C2B transaction already exists");
            }

            String accountRef = response.getBillRefNumber();
            if(StringUtils.startsWithIgnoreCase(accountRef, STK_PUSH_NAME)) {
                response.setVerificationStatus(TransactionVerificationStatus.VERIFIED);
            }else{
                initC2BValidation(response);
            }
            save(response);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void initC2BValidation(MpesaC2BCallback c2BCallback){
        c2BCallback.setValidationOccasion(UUID.randomUUID().toString());
        c2BCallback = save(c2BCallback);
        try {
            mPesaPayment.initiateTransactionValidationRequest(c2BCallback);
        }catch (PaymentException e){
            e.printStackTrace();
        }
    }

    public void onC2bValidationCallback(JSONObject request){
        MpesaTrxValidationCallbackResponse validationResponse =
                mPesaPayment.handleTransactionValidationCallback(request);

        Optional<MpesaC2BCallback> optional = repository.findByValidationOccasion(
                validationResponse.getOccasionReference()
        );
        if(optional.isEmpty()){
            throw new EntityNotFoundException("M-PESA C2B trx not found");
        }
        MpesaC2BCallback c2BCallback = optional.get();
        c2BCallback.setValidationResponse(validationResponse);
        c2BCallback = save(c2BCallback);

        //Successful
        if(validationResponse.getResultCode().equals(0)){
            c2BCallback.setVerificationStatus(TransactionVerificationStatus.VERIFIED);
            c2BCallback.setTransactionAmount(validationResponse.getAmount());
            c2BCallback = save(c2BCallback);

            PostedPaymentForm payment  = new PostedPaymentForm();

            payment.setAccountNumber(c2BCallback.getBillRefNumber());
            payment.setAmount(validationResponse.getAmount());

            payment.setTransactionCode(PaymentChannel.MPESA+"/"+c2BCallback.getTransactionCode());
            try {
                paymentService.handlePostPayment(payment);
            }catch (EntityNotFoundException ignore){}
        }else{
            c2BCallback.setVerificationStatus(TransactionVerificationStatus.FAILED);
        }
        save(c2BCallback);
    }


    @Autowired
    public void setmPesaPayment(MPesaPaymentImpl mPesaPayment) {
        this.mPesaPayment = mPesaPayment;
    }

    @Autowired
    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
