package mapoints.account.model;

import lombok.Getter;
import lombok.Setter;
import mapoints.lib.model.BaseModel;
import mapoints.lib.service.FormatUtil;
import mapoints.user.model.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.math.BigDecimal;

@Entity
public class Account extends BaseModel {
    @OneToOne
    @JoinColumn(name = "user")
    @Getter
    @Setter
    private User user;

    @Column(precision = FormatUtil.BIG_DECIMAL_PRECISION ,scale = FormatUtil.BIG_DECIMAL_SCALE)
    @Setter
    private BigDecimal balance;

    public BigDecimal getBalance(){
        return balance == null
                ? BigDecimal.ZERO
                : balance;
    }
}
