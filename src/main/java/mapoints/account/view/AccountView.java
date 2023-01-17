package mapoints.account.view;

import mapoints.account.model.Account;
import mapoints.lib.view.BaseView;

import java.math.BigDecimal;

public class AccountView extends BaseView<Account> {
    public AccountView(Account entity) {
        super(entity);
    }

    public BigDecimal getBalance(){
        return entity.getBalance();
    }

    public String getMerchantName(){
        return entity.getUser().getName();
    }
}
