package org.swiftpay.configuration;

import feign.RequestInterceptor;
import feign.codec.Decoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

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
