package mapoints.account.controller;

import mapoints.lib.service.AppConfig;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AppConfig.CUSTOMER_BASE_URL+"/account")
public class CustomerAccountController extends UserAccountController{
}
