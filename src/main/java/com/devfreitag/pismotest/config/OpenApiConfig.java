package com.devfreitag.pismotest.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Pismo Test API")
                        .version("1.0.0")
                        .description("""
                                REST API for managing financial accounts and transactions.

                                ## Key Features
                                - Account creation and retrieval
                                - Transaction processing with automatic amount sign adjustment

                                ## Transaction Amount Logic
                                The API automatically adjusts transaction amounts based on operation type:
                                - **PURCHASE (1), INSTALLMENT_PURCHASE (2), WITHDRAWAL (3)**: Amounts are stored as **negative** (debits)
                                - **PAYMENT (4)**: Amounts are stored as **positive** (credits)

                                Input amounts should always be positive; the sign is automatically adjusted.
                                """)
                        .contact(new Contact()
                                .name("Developer")
                                .email("devfreitag@gmail.com")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local development server")));
    }
}
