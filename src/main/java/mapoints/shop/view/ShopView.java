package mapoints.shop.view;

import mapoints.lib.view.BaseView;
import mapoints.shop.model.Shop;

import java.math.BigDecimal;

public class ShopView extends BaseView<Shop> {
    public ShopView(Shop entity) {
        super(entity);
    }

    public String getName(){
        return entity.getName();
    }

    public String getDescription(){
        return entity.getDescription();
    }

    public BigDecimal getCashToPoint(){
        return entity.getCashToPoint();
    }

    public String getEntityId(){
        return entity.getEntityId();
    }
}
