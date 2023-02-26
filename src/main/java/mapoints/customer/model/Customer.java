package mapoints.customer.model;

import lombok.Getter;
import lombok.Setter;
import mapoints.lib.model.BaseModel;
import mapoints.shop.model.Shop;
import mapoints.user.model.User;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Customer extends BaseModel {
    @ManyToOne
    @JoinColumn(name = "shop")
    @Getter
    @Setter
    private Shop shop;

    @ManyToOne
    @JoinColumn(name = "user")
    @Getter
    @Setter
    private User user;
}
