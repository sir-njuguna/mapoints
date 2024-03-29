package mapoints.account.service;

import mapoints.account.model.Account;
import mapoints.account.repository.AccountRepository;
import mapoints.account.view.AccountView;
import mapoints.lib.exception.CommonRuntimeException;
import mapoints.lib.exception.ExceptionType;
import mapoints.lib.exception.InsufficientBalanceException;
import mapoints.lib.service.BaseService;
import mapoints.user.model.User;
import mapoints.user.model.UserType;
import mapoints.user.service.BasicUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService extends BaseService<Account, AccountRepository> {
    private BasicUserService basicUserService;

    public void createAccount(User user){
        List<UserType> allowedTypes = List.of(UserType.CUSTOMER, UserType.MERCHANT);
        if(!allowedTypes.contains(user.getUserType())){
            return;
        }
        checkAccountExists(user);

        Account account = new Account();
        account.setUser(user);
        save(account);
    }

    private void  checkAccountExists(User merchant){
        if(repository.existsByUser(merchant)){
            throw new CommonRuntimeException(
                ExceptionType.BAD_REQUEST,
                "account.exists-error"
            );
        }
    }

    public Account getAccountByUser(String phoneNumber, UserType userType){
        User user = basicUserService.getUser(phoneNumber, userType);
        return getAccountByUser(user);
    }


    public Account getAccountByUser(String userEntityId){
        User user = basicUserService.findByEntityId(userEntityId);
        return getAccountByUser(user);
    }

    public Account getAccountByUser(User user){
        Optional<Account> accountOpt = repository.findTopByUser(user);
        if(accountOpt.isEmpty()){
            throw new CommonRuntimeException(
                    ExceptionType.NOT_FOUND,
                    "entity.not-found"
            );
        }
        return accountOpt.get();
    }


    public AccountView getBalance(String userId){
        User user = basicUserService.findByEntityId(userId);
        Account account = getAccountByUser(user);
        return new AccountView(account);
    }

    public void checkBalance(Account account, BigDecimal amount){
        if(account.getBalance().compareTo(amount) <= 0){
            throw new InsufficientBalanceException(account.getBalance());
        }
    }



    @Autowired
    public void setBasicUserService(BasicUserService basicUserService) {
        this.basicUserService = basicUserService;
    }
}
