package pk.com.financialanalysis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import pk.com.financialanalysis.service.FinancialService;

public class FinancialController {

    @Autowired
    private FinancialService financialService;

    @GetMapping("/findata")
    public Object getPaymentDetails() {
        return financialService.getFindata();
    }
}
