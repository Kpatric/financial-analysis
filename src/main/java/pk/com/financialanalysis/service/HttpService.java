package pk.com.financialanalysis.service;

import org.springframework.stereotype.Service;

@Service
public interface HttpService {
    Object get(String url);
}
