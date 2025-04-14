package org.swiftpay.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

    @Bean
    public OpenAPI openAPI () {

        return new OpenAPI()
                        .info(new Info().title("Payment API")
                        .version("1.0")
                        .description("An easy and useful payment API")
                        .license(new License().name("MIT License").url("https://opensource.org/licenses/MIT"))
                        .contact(new Contact().name("Invked").email("samuelnobrega902@gmail.com").url("https://github.com/Invokedzz")));

    }

}
