package mapoints.sale.service;

import mapoints.sale.model.Sale;

public interface AfterSaleListener {
    void doAfterSale(Sale sale);
}
