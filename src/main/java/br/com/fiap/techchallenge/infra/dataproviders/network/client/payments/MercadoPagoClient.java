package br.com.fiap.techchallenge.infra.dataproviders.network.client.payments;

import br.com.fiap.techchallenge.infra.dataproviders.network.client.payments.config.MercadoPagoFeignConfig;
import br.com.fiap.techchallenge.infra.dataproviders.network.client.payments.model.MercadoPagoPaymentDetailResponse;
import br.com.fiap.techchallenge.infra.dataproviders.network.client.payments.model.MercadoPagoOrderPaymentResponse;
import br.com.fiap.techchallenge.infra.dataproviders.network.client.payments.model.MercadoPagoPaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "mercado-pago-client", url = "${feign.client.mercado-pago.url}", configuration = MercadoPagoFeignConfig.class)
public interface MercadoPagoClient {

    @PutMapping(path = "/instore/qr/seller/collectors/2076431474/stores/52211983000109/pos/52211983000109/orders")
    ResponseEntity<Void> generatePaymentRequest(@RequestBody MercadoPagoPaymentRequest mercadoPagoPaymentRequest);

    @GetMapping(path = "/merchant_orders/{orderId}")
    ResponseEntity<MercadoPagoOrderPaymentResponse> consultOrderDetails(@PathVariable("orderId") String orderId);

    @GetMapping(path = "/v1/payments/{paymentId}")
    ResponseEntity<MercadoPagoPaymentDetailResponse> consultPaymentMethod(@PathVariable("paymentId") Long paymentId);
}
