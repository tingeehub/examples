package com.tingee.sdk.example.config;

import com.tingee.sdk.TingeeClient;
import com.tingee.sdk.client.TingeeEnvironment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TingeeConfig {

    @Value("${tingee.client-id:bdc76d6e0b2987316c676ee21bb90238}")
    private String clientId;

    @Value("${tingee.secret-key:OwKVEX9j+0pTGUaXL5FqYb7j0VTeiofsSY+35eEUTu0=}")
    private String secretKey;

    @Bean
    public TingeeClient tingeeClient() {
        return TingeeClient.builder()
                .clientId(clientId)
                .secretKey(secretKey)
                .environment(TingeeEnvironment.UAT)
                .timeout(90000)
                .build();
    }
}
