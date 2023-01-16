package mapoints.sale.model;

import lombok.Getter;
import lombok.Setter;
import mapoints.lib.model.BaseModel;
import mapoints.shop.model.Shop;
import mapoints.user.model.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

    @Column(precision = 15, scale = 5)
    @Getter
    @Setter
    private BigDecimal cashAmount;

    @Column(precision = 15, scale = 5)
    @Getter
    @Setter
    private BigDecimal points;
}
