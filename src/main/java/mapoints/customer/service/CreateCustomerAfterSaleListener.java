package mapoints.customer.service;

import mapoints.customer.model.Customer;
import mapoints.customer.repository.CustomerRepository;
import mapoints.lib.service.BaseService;
import mapoints.sale.model.Sale;
import mapoints.sale.service.AfterSaleListener;
import mapoints.shop.model.Shop;
import mapoints.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateCustomerAfterSaleListener
        extends BaseService<Customer, CustomerRepository>
        implements AfterSaleListener{

    private CustomerService customerService;

    @Override
    public void doAfterSale(Sale sale) {
        Shop shop = sale.getShop();
        User user = sale.getCustomer();

        if (!repository.existsByShopAndUser(shop, user)){
            customerService.createCustomer(shop, user);
        }
    }

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }
}
