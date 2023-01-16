package mapoints.shop.repository;

import mapoints.lib.repository.BaseRepository;
import mapoints.shop.model.Shop;
import mapoints.user.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends BaseRepository<Shop> {
    boolean existsByMerchantAndName(User merchant, String name);
}
