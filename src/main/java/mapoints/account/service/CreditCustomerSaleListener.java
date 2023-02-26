package mapoints.account.service;

import mapoints.account.form.LedgerAction;
import mapoints.account.model.Account;
import mapoints.account.model.TransactionType;
import mapoints.payment.model.PaymentChannel;
import mapoints.sale.model.Sale;
import mapoints.sale.service.AfterSaleListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreditCustomerSaleListener implements AfterSaleListener {
    private AccountService accountService;
    private LedgerService ledgerService;

    @Override
    public void doAfterSale(Sale sale) {
        Account customerAccount = accountService.getAccountByUser(sale.getCustomer());

        LedgerAction credit = new LedgerAction();
        credit.setAccount(customerAccount);
        credit.setAmount(sale.getPoints());
        credit.setPaymentChannel(PaymentChannel.INTERNAL);
        credit.setTransactionType(TransactionType.CREDIT_POINTS);
        credit.setTransactionCode(ledgerService.generateTransactionCode(TransactionType.CREDIT_POINTS));

        ledgerService.credit(credit);
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    public void setLedgerService(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }
}
