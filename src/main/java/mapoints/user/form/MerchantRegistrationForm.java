package mapoints.user.form;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.WordUtils;

import javax.validation.constraints.NotBlank;

public class MerchantRegistrationForm extends UserRegistrationForm{

    @NotBlank(message = "error.blank-shop-name")
    @Setter
    private String shopName;

    @NotBlank(message = "error.blank-shop-description")
    @Getter
    @Setter
    private String ShopDescription;


    public String getShopName() {
        if(shopName != null){
            shopName = WordUtils.capitalize(shopName.toLowerCase());
        }
        return shopName;
    }
}
