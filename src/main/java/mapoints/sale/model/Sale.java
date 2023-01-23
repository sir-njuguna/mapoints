package mapoints.sale.model;

import lombok.Getter;
import lombok.Setter;
import mapoints.lib.model.BaseModel;
import mapoints.lib.service.FormatUtil;
import mapoints.shop.model.Shop;
import mapoints.user.model.User;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Sale extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "shop")
    @Getter
    @Setter
    private Shop shop;

    @ManyToOne
    @JoinColumn(name = "customer")
    @Getter
    @Setter
    private User customer;

    @Enumerated(EnumType.STRING)
    @Getter
    @Setter
    private SaleType saleType;

    @Column(precision = FormatUtil.BIG_DECIMAL_PRECISION, scale = FormatUtil.BIG_DECIMAL_SCALE)
    @Getter
    @Setter
    private BigDecimal amount;

    @Column(precision = FormatUtil.BIG_DECIMAL_PRECISION, scale = FormatUtil.BIG_DECIMAL_SCALE)
    @Getter
    @Setter
    private BigDecimal points;

    @Column(length = 100)
    @Getter
    @Setter
    private String description;
}
