package mapoints.account.service;

import mapoints.account.form.FetchLedgerForm;
import mapoints.account.form.LedgerAction;
import mapoints.account.model.Account;
import mapoints.account.model.Ledger;
import mapoints.account.model.TransactionType;
import mapoints.account.repository.LedgerRepository;
import mapoints.account.view.LedgerView;
import mapoints.lib.repository.BaseRepository;
import mapoints.lib.service.BaseService;
import mapoints.lib.view.PagedEntityApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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


    public PagedEntityApiResponse<List<LedgerView>> fetchLedger(FetchLedgerForm form){
        Account account = accountService.getAccountByUser(form.getId());
        Page<Ledger> page = repository.findByAccount(
            account,
                BaseRepository.defaultPageable(form.getPageNum(), form.getPageSize())
        );

        List<LedgerView> views = page.stream().map(LedgerView::new).collect(Collectors.toList());
        return new PagedEntityApiResponse<>(page, views);
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}
