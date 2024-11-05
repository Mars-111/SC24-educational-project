package ag.selm.manager.config;

import ag.selm.manager.client.RestClientProductRestClientImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestClient;

@Configuration
public class ClientBeans {
    @Bean
    public RestClientProductRestClientImpl productRestClient(
            @Value("${selmag.services.catalogue.url:http://localhost:8081}") String catalogueBaseUri,
            @Value("${selmag.services.catalogue.username:}") String catalogueUsername,
            @Value("${selmag.services.catalogue.password:}") String cataloguePassword) {
        return new RestClientProductRestClientImpl(RestClient.builder()
                .baseUrl(catalogueBaseUri)
                .requestInterceptor(new BasicAuthenticationInterceptor(catalogueUsername, cataloguePassword)) //Перехватчик запросов
                .build());
    }
}
