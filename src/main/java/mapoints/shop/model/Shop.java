package mapoints.shop.model;

import lombok.Getter;
import lombok.Setter;
import mapoints.lib.model.BaseModel;
import mapoints.lib.service.FormatUtil;
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

    @Column(precision = FormatUtil.BIG_DECIMAL_PRECISION ,scale = FormatUtil.BIG_DECIMAL_SCALE)
    @Getter
    @Setter
    private BigDecimal cashToPoint;

    @Getter
    @Setter
    private Integer customersCount = 0;
}
