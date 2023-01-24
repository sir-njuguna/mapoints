package mapoints.airtime.form;

import lombok.Getter;
import lombok.Setter;
import mapoints.account.model.Account;

import java.math.BigDecimal;

public class SendAirtimeForm {
    @Getter
    @Setter
    private Account account;

    @Getter
    @Setter
    private String currencyCode = "KES";

    @Getter
    @Setter
    private BigDecimal amount;
}
