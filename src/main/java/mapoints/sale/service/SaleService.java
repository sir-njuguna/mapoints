package mapoints.sale.service;

import mapoints.account.form.LedgerAction;
import mapoints.account.model.Account;
import mapoints.account.model.TransactionType;
import mapoints.account.service.AccountService;
import mapoints.account.service.LedgerService;
import mapoints.lib.exception.CommonRuntimeException;
import mapoints.lib.exception.ExceptionType;
import mapoints.lib.exception.InsufficientBalanceException;
import mapoints.lib.repository.BaseRepository;
import mapoints.lib.service.BaseService;
import mapoints.lib.service.FormatUtil;
import mapoints.lib.view.PagedEntityApiResponse;
import mapoints.payment.model.PaymentChannel;
import mapoints.sale.form.CreateSaleForm;
import mapoints.sale.form.FetchSaleForm;
import mapoints.sale.model.Sale;
import mapoints.sale.model.SaleType;
import mapoints.sale.repository.SaleRepository;
import mapoints.sale.view.SaleView;
import mapoints.shop.model.Shop;
import mapoints.shop.service.ShopService;
import mapoints.user.model.User;
import mapoints.user.model.UserType;
import mapoints.user.service.BasicUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleService extends BaseService<Sale, SaleRepository> {

    private BasicUserService basicUserService;
    private ShopService shopService;
    private AccountService accountService;
    private LedgerService ledgerService;


    public SaleView createSale(CreateSaleForm form){
        Shop shop = shopService.findByEntityId(form.getShopEntityId());
        User customer = basicUserService.getUser(form.getCustomerPhoneNumber(), UserType.CUSTOMER);

        BigDecimal points = BigDecimal.ZERO;
        if(form.getSaleType() == SaleType.RECEIPT){
            points = form.getAmount().divide(shop.getCashToPoint(), FormatUtil.ROUND_DOWN_MATH_CONTEXT);
        }

        //Check if owner of the shop has sufficient balance
        Account merchantAccount = accountService.getAccountByUser(shop.getMerchant());
        if(merchantAccount.getBalance().compareTo(points) <= 0){
            throw new InsufficientBalanceException(merchantAccount.getBalance());
        }

        Sale sale = new Sale();
        sale.setShop(shop);
        sale.setCustomer(customer);
        sale.setSaleType(form.getSaleType());
        sale.setAmount(form.getAmount());
        sale.setPoints(points);
        sale.setDescription(form.getDescription());

        sale = save(sale);

        //Debit merchant merchantAccount
        LedgerAction debit = new LedgerAction();
        debit.setAccountEntityId(merchantAccount.getEntityId());
        debit.setAmount(points);
        debit.setPaymentChannel(PaymentChannel.INTERNAL);
        debit.setTransactionType(TransactionType.DEBIT_POINTS);
        debit.setTransactionCode(ledgerService.generateTransactionCode(TransactionType.DEBIT_POINTS));

        ledgerService.debit(debit);

        //Credit customer merchantAccount
        Account customerAccount = accountService.getAccountByUser(customer);
        LedgerAction credit = new LedgerAction();
        credit.setAccountEntityId(customerAccount.getEntityId());
        credit.setAmount(points);
        credit.setPaymentChannel(PaymentChannel.INTERNAL);
        credit.setTransactionType(TransactionType.CREDIT_POINTS);
        credit.setTransactionCode(ledgerService.generateTransactionCode(TransactionType.CREDIT_POINTS));

        ledgerService.credit(credit);

        return new SaleView(sale);
    }


    public PagedEntityApiResponse<List<SaleView>> fetchSales(FetchSaleForm form, String merchantEntityId){
        Shop shop = shopService.findByEntityId(form.getId());
        User merchant = basicUserService.findByEntityId(merchantEntityId);

        if(!shop.getMerchant().equals(merchant)){
            throw new CommonRuntimeException(ExceptionType.BAD_REQUEST, "auth.operation-not-allowed");
        }

        Page<Sale> page = repository.findByShop(
                shop,
                BaseRepository.defaultPageable(form.getPageNum(), form.getPageSize())
        );
        List<SaleView> views = page.stream().map(SaleView::new).collect(Collectors.toList());
        return new PagedEntityApiResponse<>(page, views);
    }

    @Autowired
    public void setBasicUserService(BasicUserService basicUserService) {
        this.basicUserService = basicUserService;
    }

    @Autowired
    public void setShopService(ShopService shopService) {
        this.shopService = shopService;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    public void setLedgerService(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }
}
