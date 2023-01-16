package mapoints.shop.controller;

import mapoints.lib.service.AppConfig;
import mapoints.lib.service.Message;
import mapoints.lib.view.EntityApiResponse;
import mapoints.shop.form.ShopRegistrationForm;
import mapoints.shop.service.ShopService;
import mapoints.shop.view.ShopView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Locale;

@RestController
@RequestMapping(AppConfig.MERCHANT_BASE_URL+"/shop")
public class ShopController {

    private ShopService shopService;

    @PostMapping("create")
    public EntityApiResponse<ShopView> create(
            @RequestBody @Valid ShopRegistrationForm form,
            Authentication auth,
            Locale locale){
        ShopView view = shopService.createShop(form, auth.getName());

        return new EntityApiResponse<>(
            true,
            HttpStatus.OK.value(),
            Message.get("shop.successfully-registered", locale),
            view
        );
    }

    @Autowired
    public void setShopService(ShopService shopService) {
        this.shopService = shopService;
    }
}
