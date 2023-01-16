package mapoints.user.controller;

import mapoints.lib.view.EntityApiResponse;
import mapoints.user.form.UserForm;
import mapoints.user.model.UserType;
import mapoints.user.service.UserAuthService;
import mapoints.user.view.AuthUserView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public abstract class UserController {
    protected UserAuthService userAuthService;

    @PostMapping("login")
    public EntityApiResponse<AuthUserView> login(@RequestBody @Valid UserForm form){
        form.setUserType(getUserType());
        return new EntityApiResponse<>(userAuthService.login(form));
    }

    protected abstract UserType getUserType();

    @Autowired
    public void setUserAuthService(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }
}
