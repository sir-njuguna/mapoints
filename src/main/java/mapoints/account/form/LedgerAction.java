package mapoints.account.form;

import lombok.Getter;
import lombok.Setter;

import mapoints.payment.model.PaymentChannel;

import java.math.BigDecimal;

public class LedgerAction {

    @Getter
    @Setter
    private String accountEntityId;

    @Getter
    @Setter
    private BigDecimal amount;

    @Getter
    @Setter
    private String transactionCode;

    @Getter
    @Setter
    private PaymentChannel paymentChannel;
}
