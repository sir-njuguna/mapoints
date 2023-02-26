package mapoints.airtime.service;

import com.africastalking.AfricasTalking;
import com.africastalking.Callback;
import com.africastalking.airtime.AirtimeResponse;
import mapoints.account.form.LedgerAction;
import mapoints.account.model.Account;
import mapoints.account.model.TransactionType;
import mapoints.account.service.LedgerService;
import mapoints.airtime.form.SendAirtimeForm;
import mapoints.payment.model.PaymentChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AirtimeService {
    private LedgerService ledgerService;
    private String africasTalkingUsername;
    private String africasTalkingApiKey;

    public void sendAirtime(final SendAirtimeForm form){
        AfricasTalking.initialize(africasTalkingUsername, africasTalkingApiKey);
        com.africastalking.AirtimeService airtimeService = AfricasTalking.getService(AfricasTalking.SERVICE_AIRTIME);

        Callback<AirtimeResponse> responseCallback = new Callback<>() {
            @Override
            public void onSuccess(AirtimeResponse response) {
                if (response.numSent > 0) {
                    response.responses.forEach(entry -> debitAccount(form.getAccount(), new BigDecimal(entry.amount)));
                }else {
                    System.out.println(response.errorMessage);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }
        };

        airtimeService.send(
                form.getAccount().getUser().getPhoneNumber(),
                form.getCurrencyCode(),
                form.getAmount().floatValue(),
                responseCallback
        );
    }


    private void debitAccount(Account account, BigDecimal amount){
        TransactionType transactionType = TransactionType.DEBIT_POINTS;
        LedgerAction form = new LedgerAction();

        form.setAccount(account);
        form.setAmount(amount);
        form.setTransactionCode(ledgerService.generateTransactionCode(transactionType));
        form.setPaymentChannel(PaymentChannel.AFRICAS_TALKING_AIRTIME);
        form.setTransactionType(transactionType);

        ledgerService.debit(form);
    }

    @Autowired
    public void setLedgerService(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    @Value("${africastalking.username}")
    public void setAfricasTalkingUsername(String africasTalkingUsername) {
        this.africasTalkingUsername = africasTalkingUsername;
    }

    @Value("${africastalking.api-key}")
    public void setAfricasTalkingApiKey(String africasTalkingApiKey) {
        this.africasTalkingApiKey = africasTalkingApiKey;
    }

}
