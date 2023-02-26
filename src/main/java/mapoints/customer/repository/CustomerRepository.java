package mapoints.customer.repository;

import mapoints.customer.model.Customer;
import mapoints.lib.repository.BaseRepository;
import mapoints.shop.model.Shop;
import mapoints.user.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends BaseRepository<Customer> {
    boolean existsByShopAndUser(Shop shop, User user);
}
