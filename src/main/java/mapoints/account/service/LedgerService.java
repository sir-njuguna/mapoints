package mapoints.account.service;

import mapoints.account.form.FetchLedgerForm;
import mapoints.account.form.LedgerAction;
import mapoints.account.model.Account;
import mapoints.account.model.Ledger;
import mapoints.account.model.TransactionType;
import mapoints.account.repository.LedgerRepository;
import mapoints.account.view.LedgerView;
import mapoints.lib.exception.CommonRuntimeException;
import mapoints.lib.exception.ExceptionType;
import mapoints.lib.repository.BaseRepository;
import mapoints.lib.service.BaseService;
import mapoints.lib.view.PagedEntityApiResponse;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LedgerService extends BaseService<Ledger, LedgerRepository> {
    private final int TRANSACTION_CODE_LEN = 8;
    private AccountService accountService;
    public Ledger credit(LedgerAction form){

        List<TransactionType> allowedTypes = List.of(TransactionType.CREDIT_CASH, TransactionType.CREDIT_POINTS);
        if(!allowedTypes.contains(form.getTransactionType())){
            throw new CommonRuntimeException(ExceptionType.BAD_REQUEST, "auth.operation-not-allowed");
        }
        Account account = form.getAccount();
        BigDecimal balanceBefore = account.getBalance();
        BigDecimal balanceAfter = balanceBefore.add(form.getAmount());

        Ledger ledger = new Ledger();
        ledger.setUser(account.getUser());
        ledger.setAccount(account);
        ledger.setAmount(form.getAmount());
        ledger.setBalanceBefore(balanceBefore);
        ledger.setBalanceAfter(balanceAfter);
        ledger.setTransactionCode(form.getTransactionCode());
        ledger.setTransactionType(form.getTransactionType());
        ledger = save(ledger);

        account.setBalance(balanceAfter);
        accountService.save(account);

        return ledger;
    }

    public Ledger debit(LedgerAction form){

        List<TransactionType> allowedTypes = List.of(TransactionType.DEBIT_POINTS, TransactionType.DEBIT_CASH);
        if(!allowedTypes.contains(form.getTransactionType())){
            throw new CommonRuntimeException(ExceptionType.BAD_REQUEST, "auth.operation-not-allowed");
        }

        Account account = form.getAccount();
        BigDecimal amount = form.getAmount();
        if(amount.compareTo(BigDecimal.ZERO) > 0){
            amount = amount.negate();
        }

        BigDecimal balanceBefore = account.getBalance();
        BigDecimal balanceAfter = balanceBefore.add(amount);

        Ledger ledger = new Ledger();
        ledger.setUser(account.getUser());
        ledger.setAccount(account);
        ledger.setAmount(amount);
        ledger.setBalanceBefore(balanceBefore);
        ledger.setBalanceAfter(balanceAfter);
        ledger.setTransactionCode(form.getTransactionCode());
        ledger.setTransactionType(form.getTransactionType());
        ledger = save(ledger);

        account.setBalance(balanceAfter);
        accountService.save(account);

        return ledger;
    }

    public String generateTransactionCode(TransactionType transactionType){
        String prefix = "";
        switch (transactionType){
            case CREDIT_CASH:
                prefix = "CC";
                break;

            case DEBIT_CASH:
                prefix = "DC";
                break;
            case CREDIT_POINTS:
                prefix = "CP";
                break;
            case DEBIT_POINTS:
                prefix = "DB";
                break;
        }

        String transactionCode = prefix + "/" + RandomStringUtils.randomAlphanumeric(TRANSACTION_CODE_LEN);
        transactionCode = transactionCode.toUpperCase();
        if(repository.existsByTransactionCode(transactionCode)){
            transactionCode = generateTransactionCode(transactionType);
        }

        return transactionCode;
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
