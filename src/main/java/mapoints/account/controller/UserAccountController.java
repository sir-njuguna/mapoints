package mapoints.account.controller;


import mapoints.account.form.FetchLedgerForm;
import mapoints.account.service.AccountService;
import mapoints.account.service.LedgerService;
import mapoints.account.view.AccountView;
import mapoints.account.view.LedgerView;
import mapoints.lib.view.EntityApiResponse;
import mapoints.lib.view.PagedEntityApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


public abstract class UserAccountController {
    protected AccountService accountService;
    protected LedgerService ledgerService;


    @GetMapping("balance")
    public EntityApiResponse<AccountView> getBalance(Authentication auth){
        return new EntityApiResponse<>(
                accountService.getBalance(auth.getName())
        );
    }

    @GetMapping("ledger/fetch")
    public PagedEntityApiResponse<List<LedgerView>> fetchLedger(
            @RequestParam(value = "pageNum", required = false, defaultValue = "0") Integer pageNum,
            @RequestParam(value = "pageSize", required = false, defaultValue = "100") Integer pageSize,
            Authentication auth){

        FetchLedgerForm form = new FetchLedgerForm();
        form.setPageNum(pageNum);
        form.setPageSize(pageSize);
        form.setId(auth.getName());

        PagedEntityApiResponse<List<LedgerView>> response = ledgerService.fetchLedger(form);
        response.setStatus(true);
        response.setStatusCode(HttpStatus.OK.value());
        return response;
    }

    @Autowired
    void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    public void setLedgerService(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }
}
