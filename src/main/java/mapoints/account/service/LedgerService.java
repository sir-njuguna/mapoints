package mapoints.account.service;

import mapoints.account.form.LedgerAction;
import mapoints.account.model.Account;
import mapoints.account.model.Ledger;
import mapoints.account.model.TransactionType;
import mapoints.account.repository.LedgerRepository;
import mapoints.lib.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class LedgerService extends BaseService<Ledger, LedgerRepository> {
    private AccountService accountService;
    public Ledger credit(LedgerAction form){
        Account account = accountService.findByEntityId(form.getAccountEntityId());
        BigDecimal balanceBefore = account.getBalance();
        BigDecimal balanceAfter = balanceBefore.add(form.getAmount());

        Ledger ledger = new Ledger();
        ledger.setUser(account.getUser());
        ledger.setAccount(account);
        ledger.setAmount(form.getAmount());
        ledger.setBalanceBefore(balanceBefore);
        ledger.setBalanceAfter(balanceAfter);
        ledger.setTransactionCode(form.getTransactionCode());
        ledger.setTransactionType(TransactionType.CREDIT_CASH);
        ledger = save(ledger);

        account.setBalance(balanceAfter);
        accountService.save(account);

        return ledger;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}
