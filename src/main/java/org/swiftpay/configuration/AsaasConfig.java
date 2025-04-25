package org.swiftpay.configuration;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AsaasConfig {

    @Value("${asaas.api.key}")
    private String apiKey;

    @Bean
    public RequestInterceptor requestInterceptor() {

        return template -> {

            template.header("Content-Type", "application/json");
            template.header("Accept", "application/json");

            template.header("access_token", apiKey);

        };

    }

}
