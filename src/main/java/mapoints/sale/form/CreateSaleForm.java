package mapoints.sale.form;

import lombok.Getter;
import lombok.Setter;
import mapoints.sale.model.SaleType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CreateSaleForm {

    @NotBlank(message = "error.empty-shop-id")
    @Getter
    @Setter
    private String shopEntityId;

    @NotNull(message = "error.empty-sale-type")
    @Getter
    @Setter
    private SaleType saleType;

    @NotNull(message = "error.empty-sale-amount")
    @Getter
    @Setter
    private BigDecimal amount;

    @NotNull(message = "error.empty-customer-phone-number")
    @Getter
    @Setter
    private String customerPhoneNumber;

    @Getter
    @Setter
    private String description;
}
