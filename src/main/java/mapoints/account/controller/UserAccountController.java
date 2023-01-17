package mapoints.account.controller;


import mapoints.account.service.AccountService;
import mapoints.account.view.AccountView;
import mapoints.lib.view.EntityApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;



public abstract class UserAccountController {
    protected AccountService accountService;


    @GetMapping("balance")
    public EntityApiResponse<AccountView> getBalance(Authentication auth){
        return new EntityApiResponse<>(
                accountService.getBalance(auth.getName())
        );
    }

    @Autowired
    void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}
