package mapoints.user.controller;

import mapoints.lib.service.Message;
import mapoints.lib.view.ApiResponse;
import mapoints.lib.view.EntityApiResponse;
import mapoints.user.form.LoginForm;
import mapoints.user.form.PhoneNumberForm;
import mapoints.user.form.PasswordResetForm;
import mapoints.user.model.UserType;
import mapoints.user.service.UserAuthService;
import mapoints.user.service.VerificationCodeService;
import mapoints.user.view.AuthUserView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.Locale;

public abstract class UserController {
    protected UserAuthService userAuthService;
    private VerificationCodeService verificationCodeService;

    @PostMapping("login")
    public EntityApiResponse<AuthUserView> login(@RequestBody @Valid LoginForm form){
        form.setUserType(getUserType());
        return new EntityApiResponse<>(userAuthService.login(form));
    }


    @PostMapping("generate_auth_code")
    public ApiResponse generateAuthCode(@RequestBody @Valid PhoneNumberForm form, Locale locale){
        form.setUserType(getUserType());
        verificationCodeService.generateVerificationCode(form);
        String msg = String.format(
                Message.get("verification.code.sent", locale), form.getPhoneNumber()
        );
        return new ApiResponse(
                true,
                HttpStatus.OK.value(),
                msg
        );
    }

    @PostMapping("reset_password")
    public EntityApiResponse<AuthUserView> resetPassword(@RequestBody @Valid PasswordResetForm form){
        form.setUserType(getUserType());
        return new EntityApiResponse<>(userAuthService.resetPassword(form));
    }


    protected abstract UserType getUserType();

    @Autowired
    public void setUserAuthService(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @Autowired
    public void setVerificationCodeService(VerificationCodeService verificationCodeService) {
        this.verificationCodeService = verificationCodeService;
    }
}
