package mapoints.sale.repository;

import mapoints.lib.repository.BaseRepository;
import mapoints.sale.model.Sale;
import mapoints.shop.model.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository extends BaseRepository<Sale> {
    Page<Sale> findByShop(Shop shop, Pageable pageable);
}
