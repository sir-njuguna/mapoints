package mapoints.shop.service;

import mapoints.lib.exception.CommonRuntimeException;
import mapoints.lib.exception.ExceptionType;
import mapoints.lib.service.BaseService;
import mapoints.shop.form.ShopRegistrationForm;
import mapoints.shop.model.Shop;
import mapoints.shop.repository.ShopRepository;
import mapoints.shop.view.ShopView;
import mapoints.user.model.User;
import mapoints.user.service.BasicUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShopService extends BaseService<Shop, ShopRepository> {
    private BasicUserService basicUserService;

    public ShopView createShop(ShopRegistrationForm form, String merchantId){
        User merchant = basicUserService.findByEntityId(merchantId);

        checkNameExists(merchant, form.getName());

        Shop shop = new Shop();
        shop.setMerchant(merchant);
        shop.setName(form.getName());
        shop.setDescription(form.getDescription());
        shop.setCashToPoint(form.getCashToPoint());

        shop = save(shop);
        return new ShopView(shop);
    }
    private void checkNameExists(User merchant, String name){
        if(repository.existsByMerchantAndName(merchant, name)){
            throw new CommonRuntimeException(
                    ExceptionType.ALREADY_EXISTS,
                    "shop.name-exits"
            );
        }
    }

    @Autowired
    public void setUserService(BasicUserService basicUserService) {
        this.basicUserService = basicUserService;
    }
}
