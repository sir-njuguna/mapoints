package mapoints.shop.controller;

import mapoints.lib.service.AppConfig;
import mapoints.lib.service.Message;
import mapoints.lib.view.EntityApiResponse;
import mapoints.lib.view.PagedEntityApiResponse;
import mapoints.shop.form.FetchShopForm;
import mapoints.shop.form.ShopRegistrationForm;
import mapoints.shop.service.ShopService;
import mapoints.shop.view.ShopView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
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

    @GetMapping("fetch")
    public PagedEntityApiResponse<List<ShopView>> fetchShops(
            @RequestParam(value = "pageNum", required = false, defaultValue = "0") Integer pageNum,
            @RequestParam(value = "pageSize", required = false, defaultValue = "100") Integer pageSize,
            Authentication auth){

        FetchShopForm form = new FetchShopForm();
        form.setPageNum(pageNum);
        form.setPageSize(pageSize);
        form.setId(auth.getName());

        PagedEntityApiResponse<List<ShopView>> response = shopService.fetchShops(form);
        response.setStatus(true);
        response.setStatusCode(HttpStatus.OK.value());
        return response;
    }

    @Autowired
    public void setShopService(ShopService shopService) {
        this.shopService = shopService;
    }
}
