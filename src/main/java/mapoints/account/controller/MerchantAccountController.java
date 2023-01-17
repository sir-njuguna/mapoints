package mapoints.account.controller;

import mapoints.account.form.DepositForm;
import mapoints.account.model.Account;
import mapoints.lib.service.AppConfig;
import mapoints.lib.service.Message;
import mapoints.lib.view.ApiResponse;
import mapoints.payment.form.PaymentForm;
import mapoints.payment.model.PaymentChannel;
import mapoints.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Locale;

@RestController
@RequestMapping(AppConfig.MERCHANT_BASE_URL+"/account")
public class MerchantAccountController extends UserAccountController {
    private PaymentService paymentService;

    @PostMapping("deposit")
    public ApiResponse deposit(@RequestBody @Valid DepositForm form, Authentication auth, Locale locale){
        Account account = accountService.getAccountByUser(auth.getName());

        PaymentForm paymentForm = new PaymentForm();
        paymentForm.setAccount(account);
        paymentForm.setAmount(form.getAmount());
        paymentForm.setPaymentChannel(PaymentChannel.MPESA);

        paymentService.initiatePayin(paymentForm);

        return new ApiResponse(
                true,
                HttpStatus.OK.value(),
                Message.get("mpesa.payin.initiate-success", locale)
        );
    }

    @Autowired
    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
