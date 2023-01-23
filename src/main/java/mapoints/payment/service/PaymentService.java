package mapoints.payment.service;


import mapoints.account.form.LedgerAction;
import mapoints.account.model.Account;
import mapoints.account.model.Ledger;
import mapoints.account.model.TransactionType;
import mapoints.account.service.AccountService;
import mapoints.account.service.LedgerService;
import mapoints.lib.exception.CommonRuntimeException;
import mapoints.lib.exception.ExceptionType;
import mapoints.lib.exception.NotImplementedException;
import mapoints.lib.service.FormatUtil;
import mapoints.lib.service.Message;
import mapoints.mpesa.service.MPesaPaymentImpl;
import mapoints.payment.form.PaymentForm;
import mapoints.payment.form.PostedPaymentForm;
import mapoints.payment.model.*;
import mapoints.payment.repository.PaymentTransactionRepository;
import mapoints.user.model.User;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.*;

@Service
public class PaymentService {
    private LedgerService ledgerService;


    private List<OnPostPaymentListener> onPostPaymentListeners;

    private PaymentTransactionRepository paymentTransactionRepository;

    private final Map<PaymentChannel, PaymentProvider> paymentMethodPaymentMap = new HashMap<>();


    public PaymentService(MPesaPaymentImpl mpesaPayment) {
        paymentMethodPaymentMap.put(PaymentChannel.MPESA, mpesaPayment);
    }


    public PaymentResponse initiatePayin(PaymentForm paymentForm){
        User merchant = paymentForm.getAccount().getUser();

        String accountNumber = merchant.getPhoneNumber();
        BigDecimal amount = FormatUtil.getRoundedUpValue(paymentForm.getAmount());

        PaymentProvider paymentProvider = getPaymentImplementation(accountNumber);
        PaymentResponse transactionStatus;
        try {
            transactionStatus = paymentProvider.initPayIn(accountNumber, amount);
        }catch (PaymentException e){
            e.printStackTrace();
            throw new CommonRuntimeException(
                ExceptionType.BAD_REQUEST,
                Message.get("mpesa.payin.initiate-failed")
            );
        }

        //Register a payin payment response
        PaymentTransaction paymentTransaction = new PaymentTransaction();
        paymentTransaction.setPaymentChannel(paymentForm.getPaymentChannel());
        paymentTransaction.setAmount(paymentForm.getAmount());
        paymentTransaction.setPaymentType(PaymentType.PAYIN);
        paymentTransaction.setTransactionId(transactionStatus.getTransactionId());
        paymentTransaction.setInitialResponse(transactionStatus);
        paymentTransaction.setAccount(paymentForm.getAccount());
        paymentTransactionRepository.save(paymentTransaction);

        return transactionStatus;
    }


    /**
     * Handles a pay-in callback
     * @param paymentChannel to handle this request
     * @param response {@link JSONObject} posted onn callback
     */
    public void handlePayinCallback(PaymentChannel paymentChannel, JSONObject response){
        PaymentProvider paymentProvider = getPaymentImplementation(paymentChannel);
        PaymentResponse transactionStatus = paymentProvider.onPayInCallback(response);

        PaymentTransaction paymentTransaction = findByTransactionIdAndPaymentChannel(
                transactionStatus.getTransactionId(),
                transactionStatus.getPaymentChannel()
        );

        if(paymentTransaction.getCallbackResponse() != null){
            throw new EntityExistsException();
        }

        paymentTransaction.setTransactionCode(transactionStatus.getTransactionCode());
        paymentTransaction.setCallbackResponse(transactionStatus);
        paymentTransactionRepository.save(paymentTransaction);

        //A payin transaction was successful, credit wallet customer's wallet
        if(transactionStatus.getStatus()){
            LedgerAction ledgerAction = new LedgerAction();
            ledgerAction.setAmount(paymentTransaction.getAmount());
            ledgerAction.setAccountEntityId(paymentTransaction.getAccount().getEntityId());
            ledgerAction.setTransactionCode(paymentTransaction.getTransactionCode());
            ledgerAction.setPaymentChannel(paymentChannel);
            ledgerAction.setTransactionType(TransactionType.CREDIT_CASH);

            Ledger ledger = ledgerService.credit(ledgerAction);
            paymentTransaction.setLedger(ledger);
            paymentTransactionRepository.save(paymentTransaction);
        }
    }

    public void handlePostPayment(final PostedPaymentForm postedPayment) throws EntityNotFoundException {
        LedgerAction ledgerAction = null;

        for (OnPostPaymentListener paymentHandler: onPostPaymentListeners){
            ledgerAction = paymentHandler.onReceiptOfPayment(postedPayment);
            if(ledgerAction != null){
                break;
            }
        }

        if(ledgerAction != null) {
            ledgerService.credit(ledgerAction);
        }
    }

    /**
     * Given a phone number, retrieve and return a payments implementation
     * @param phoneNumber for which to get a payments provider
     * @return {@link PaymentProvider}
     */
    private PaymentProvider getPaymentImplementation(String phoneNumber){
        return getPaymentImplementation(PaymentChannel.MPESA);
    }

    /**
     * Given a {@link PaymentChannel}, retrieve and return a {@link PaymentProvider} implementation
     * @param paymentChannel for which to get a payments provider
     * @return {@link PaymentProvider}
     */
    private PaymentProvider getPaymentImplementation(PaymentChannel paymentChannel){
        PaymentProvider paymentProvider = paymentMethodPaymentMap.get(paymentChannel);
        if(paymentProvider == null){
            throw new NotImplementedException();
        }
        return paymentProvider;
    }

    private PaymentTransaction findByTransactionIdAndPaymentChannel(String transactionId, PaymentChannel paymentChannel){
        Optional<PaymentTransaction> optional = paymentTransactionRepository.findByTransactionIdAndPaymentChannel(
                transactionId, paymentChannel);

        if(optional.isEmpty()){
            throw new EntityNotFoundException();
        }
        return optional.get();
    }


    @Autowired
    public void setLedgerService(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }


    @Autowired
    public void setPaymentTransactionRepository(PaymentTransactionRepository paymentTransactionRepository) {
        this.paymentTransactionRepository = paymentTransactionRepository;
    }

    @Autowired
    public void setOnPostPaymentListeners(List<OnPostPaymentListener> onPostPaymentListeners) {
        this.onPostPaymentListeners = onPostPaymentListeners;
    }

}
