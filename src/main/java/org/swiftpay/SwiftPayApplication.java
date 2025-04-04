package org.swiftpay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SwiftPayApplication {

    public static void main (String[] args) {

        SpringApplication.run(SwiftPayApplication.class, args);

    }

}
