package com.loananalytics.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI loanAnalyticsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Fineract Loan Analytics API")
                        .description("""
                            REST API for microfinance loan portfolio analytics.
                            Built on top of Apache Fineract data model.
                            
                            Key metrics provided:
                            - Portfolio At Risk (PAR30, PAR90)
                            - Repayment Rate
                            - Branch Performance
                            - Disbursement Trends
                            """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Gifty Rani R")
                                .email("r.giftyrani@gmail.com")));
    }
}
