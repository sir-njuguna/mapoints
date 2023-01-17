package mapoints.account.repository;

import mapoints.account.model.Account;
import mapoints.account.model.Ledger;
import mapoints.lib.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface LedgerRepository extends BaseRepository<Ledger> {
    Page<Ledger> findByAccount(Account account, Pageable pageable);
}
