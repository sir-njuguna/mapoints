package mapoints.shop.model;

import lombok.Getter;
import lombok.Setter;
import mapoints.lib.model.BaseModel;
import mapoints.user.model.User;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"name", "merchant"})
})
public class Shop extends BaseModel {
    @ManyToOne
    @JoinColumn(name = "merchant")
    @Getter
    @Setter
    private User merchant;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String description;

    @Column( precision = 15, scale = 5)
    @Getter
    @Setter
    private BigDecimal cashToPoint;

    @Getter
    @Setter
    private Integer customersCount = 0;
}
