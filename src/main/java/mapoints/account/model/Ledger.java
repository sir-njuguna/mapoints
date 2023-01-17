package mapoints.account.model;

import lombok.Getter;
import lombok.Setter;
import mapoints.lib.model.BaseModel;
import mapoints.lib.service.FormatUtil;
import mapoints.user.model.User;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Ledger extends BaseModel {
    @ManyToOne
    @JoinColumn(name = "account")
    @Getter
    @Setter
    private Account account;

    @ManyToOne
    @JoinColumn(name = "user")
    @Getter
    @Setter
    private User user;

    @Getter
    @Setter
    private String transactionCode;

    @Enumerated(EnumType.STRING)
    @Getter
    @Setter
    private TransactionType transactionType;

    @Column(precision = FormatUtil.BIG_DECIMAL_PRECISION ,scale = FormatUtil.BIG_DECIMAL_SCALE)
    @Getter
    @Setter
    private BigDecimal amount;

    @Column(precision = FormatUtil.BIG_DECIMAL_PRECISION ,scale = FormatUtil.BIG_DECIMAL_SCALE)
    @Getter
    @Setter
    private BigDecimal balanceBefore;

    @Column(precision = FormatUtil.BIG_DECIMAL_PRECISION ,scale = FormatUtil.BIG_DECIMAL_SCALE)
    @Getter
    @Setter
    private BigDecimal balanceAfter;
}
