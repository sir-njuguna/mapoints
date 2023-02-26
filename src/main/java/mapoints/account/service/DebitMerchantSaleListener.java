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
public class DebitMerchantSaleListener implements AfterSaleListener {
    private AccountService accountService;
    private LedgerService ledgerService;
    @Override
    public void doAfterSale(Sale sale) {
        Account merchantAccount = accountService.getAccountByUser(sale.getCustomer());

        LedgerAction debit = new LedgerAction();
        debit.setAccount(merchantAccount);
        debit.setAmount(sale.getPoints());
        debit.setPaymentChannel(PaymentChannel.INTERNAL);
        debit.setTransactionType(TransactionType.DEBIT_POINTS);
        debit.setTransactionCode(ledgerService.generateTransactionCode(TransactionType.DEBIT_POINTS));

        ledgerService.debit(debit);
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
