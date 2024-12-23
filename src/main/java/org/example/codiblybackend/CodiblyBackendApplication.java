package org.example.codiblybackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class CodiblyBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(CodiblyBackendApplication.class, args);
    }
}
