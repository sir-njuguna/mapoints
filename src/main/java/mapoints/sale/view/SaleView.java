package mapoints.sale.view;

import mapoints.lib.view.BaseView;
import mapoints.sale.model.Sale;
import mapoints.sale.model.SaleType;

import java.math.BigDecimal;

public class SaleView extends BaseView<Sale> {
    public SaleView(Sale entity) {
        super(entity);
    }

    public String getSaleEntityId(){
        return entity.getEntityId();
    }

    public String getShopName(){
        return entity.getShop().getName();
    }

    public SaleType getSaleType(){
        return entity.getSaleType();
    }

    public BigDecimal getAmount(){
        return entity.getAmount();
    }

    public BigDecimal getPoints(){
        return entity.getPoints();
    }

    public String getDescription(){
        return entity.getDescription();
    }

    public String getCustomerName(){
        return entity.getCustomer().getName();
    }

    public String getCustomerPhoneNumber(){
        return entity.getCustomer().getPhoneNumber();
    }
}
