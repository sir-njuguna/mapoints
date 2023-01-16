package mapoints.shop.form;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.WordUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ShopRegistrationForm {

    @NotBlank(message = "error.blank-shop-name")
    @Setter
    private String name;

    @NotBlank(message = "error.blank-shop-description")
    @Getter
    @Setter
    private String description;

    @NotNull(message = "error.null-cash-to-points")
    @Getter
    @Setter
    private BigDecimal cashToPoint;


    public String getName() {
        if(name != null){
            name = WordUtils.capitalize(name.toLowerCase());
        }
        return name.trim();
    }
}
