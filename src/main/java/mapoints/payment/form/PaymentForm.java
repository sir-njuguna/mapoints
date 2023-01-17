package mapoints.payment.form;

import lombok.Getter;
import lombok.Setter;
import mapoints.account.model.Account;
import mapoints.payment.model.PaymentChannel;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class PaymentForm {
    @Getter
    @Setter
    private Account account;

    @NotNull
    @Getter
    @Setter
    private PaymentChannel paymentChannel;

    @NotNull
    @DecimalMin(value = "1")
    @Getter
    @Setter
    private BigDecimal amount;
}
