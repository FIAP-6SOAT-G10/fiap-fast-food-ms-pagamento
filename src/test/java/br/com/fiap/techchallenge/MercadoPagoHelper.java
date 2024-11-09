package br.com.fiap.techchallenge;

import br.com.fiap.techchallenge.infra.dataproviders.network.client.payments.model.MercadoPagoOrderPaymentResponse;

import java.util.List;

public class MercadoPagoHelper {
    public static MercadoPagoOrderPaymentResponse buildMercadoPagoOrderPaymentResponse(List payments) {
        MercadoPagoOrderPaymentResponse mercadoPagoOrderPaymentResponse = new MercadoPagoOrderPaymentResponse();
        mercadoPagoOrderPaymentResponse.setId(9999999991L);
        mercadoPagoOrderPaymentResponse.setStatus("closed");
        mercadoPagoOrderPaymentResponse.setExternalReference("9999999992");
        mercadoPagoOrderPaymentResponse.setPayments(payments);
        mercadoPagoOrderPaymentResponse.setTotalAmount(199.9);
        mercadoPagoOrderPaymentResponse.setPaidAmount(199.9);
        mercadoPagoOrderPaymentResponse.setOrderStatus("closed");
        return mercadoPagoOrderPaymentResponse;
    }
}
