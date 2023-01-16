package mapoints.shop;

import lombok.Getter;
import lombok.Setter;
import mapoints.lib.model.BaseModel;
import mapoints.user.model.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Entity
public class Shop extends BaseModel {
    @ManyToOne
    @JoinColumn(name = "owner")
    @Getter
    @Setter
    private User owner;

    @Getter
    @Setter
    private String shopName;

    @Getter
    @Setter
    private String description;

    @Column( precision = 15, scale = 5)
    @Getter
    @Setter
    private BigDecimal cashToPoint;
}
