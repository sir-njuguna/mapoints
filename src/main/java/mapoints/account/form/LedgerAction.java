package mapoints.account.form;

import lombok.Getter;
import lombok.Setter;

import mapoints.account.model.Account;
import mapoints.account.model.TransactionType;
import mapoints.payment.model.PaymentChannel;

import java.math.BigDecimal;

public class LedgerAction {

    @Getter
    @Setter
    private Account account;

    @Getter
    @Setter
    private BigDecimal amount;

    @Getter
    @Setter
    private String transactionCode;

    @Getter
    @Setter
    private PaymentChannel paymentChannel;

    @Getter
    @Setter
    private TransactionType transactionType;
}
