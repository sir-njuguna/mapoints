package mapoints.account.view;

import mapoints.account.model.Ledger;
import mapoints.account.model.TransactionType;
import mapoints.lib.view.BaseView;

import java.math.BigDecimal;

public class LedgerView extends BaseView<Ledger> {

    public LedgerView(Ledger entity) {
        super(entity);
    }

    public BigDecimal getAmount(){
        return entity.getAmount();
    }

    public BigDecimal getBalanceBefore(){
        return entity.getBalanceBefore();
    }

    public BigDecimal getBalanceAfter(){
        return entity.getBalanceAfter();
    }

    public String getTransactionCode(){
        return entity.getTransactionCode();
    }

    public TransactionType getTransactionType(){
        return entity.getTransactionType();
    }
}
