package mapoints.user.controller;

import mapoints.lib.service.AppConfig;
import mapoints.user.model.UserType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AppConfig.CUSTOMER_BASE_URL)
public class CustomerController extends UserController{
    @Override
    protected UserType getUserType(){
        return UserType.CUSTOMER;
    }
}
