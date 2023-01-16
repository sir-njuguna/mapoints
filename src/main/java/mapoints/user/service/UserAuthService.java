package mapoints.user.service;

import mapoints.lib.exception.CommonRuntimeException;
import mapoints.lib.exception.ExceptionType;
import mapoints.lib.service.BaseService;
import mapoints.user.form.UserForm;
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
public class UserAuthService extends BaseService<User, UserRepository> {
    private BCryptPasswordEncoder passwordEncoder;
    private JwtService jwtService;

    public AuthUserView register(UserRegistrationForm form){
        checkPhoneNumberExists(form.getPhoneNumber(), form.getUserType());

        User user = new User();
        user.setName(form.getName());
        user.setPhoneNumber(form.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.setUserType(form.getUserType());

        user = save(user);
        return onUserAuthentication(user);
    }

    protected void checkPhoneNumberExists(String phoneNumber, UserType userType){
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

    public AuthUserView login(UserForm form){
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

        User user = getUser(form);
        if(!passwordEncoder.matches(form.getPassword(), user.getPassword())){
            throw new CommonRuntimeException(
                    ExceptionType.NOT_AUTHORISED,
                    "auth.invalid-username-password"
            );
        }

        return onUserAuthentication(user);
    }

    private User getUser(UserForm form){
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

        return userOpt.get();
    }

    private AuthUserView onUserAuthentication(User user){
        JwtView jwtView = jwtService.generateJwt(user);
        user.setRecentAuthId(jwtView.getJwtId());

        user = save(user);

        AuthUserView userView = new AuthUserView(user);
        userView.setAuthToken(jwtView.getJwtToken());
        return userView;
    }


    private void generateAuthCode(UserForm form){

    }

    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }
}