package mapoints.user.service;

import mapoints.lib.exception.CommonRuntimeException;
import mapoints.lib.exception.ExceptionType;
import mapoints.lib.service.BaseService;
import mapoints.user.model.User;
import mapoints.user.model.UserType;
import mapoints.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BasicUserService extends BaseService<User, UserRepository> {

    public User getUser(String phoneNumber, UserType userType){
        Optional<User> userOpt = repository.findByPhoneNumberAndUserType(phoneNumber, userType);

        if (userOpt.isEmpty()){
            throw new CommonRuntimeException(
                    ExceptionType.NOT_FOUND,
                    "user.phone-number-does-not-exist"
            );
        }

        return userOpt.get();
    }
}
