package mapoints.customer.service;

import mapoints.customer.model.Customer;
import mapoints.customer.repository.CustomerRepository;
import mapoints.lib.service.BaseService;
import mapoints.shop.model.Shop;
import mapoints.user.model.User;
import org.springframework.stereotype.Service;

@Service
public class CustomerService extends BaseService<Customer, CustomerRepository> {

    void createCustomer(Shop shop, User user){
        Customer customer = new Customer();
        customer.setShop(shop);
        customer.setUser(user);
        repository.save(customer);
    }
}
