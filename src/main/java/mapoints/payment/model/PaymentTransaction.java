package mapoints.payment.model;


import lombok.Getter;
import lombok.Setter;
import mapoints.account.model.Account;
import mapoints.account.model.Ledger;
import mapoints.lib.model.BaseModel;
import mapoints.lib.service.FormatUtil;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class PaymentTransaction extends BaseModel {
    @Enumerated(EnumType.STRING)
    @Getter
    @Setter
    private PaymentChannel paymentChannel;

    @Column(precision = FormatUtil.BIG_DECIMAL_PRECISION ,scale = FormatUtil.BIG_DECIMAL_SCALE)
    @Getter
    @Setter
    private BigDecimal amount;

    /**
     * Unique identifier provided by the {@link PaymentChannel}
     * that identifies this transaction during initialization and callback
     * eg ws_CO_19092020000828106872
     */
    @Getter
    @Setter
    private String transactionId;

    /**
     * Unique human-readable transaction ID assigned to a transaction
     * that the customer can identify.
     * e.g. OIH9MPS33D
     */
    @Getter
    @Setter
    private String transactionCode;

    @Enumerated(EnumType.STRING)
    @Getter
    @Setter
    private PaymentType paymentType;

    @ManyToOne
    @JoinColumn(name = "account")
    @Getter
    @Setter
    private Account account;

    @OneToOne
    @JoinColumn(name = "ledger")
    @Getter
    @Setter
    private Ledger ledger;


    @Type(type = "json")
    @Column(columnDefinition = "json")
    @Getter
    @Setter
    private PaymentResponse initialResponse;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    @Getter
    @Setter
    private PaymentResponse callbackResponse;

}
