package mapoints.account.controller;

import mapoints.account.form.WithdrawalForm;
import mapoints.account.model.Account;
import mapoints.airtime.form.SendAirtimeForm;
import mapoints.airtime.service.AirtimeService;
import mapoints.lib.service.AppConfig;
import mapoints.lib.service.Message;
import mapoints.lib.view.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Locale;

@RestController
@RequestMapping(AppConfig.CUSTOMER_BASE_URL+"/account")
public class CustomerAccountController extends UserAccountController{
    private AirtimeService airtimeService;
    @PostMapping("withdraw")
    public ApiResponse withdraw(@RequestBody @Valid WithdrawalForm form, Authentication auth, Locale locale) {
        Account account = accountService.getAccountByUser(auth.getName());
        accountService.checkBalance(account, form.getAmount());

        SendAirtimeForm airtimeForm = new SendAirtimeForm();
        airtimeForm.setAccount(account);
        airtimeForm.setAmount(form.getAmount());

        airtimeService.sendAirtime(airtimeForm);


        return new ApiResponse(
                true,
                HttpStatus.OK.value(),
                Message.get("account.withdrawal-success", locale)
        );
    }

    @Autowired
    public void setAirtimeService(AirtimeService airtimeService) {
        this.airtimeService = airtimeService;
    }
}
