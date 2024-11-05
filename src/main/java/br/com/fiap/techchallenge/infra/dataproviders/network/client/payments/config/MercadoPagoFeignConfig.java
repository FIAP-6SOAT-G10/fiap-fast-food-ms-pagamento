package br.com.fiap.techchallenge.infra.dataproviders.network.client.payments.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class MercadoPagoFeignConfig {

    @Value("${mercado-pago.access-token}")
    private String mercadoPagoAccessToken;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Authorization", mercadoPagoAccessToken);
        };
    }

}
