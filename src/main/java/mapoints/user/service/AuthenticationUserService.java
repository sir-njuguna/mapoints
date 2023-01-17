package mapoints.user.service;

import mapoints.account.service.AccountService;
import mapoints.lib.exception.CommonRuntimeException;
import mapoints.lib.exception.ExceptionType;
import mapoints.lib.service.BaseService;
import mapoints.user.form.LoginForm;
import mapoints.user.form.PasswordResetForm;
import mapoints.user.form.UserRegistrationForm;
import mapoints.user.model.User;
import mapoints.user.model.UserType;
import mapoints.user.repository.UserRepository;
import mapoints.user.view.AuthUserView;
import mapoints.user.view.JwtView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationUserService extends BaseService<User, UserRepository> {
    private BasicUserService basicUserService;
    private VerificationCodeService verificationCodeService;
    private BCryptPasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private AccountService accountService;

    public AuthUserView register(UserRegistrationForm form){
        checkPhoneNumberExists(form.getPhoneNumber(), form.getUserType());
        verificationCodeService.verifyVerificationCode(form, form.getVerificationCode());

        User user = new User();
        user.setName(form.getName());
        user.setPhoneNumber(form.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.setUserType(form.getUserType());

        user = save(user);
        accountService.createAccount(user);
        return onUserAuthentication(user);
    }

    private void checkPhoneNumberExists(String phoneNumber, UserType userType){
        if(existsByPhoneNumber(phoneNumber, userType)){
            throw new CommonRuntimeException(
                    ExceptionType.ALREADY_EXISTS,
                    "user.phone-number-exits"
            );
        }
    }

    private boolean existsByPhoneNumber(String phoneNumber, UserType userType){
        return repository.existsByPhoneNumberAndUserType(phoneNumber, userType);
    }

    public AuthUserView login(LoginForm form){
        Optional<User> userOpt = repository.findByPhoneNumberAndUserType(
                form.getPhoneNumber(),
                form.getUserType()
        );

        if (userOpt.isEmpty()){
            throw new CommonRuntimeException(
                    ExceptionType.NOT_FOUND,
                    "user.phone-number-does-not-exist"
            );
        }

        User user = basicUserService.getUser(form.getPhoneNumber(), form.getUserType());
        if(!passwordEncoder.matches(form.getPassword(), user.getPassword())){
            throw new CommonRuntimeException(
                    ExceptionType.NOT_AUTHORISED,
                    "auth.invalid-username-password"
            );
        }

        return onUserAuthentication(user);
    }

    public AuthUserView resetPassword(PasswordResetForm form){
        User user = basicUserService.getUser(form.getPhoneNumber(), form.getUserType());
        verificationCodeService.verifyVerificationCode(form, form.getVerificationCode());

        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user = save(user);

        return onUserAuthentication(user);
    }


    private AuthUserView onUserAuthentication(User user){
        JwtView jwtView = jwtService.generateJwt(user);
        user.setRecentAuthId(passwordEncoder.encode(jwtView.getJwtId()));

        user = save(user);

        AuthUserView userView = new AuthUserView(user);
        userView.setAuthToken(jwtView.getJwtToken());
        return userView;
    }

    @Autowired
    public void setBasicUserService(BasicUserService basicUserService) {
        this.basicUserService = basicUserService;
    }

    @Autowired
    public void setVerificationCodeService(VerificationCodeService verificationCodeService) {
        this.verificationCodeService = verificationCodeService;
    }

    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}
