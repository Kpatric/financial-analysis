package pk.com.financialanalysis.service;

import org.springframework.stereotype.Service;
import pk.com.financialanalysis.config.HttpClient;

@Service
public class HttpServiceImpl implements HttpService {

    private final HttpClient httpClient;

    public HttpServiceImpl(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public Object get(String url) {
        return httpClient.getRestTemplate().getForObject(url,Object.class);
    }

}

