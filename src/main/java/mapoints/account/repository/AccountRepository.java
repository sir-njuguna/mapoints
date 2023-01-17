package mapoints.account.repository;

import mapoints.account.model.Account;
import mapoints.lib.repository.BaseRepository;
import mapoints.user.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends BaseRepository<Account> {
    boolean existsByUser(User user);
    Optional<Account> findTopByUser(User user);
}
