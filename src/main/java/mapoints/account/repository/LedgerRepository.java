package mapoints.account.repository;

import mapoints.account.model.Ledger;
import mapoints.lib.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LedgerRepository extends BaseRepository<Ledger> {
}
