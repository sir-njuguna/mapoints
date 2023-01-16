package mapoints.user.repository;

import mapoints.lib.repository.BaseRepository;
import mapoints.user.model.User;
import mapoints.user.model.UserType;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User> {
    boolean existsByPhoneNumberAndUserType(String phoneNumber, UserType userType);
    Optional<User> findByPhoneNumberAndUserType(String phoneNumber, UserType userType);
}
