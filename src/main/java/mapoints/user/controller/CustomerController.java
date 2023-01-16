package mapoints.user.controller;

import mapoints.lib.service.AppConfig;
import mapoints.lib.view.EntityApiResponse;
import mapoints.user.form.UserRegistrationForm;
import mapoints.user.model.UserType;
import mapoints.user.view.AuthUserView;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(AppConfig.CUSTOMER_BASE_URL)
public class CustomerController extends UserController{

    @PostMapping("register")
    public EntityApiResponse<AuthUserView> register(@RequestBody @Valid UserRegistrationForm form){
        form.setUserType(getUserType());
        AuthUserView view = userAuthService.register(form);
        return new EntityApiResponse<>(view);
    }

    @Override
    protected UserType getUserType(){
        return UserType.CUSTOMER;
    }
}
