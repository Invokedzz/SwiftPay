package org.swiftpay.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import br.com.caelum.stella.validation.CPFValidator;

@Component
public class StellaConfig {

    @Bean
    public CPFValidator cpfValidator () {

        return new CPFValidator();

    }

}
