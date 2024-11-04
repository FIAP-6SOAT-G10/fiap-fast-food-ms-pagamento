package br.com.fiap.techchallenge.infra.dataproviders.network.client.payments.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

public class MercadoPagoFeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Authorization", "Bearer APP_USR-1280609204491025-110319-0ef38ce64565a1827397a0d8f027da22-2076431474");
        };
    }

}
