package pk.com.financialanalysis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FinancialService {

    @Value("${getEndPoint}")
    private String getEndPoint;

    private final HttpService httpService;

    @Autowired
    public FinancialService(HttpService httpService) {
        this.httpService = httpService;
    }

    public Object getFindata() {

        Object response = httpService.get(getEndPoint);
        // Do something with the response
        return response;
    }
}
