package mapoints.shop.repository;

import mapoints.lib.repository.BaseRepository;
import mapoints.shop.model.Shop;
import mapoints.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends BaseRepository<Shop> {
    boolean existsByMerchantAndName(User merchant, String name);
    Page<Shop> findByMerchant(User merchant, Pageable pageable);
}
