package mapoints.sale.controller;

import mapoints.lib.service.AppConfig;
import mapoints.lib.view.EntityApiResponse;
import mapoints.lib.view.PagedEntityApiResponse;
import mapoints.sale.form.CreateSaleForm;
import mapoints.sale.form.FetchSaleForm;
import mapoints.sale.service.SaleService;
import mapoints.sale.view.SaleView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(AppConfig.MERCHANT_BASE_URL + "/sale")
public class MerchantSaleController {
    private SaleService saleService;
    @PostMapping("register")
    public EntityApiResponse<SaleView> registerSale(@RequestBody @Valid CreateSaleForm form){
        SaleView saleView = saleService.createSale(form);
        return new EntityApiResponse<>(saleView);
    }

    @GetMapping("fetch")
    public PagedEntityApiResponse<List<SaleView>> fetch(
            @RequestParam(value = "shopId") String shopId,
            @RequestParam(value = "pageNum", required = false, defaultValue = "0") Integer pageNum,
            @RequestParam(value = "pageSize", required = false, defaultValue = "100") Integer pageSize,
            Authentication auth){
        FetchSaleForm form = new FetchSaleForm();
        form.setId(shopId);
        form.setPageNum(pageNum);
        form.setPageSize(pageSize);

        PagedEntityApiResponse<List<SaleView>> response = saleService.fetchSales(form, auth.getName());
        response.setStatus(true);
        response.setStatusCode(HttpStatus.OK.value());
        return response;
    }

    @Autowired
    public void setSaleService(SaleService saleService) {
        this.saleService = saleService;
    }
}
