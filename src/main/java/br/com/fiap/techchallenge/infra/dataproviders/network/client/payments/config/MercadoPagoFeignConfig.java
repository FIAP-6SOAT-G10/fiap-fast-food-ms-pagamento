package br.com.fiap.techchallenge.infra.dataproviders.network.client.payments.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

public class MercadoPagoFeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        String token = System.getenv("MP_TOKEN");

        return requestTemplate -> {
            requestTemplate.header("Authorization", token);
        };
    }

}
