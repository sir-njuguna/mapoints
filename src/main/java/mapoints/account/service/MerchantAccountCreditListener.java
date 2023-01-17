package mapoints.account.service;

import mapoints.account.form.LedgerAction;
import mapoints.account.model.Account;
import mapoints.lib.service.FormatUtil;
import mapoints.payment.form.PostedPaymentForm;
import mapoints.payment.service.OnPostPaymentListener;
import mapoints.user.model.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;

@Service
public class MerchantAccountCreditListener implements OnPostPaymentListener {
    private AccountService accountService;


    @Nullable
    @Override
    public LedgerAction onReceiptOfPayment(PostedPaymentForm payment) {
        String phoneNumber = FormatUtil.internationalizePhoneNumber(payment.getAccountNumber(), "KE");
        Account account = accountService.getAccountByUser(phoneNumber, UserType.MERCHANT);

        LedgerAction ledgerAction = new LedgerAction();
        ledgerAction.setAccountEntityId(account.getEntityId());
        ledgerAction.setAmount(payment.getAmount());
        ledgerAction.setPaymentChannel(payment.getPaymentChannel());
        ledgerAction.setTransactionCode(payment.getTransactionCode());

        return ledgerAction;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}
