package mapoints.mpesa.controller;


import mapoints.mpesa.service.MPesaC2BService;
import mapoints.payment.model.PaymentChannel;
import mapoints.payment.service.PaymentService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/saf")
public class MpesaController {
    private MPesaC2BService mpesaC2BService;
    private PaymentService paymentService;

    @PostMapping("/c2b-payment-callback")
    public void c2bPaymentReceiver(@RequestBody JSONObject request){
        Thread thread = new Thread(() ->mpesaC2BService.onC2BCallback(request));
        thread.start();
    }

    @PostMapping("/c2b-payment-allowed-checker")
    public void c2bPaymentAllowedChecker(@RequestBody JSONObject request){
        System.out.println("========Start of check ===========");
        System.out.println(request.toJSONString());
        System.out.println("========End of check ===========");
    }


    @PostMapping("/c2b-validation-callback")
    public void c2bValidationCallback(final @RequestBody JSONObject request){
        Thread thread = new Thread(() -> mpesaC2BService.onC2bValidationCallback(request));
        thread.start();
    }

    @PostMapping("/stk-payin-callback")
    public void stkPushCallbackHandler(@RequestBody JSONObject request){
        Thread thread = new Thread(() -> paymentService.handlePayinCallback(PaymentChannel.MPESA, request));
        thread.start();
    }



    @Autowired
    public void setMpesaC2BService(MPesaC2BService mpesaC2BService) {
        this.mpesaC2BService = mpesaC2BService;
    }

    @Autowired
    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
