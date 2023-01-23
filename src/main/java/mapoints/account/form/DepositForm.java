package mapoints.account.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class DepositForm {

    @NotNull(message = "error.empty-deposit-amount")
    @Getter
    @Setter
    private BigDecimal amount;
}
