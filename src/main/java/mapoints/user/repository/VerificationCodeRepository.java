package mapoints.user.repository;

import mapoints.lib.repository.BaseRepository;
import mapoints.user.model.UserType;
import mapoints.user.model.VerificationCode;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends BaseRepository<VerificationCode> {
    boolean existsByPhoneNumberAndUserType(String phoneNumber, UserType userType);
    Optional<VerificationCode> findByPhoneNumberAndUserType(String phoneNumber, UserType userType);
}
